package io.shulie.tro.monitor.util;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @author: zhaoyong
 * @date: Create in 11:09 2018/3/30
 * @description:
 */
@Slf4j
public class HttpClientUtils {


    private static HttpClientUtils instance  = new HttpClientUtils();

    private HttpClientUtils() {
    }

    public static HttpClientUtils getInstance() {
        if (instance == null) {
            instance = new HttpClientUtils();
        }
        return instance;
    }

    private  RequestConfig  requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000).build();

    public String doJsonPost(String url, String param) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建默认的httpClient实例,和发送get请求基本一致
            HttpPost httpPost = new HttpPost(url);
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            StringEntity se = new StringEntity(param, "utf-8");
            // post方法中，加入json数据
            httpPost.setEntity(se);
            httpPost.setHeader("Content-Type", "application/json");

            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                }
            }
        } catch (Exception e) {
            log.error("send http post is error!",e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e2) {
                log.error("send http post is error!",e2);
            }
        }
        return result;
    }
}
