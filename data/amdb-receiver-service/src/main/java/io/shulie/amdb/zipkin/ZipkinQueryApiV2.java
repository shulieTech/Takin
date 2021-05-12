/*
 * Copyright 2015-2020 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.shulie.amdb.zipkin;

import com.google.common.primitives.Longs;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.amdb.common.request.trace.EntryTraceQueryParam;
import io.shulie.amdb.service.TraceService;
import io.shulie.pradar.log.rule.RuleFactory;
import io.shulie.surge.data.deploy.pradar.parser.MiddlewareType;
import io.shulie.surge.data.deploy.pradar.parser.PradarLogType;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zipkin2.Endpoint;
import zipkin2.Span;
import zipkin2.codec.SpanBytesEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pamirs.pradar.log.parser.utils.CommonUtils.parseIntQuietly;

@Controller
@RequestMapping(path = "/zipkin")
public class ZipkinQueryApiV2 {
    /**
     * The Cache-Control max-age (seconds) for /api/v2/services /api/v2/remoteServices and
     * /api/v2/spans
     */

    @Autowired
    private TraceService traceService;

    private static final LogFormatter LOG_FORMATTER = new LogFormatter();

    /*  @Get("/api/v2/dependencies")
      @Blocking
      public AggregatedHttpResponse getDependencies(
        @Param("endTs") long endTs,
        @Param("lookback") Optional<Long> lookback) throws IOException {
        Call<List<DependencyLink>> call =
          storage.spanStore().getDependencies(endTs, lookback.orElse(defaultLookback));
        return jsonResponse(DependencyLinkBytesEncoder.JSON_V1.encodeList(call.execute()));
      }

      @RequestMapping("/api/v2/services")
      @ResponseBody
      public String getServiceNames(ServiceRequestContext ctx) throws IOException {
        List<String> serviceNames = storage.serviceAndSpanNames().getServiceNames().execute();
        serviceCount = serviceNames.size();
        return maybeCacheNames(serviceCount > 3, serviceNames, ctx.alloc());
      }

      @Get("/api/v2/spans")
      @Blocking
      public AggregatedHttpResponse getSpanNames(
              @Param("serviceName") String serviceName, ServiceRequestContext ctx)
        throws IOException {
        List<String> spanNames = storage.serviceAndSpanNames().getSpanNames(serviceName).execute();
        return maybeCacheNames(serviceCount > 3, spanNames, ctx.alloc());
      }

      @Get("/api/v2/remoteServices")
      @Blocking
      public AggregatedHttpResponse getRemoteServiceNames(
        @Param("serviceName") String serviceName, ServiceRequestContext ctx)
        throws IOException {
        List<String> remoteServiceNames =
          storage.serviceAndSpanNames().getRemoteServiceNames(serviceName).execute();
        return maybeCacheNames(serviceCount > 3, remoteServiceNames, ctx.alloc());
      }

*/
    @RequestMapping("/api/v2/traces")
    public void getTraces(EntryTraceQueryParam param, HttpServletResponse response)
            throws IOException {

        Map<String, List<RpcBased>> map = traceService.getTraceInfo(param).getData();
        List<List<Span>> list = Lists.newArrayList(map.values().stream().map(rpcBaseds -> rpcBaseds.stream().map(rpcBased ->
                rpcBasedToSpan(rpcBased)
        ).collect(Collectors.toList())).collect(Collectors.toList()));
        writeResponse(writeTraces(SpanBytesEncoder.JSON_V2, list), response);
    }


    @RequestMapping("/api/v2/export")
    public void getExportMany(EntryTraceQueryParam param, HttpServletResponse response)
            throws IOException {
        Map<String, List<RpcBased>> map = traceService.getTraceInfo(param).getData();
        StringBuilder sb = new StringBuilder();
        map.values().forEach(rpcBaseds -> rpcBaseds.forEach(rpcBased -> sb.append("\r\n").append(LOG_FORMATTER.format(rpcBased))));
        writeResponse(sb.toString().getBytes(), response);

    }

    @RequestMapping("/api/v2/export/{traceId}")
    public void getExportOne(@PathVariable String traceId, HttpServletResponse response)
            throws IOException {
        List<RpcBased> rpcBaseds = traceService.getTraceDetail(traceId);
        StringBuilder sb = new StringBuilder();
        rpcBaseds.forEach(rpcBased -> sb.append("\r\n").append(LOG_FORMATTER.format(rpcBased)));
        writeResponse(sb.toString().getBytes(), response);
    }

    /**
     * 整数数组间的比较，前面的数较大，则算大，相等则比较下一位。 前面都相等时，长的数组较大。
     *
     * @param thisIntArray
     * @param thatIntArray
     * @return
     */
    public static int intArrayCompare(int[] thisIntArray, int[] thatIntArray) {
        int size = Math.min(thisIntArray.length, thatIntArray.length);
        for (int i = 0; i < size; i++) {
            int result = thisIntArray[i] - thatIntArray[i];
            if (result != 0) {
                return result;
            }
        }
        return thisIntArray.length - thatIntArray.length;
    }


    @RequestMapping("/api/v2/trace/{traceId}")
    public void getTrace(@PathVariable String traceId, HttpServletResponse response) throws IOException {
        traceId = traceId != null ? traceId.trim() : null;
        List<RpcBased> rpcBaseds = traceService.getTraceDetail(traceId);
        rpcBaseds.sort(new Comparator<RpcBased>() {
            @Override
            public int compare(RpcBased o1, RpcBased o2) {
                // 第一优先级
                int rc = intArrayCompare(parseVersion(o1.getRpcId()), parseVersion(o2.getRpcId()));
                if (rc == 0) {
                    // 第二优先级
                    return Longs.compare(o1.getLogTime(), o2.getLogTime());
                }
                return rc;
            }
        });
        List<Span> spans = rpcBaseds.stream().map(rpcBased ->
                rpcBasedToSpan(rpcBased)
        ).collect(Collectors.toList());
        byte[] body = SpanBytesEncoder.JSON_V2.encodeList(spans);
        writeResponse(body, response);
    }

    /**
     * 将“.” 分隔的版本号、rpcId 切分成整数数组
     *
     * @param str
     * @return
     */
    public static int[] parseVersion(String str) {
        if (str != null) {
            String[] strs = org.apache.commons.lang.StringUtils.split(str, '.');
            int[] ints = new int[strs.length];
            for (int i = 0; i < strs.length; ++i) {
                ints[i] = parseIntQuietly(strs[i], 0);
            }
            return ints;
        }
        return new int[]{0};
    }

    /**
     * 写调用链信息
     *
     * @param response
     * @throws IOException
     */
    public void writeResponse(byte[] body, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        printWriter.write(new String(body, "utf-8"));
    }


    /**
     * @Get("/api/v2/traceMany")
     * @Blocking public AggregatedHttpResponse getTraces(@Param("traceIds") String traceIds) throws IOException {
     * if (traceIds.isEmpty()) {
     * return AggregatedHttpResponse.of(BAD_REQUEST, ANY_TEXT_TYPE, "traceIds parameter is empty");
     * }
     * <p>
     * Set<String> normalized = new LinkedHashSet<>();
     * for (String traceId : traceIds.split(",", 1000)) {
     * if (normalized.add(Span.normalizeTraceId(traceId))) continue;
     * return AggregatedHttpResponse.of(BAD_REQUEST, ANY_TEXT_TYPE, "redundant traceId: " + traceId);
     * }
     * <p>
     * if (normalized.size() == 1) {
     * return AggregatedHttpResponse.of(BAD_REQUEST, ANY_TEXT_TYPE,
     * "Use /api/v2/trace/{traceId} endpoint to retrieve a single trace");
     * }
     * <p>
     * List<List<Span>> traces = storage.traces().getTraces(normalized).execute();
     * return jsonResponse(writeTraces(SpanBytesEncoder.JSON_V2, traces));
     * }
     */


    private Span rpcBasedToSpan(RpcBased rpcBased) {
        Span.Builder builder = Span.newBuilder().traceId(rpcBased.getTraceId()).timestamp(rpcBased.getLogTime() * 1000).id(rpcBased.getRpcId().replaceAll("\\.", "a")).localEndpoint(Endpoint.newBuilder().serviceName(rpcBased.getAppName()).ip(rpcBased.getRemoteIp()).build()).addAnnotation(rpcBased.getLogTime() * 1000, "cs").name("logStartTime").addAnnotation(rpcBased.getLogTime() * 1000 + rpcBased.getCost() * 1000, "cs").name(rpcBased.getMiddlewareName());
        if (StringUtils.isNotBlank(rpcBased.getRpcId()) && (!"0".equals(rpcBased.getRpcId()) || !"9".equals(rpcBased.getRpcId())) && rpcBased.getRpcId().lastIndexOf(".") != -1) {
            builder.parentId(rpcBased.getRpcId().substring(0, rpcBased.getRpcId().lastIndexOf(".")).replaceAll("\\.", "a"));
        }
        builder.duration(rpcBased.getCost() * 1000);
        Span.Kind kind = null;
        switch (rpcBased.getRpcType()) {
            case MiddlewareType
                    .TYPE_CACHE:
            case MiddlewareType
                    .TYPE_DB:
            case MiddlewareType
                    .TYPE_FS:
            case MiddlewareType
                    .TYPE_LOCAL:
            case MiddlewareType
                    .TYPE_SEARCH:
                kind = Span.Kind.CLIENT;
                break;
            case MiddlewareType
                    .TYPE_WEB_SERVER:
                kind = Span.Kind.SERVER;
                break;
            case MiddlewareType
                    .TYPE_RPC:
                kind = rpcBased.getLogType() == PradarLogType.LOG_TYPE_RPC_CLIENT ? Span.Kind.CLIENT : Span.Kind.SERVER;
                break;
            case MiddlewareType
                    .TYPE_MQ:
                kind = rpcBased.getLogType() == PradarLogType.LOG_TYPE_RPC_CLIENT ? Span.Kind.PRODUCER : Span.Kind.CONSUMER;
                break;
        }
        builder.putTag("logType", String.valueOf(rpcBased.getLogType()));
        builder.putTag("rpcId", String.valueOf(rpcBased.getRpcId()));
        builder.putTag("rpcType", String.valueOf(rpcBased.getRpcType()));
        builder.putTag("serviceName", String.valueOf(rpcBased.getServiceName()));
        builder.putTag("method", String.valueOf(rpcBased.getMethodName()));
        builder.putTag("upAppName", String.valueOf(rpcBased.getUpAppName()));
        builder.putTag("resultCode", String.valueOf(rpcBased.getResultCode()));
        builder.putTag("agentId", String.valueOf(rpcBased.getAgentId()));
        builder.putTag("middlewareName", String.valueOf(rpcBased.getMiddlewareName()));
        builder.putTag("callbackMsg", String.valueOf(rpcBased.getCallbackMsg()));
        builder.putTag("request", String.valueOf(rpcBased.getRequest()));
        builder.putTag("response", String.valueOf(rpcBased.getResponse()));
        return builder.build();
    }

    static byte[] writeTraces(SpanBytesEncoder codec, List<List<zipkin2.Span>> traces) {
        // Get the encoded size of the nested list so that we don't need to grow the buffer
        int length = traces.size();
        int sizeInBytes = 2; // []
        if (length > 1) sizeInBytes += length - 1; // comma to join elements

        for (int i = 0; i < length; i++) {
            List<zipkin2.Span> spans = traces.get(i);
            int jLength = spans.size();
            sizeInBytes += 2; // []
            if (jLength > 1) sizeInBytes += jLength - 1; // comma to join elements
            for (int j = 0; j < jLength; j++) {
                sizeInBytes += codec.sizeInBytes(spans.get(j));
            }
        }

        byte[] out = new byte[sizeInBytes];
        int pos = 0;
        out[pos++] = '['; // start list of traces
        for (int i = 0; i < length; i++) {
            pos += codec.encodeList(traces.get(i), out, pos);
            if (i + 1 < length) out[pos++] = ',';
        }
        out[pos] = ']'; // stop list of traces
        return out;
    }
}
