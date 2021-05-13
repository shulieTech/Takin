/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.common.http;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.exception.ApiException;
import io.shulie.tro.exception.entity.ExceptionCode;
import io.shulie.tro.exception.entity.TroException;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebRequest;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.util.FilterSqlUtil;
import io.shulie.tro.web.common.vo.FileWrapperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName HttpWebClient
 * @Description
 * @Author qianshui
 * @Date 2020/5/11 下午2:33
 */
@Component
@Slf4j
public class HttpWebClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate fileRestTemplate;

    public WebResponse request(WebRequest request) {
        validateParam(request);
        HttpEntity<?> entity = null;
        try {
            ResponseEntity<WebResponse> responseEntity = null;
            if (HttpMethod.GET == request.getHttpMethod()) {
                entity = new HttpEntity<>(null, buildHeader(request));
                responseEntity = restTemplate.exchange(concatGetUrl(request.getRequestUrl(), request),
                        request.getHttpMethod(), entity, WebResponse.class);
            } else {
                entity = new HttpEntity<>(request, buildHeader(request));
                responseEntity = restTemplate.exchange(request.getRequestUrl(), request.getHttpMethod(), entity,
                        WebResponse.class);
            }
            fillHeaderTotal(responseEntity.getHeaders());
            return responseEntity.getBody();
        }catch (Exception e){
            log.error("请求http接口异常，request:{}", JsonHelper.bean2Json(request),e);
            throw new TroException(ExceptionCode.HTTP_REQUEST_ERROR,e.getMessage());
        }
    }

    public WebResponse requestFile(FileWrapperVO fileVO) {
        validateParam(fileVO);
        MultiValueMap<String, FileSystemResource> dataMap = new LinkedMultiValueMap<>();
        List<FileSystemResource> resourceList = Lists.newArrayList();
        fileVO.getFile().stream().forEach(data -> resourceList.add(new FileSystemResource(data)));
        dataMap.put("file", resourceList);
        HttpEntity<MultiValueMap<String, FileSystemResource>> entity = new HttpEntity<>(dataMap,
            buildHeader(fileVO));
        ResponseEntity<WebResponse> responseEntity = fileRestTemplate.exchange(fileVO.getRequestUrl(), HttpMethod.POST,
            entity, WebResponse.class);
        return responseEntity.getBody();
    }

    private void validateParam(WebRequest request) {
        if (request.getLicense() == null) {
            throw ApiException.create(500, "license不能为空");
        }
        if (request.getHttpMethod() == null) {
            throw ApiException.create(500, "请求方式");
        }
    }

    private HttpHeaders buildHeader(WebRequest request) {
        HttpHeaders header = new HttpHeaders();
        header.add(RemoteConstant.LICENSE_REQUIRED, "true");
        header.add(RemoteConstant.LICENSE_KEY, request.getLicense());
        String filterSql = FilterSqlUtil.buildFilterSql(request.getFilterUids());
        if(filterSql != null) {
            header.add(RemoteConstant.FILTER_SQL, filterSql);
        }
        return header;
    }

    private String concatGetUrl(String url, WebRequest request) {
        String jsonString = JSON.toJSONString(request);
        Map<String, Object> dataMap = (Map<String, Object>)JSON.parse(jsonString);
        if (dataMap == null || dataMap.size() == 0) {
            return url;
        }
        StringBuffer sb = new StringBuffer(url);
        sb.append("?");
        dataMap.entrySet().stream().forEach(data -> {
            sb.append("&");
            sb.append(data.getKey());
            sb.append("=");
            sb.append(data.getValue());
        });
        return sb.toString();
    }

    private void fillHeaderTotal(HttpHeaders headers) {
        if (headers == null || headers.size() == 0) {
            return;
        }
        if (!headers.containsKey(RemoteConstant.PAGE_TOTAL_HEADER)) {
            return;
        }
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", RemoteConstant.PAGE_TOTAL_HEADER);
        response.setHeader(RemoteConstant.PAGE_TOTAL_HEADER, headers.get(RemoteConstant.PAGE_TOTAL_HEADER).get(0));
    }
}
