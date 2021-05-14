package io.shulie.tro.utils.http;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shiyajian
 * create: 2020-10-20
 */
@Slf4j
public final class HttpHelper {

    private HttpHelper() { /* no instance */ }

    private static final OkHttpClient DEFAULT_CLIENT = new OkHttpClient.Builder()
        .build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 携带请求头不携带get参数的get方法
     *
     * @return
     */
    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        @NotNull Class<T> responseClazz) {
        if (null == responseClazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        return get(url, headerMap, responseClazz, null, null);
    }

    /**
     * 携带请求头不携带get参数的get方法
     *
     * @return
     */
    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        @NotNull TypeReference<T> typeReference) {
        if (null == typeReference || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        return get(url, headerMap, null, typeReference, null);
    }

    /**
     * 携带请求头不携带get参数的get方法
     *
     * @return
     */
    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        Map<String, Object> params, @NotNull Class<T> responseClazz) {
        if (null == responseClazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        return get(url, headerMap, responseClazz, null, params);
    }

    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        Map<String, Object> params,
        @NotNull TypeReference<T> typeReference) {
        if (null == typeReference || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        return get(url, headerMap, null, typeReference, params);
    }

    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        Object params, @NotNull Class<T> responseClazz) {
        if (null == responseClazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        Map<String, Object> paramMaps = null;
        if (params != null) {
            paramMaps = param2Map(params);
        }
        return get(url, headerMap, responseClazz, null, paramMaps);
    }

    public static <T> TroResponseEntity<T> doGet(@NotNull String url, Map<String, String> headerMap,
        Object params,
        @NotNull TypeReference<T> typeReference) {
        if (null == typeReference || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or responseClazz is null");
        }
        Map<String, Object> paramMaps = null;
        if (params != null) {
            paramMaps = param2Map(params);
        }
        return get(url, headerMap, null, typeReference, paramMaps);
    }

    private static <T> TroResponseEntity<T> get(String url, Map<String, String> headerMap, Class<T> clazz,
        TypeReference<T> typeRef, Map<String, Object> params) {
        if (params != null) {
            StringBuffer sb = new StringBuffer(url);
            if (url.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            params.forEach((key, value) -> {
                if (value != null) {
                    sb.append("&");
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
            });
            url = sb.toString();
        }
        Response response = null;
        Request request = null;
        try {
            request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headerMap))
                .get()
                .build();
            log.debug(String.format("请求地址: [%s]", request.url()));
            response = DEFAULT_CLIENT.newCall(request).execute();
        } catch (Exception e) {
            log.error("发送请求失败", e);
            return TroResponseEntity.error(e.getMessage());
        }

        return TroResponseEntity.create(response, clazz, typeRef);
    }

    /**
     * post Json 请求 class
     *
     * @return
     */
    public static <T> TroResponseEntity<T> doPost(String url, Map<String, String> headerMap, Class<T> clazz,
        Object requestObject) {
        if (null == clazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or clazz is null");
        }
        return postJson(url, headerMap, clazz, null, requestObject);
    }

    /**
     * post Json 请求 泛型传递
     *
     * @return
     */
    public static <T> TroResponseEntity<T> doPost(String url, Map<String, String> headerMap,
        TypeReference<T> typeRef, Object requestObject) {
        if (null == typeRef || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or typeRef is null");
        }
        return postJson(url, headerMap, null, typeRef, requestObject);
    }

    private static <T> TroResponseEntity<T> postJson(String url, Map<String, String> headerMap, Class<T> clazz,
        TypeReference<T> typeRef, Object requestObject) {
        RequestBody body = RequestBody.create(JSON, JsonHelper.bean2Json(requestObject));
        Request request = new Request.Builder()
            .url(url)
            .headers(getHeaders(headerMap))
            .post(body)
            .build();
        log.debug(String.format("请求地址: [%s]", request.url()));
        log.debug(String.format("请求参数: %s", JsonHelper.bean2Json(request)));
        log.debug(String.format("请求类型: %s",
            Objects.requireNonNull(Objects.requireNonNull(request.body()).contentType()).toString()));
        Response response = null;
        try {
            response = DEFAULT_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TroResponseEntity.create(response, clazz, typeRef);
    }

    public static <T> TroResponseEntity<T> doPut(String url, Map<String, String> headerMap, Class<T> clazz,
        Object requestObject) {
        if (null == clazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or clazz is null");
        }
        return putJson(url, headerMap, clazz, null, requestObject);
    }

    /**
     * put 方法发请求 泛型
     */
    public static <T> TroResponseEntity<T> doPut(String url, Map<String, String> headerMap,
        TypeReference<T> typeRef, Object requestObject) {
        if (null == typeRef || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or typeRef is null");
        }
        return putJson(url, headerMap, null, typeRef, requestObject);
    }

    private static <T> TroResponseEntity<T> putJson(String url, Map<String, String> headerMap, Class<T> clazz,
        TypeReference<T> typeRef, Object requestObject) {
        RequestBody body = RequestBody.create(JSON, JsonHelper.bean2Json(requestObject));
        Request request = new Request.Builder()
            .url(url)
            .headers(getHeaders(headerMap))
            .put(body)
            .build();
        log.debug(String.format("请求地址: [%s]", request.url()));
        log.debug(String.format("请求参数: %s", JsonHelper.bean2Json(request)));
        log.debug(String.format("请求类型: %s",
            Objects.requireNonNull(Objects.requireNonNull(request.body()).contentType()).toString()));
        Response response = null;
        try {
            response = DEFAULT_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TroResponseEntity.create(response, clazz, typeRef);
    }

    public static <T> TroResponseEntity<T> doDelete(String url, Map<String, String> headerMap, Class<T> clazz,
        Object requestObject) {
        if (null == clazz || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or clazz is null");
        }
        return deleteJson(url, headerMap, clazz, null, requestObject);
    }

    /**
     * delete 方法发请求 泛型
     */
    public static <T> TroResponseEntity<T> doDelete(String url, Map<String, String> headerMap,
        TypeReference<T> typeRef, Object requestObject) {
        if (null == typeRef || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty or typeRef is null");
        }
        return deleteJson(url, headerMap, null, typeRef, requestObject);
    }

    private static <T> TroResponseEntity<T> deleteJson(String url, Map<String, String> headerMap, Class<T> clazz,
        TypeReference<T> typeRef, Object requestObject) {
        RequestBody body = RequestBody.create(JSON, JsonHelper.bean2Json(requestObject));
        Request request = new Request.Builder()
            .url(url)
            .headers(getHeaders(headerMap))
            .delete(body)
            .build();
        log.debug(String.format("请求地址: [%s]", request.url()));
        log.debug(String.format("请求参数: %s", JsonHelper.bean2Json(request)));
        log.debug(String.format("请求类型: %s",
            Objects.requireNonNull(Objects.requireNonNull(request.body()).contentType()).toString()));
        Response response = null;
        try {
            response = DEFAULT_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TroResponseEntity.create(response, clazz, typeRef);
    }

    private static Map<String, Object> param2Map(Object params) {
        String jsonString = JsonHelper.bean2Json(params);
        return JsonHelper.json2Map(jsonString, String.class, Object.class);
    }

    private static Headers getHeaders(Map<String, String> headerMap) {
        if (headerMap == null) {
            return new Headers.Builder().build();
        }
        Map<String, String> resultMap = Maps.newConcurrentMap();
        headerMap.forEach((key, value) -> {
            if (key != null && value != null) {
                resultMap.put(key, value);
            }
        });
        if (resultMap.isEmpty()) {
            return new Headers.Builder().build();
        }
        return Headers.of(resultMap);
    }
}
