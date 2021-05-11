package io.shulie.amdb.utils;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    @SneakyThrows
    public static String sendGet(String url_, Object object) {
        StringBuffer sb = new StringBuffer();
        try {
            if (!url_.endsWith("?")) {
                url_ += "?";
            }
            url_ += parseParams(object);
            // 创建url资源
            URL url = new URL(url_);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(false);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("GET");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;");
            //log.info("sendGet:{}", url_);
            // 开始连接请求
            conn.connect();
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
            } else {
                log.error("请求失败:Url:{}, ResponseCode:{}, ResponseMessage:{}", url_, conn.getResponseCode(), conn.getResponseMessage());
                throw new Exception("httpGet请求失败:" + conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
        } catch (Exception e) {
            throw e;
        }
        return sb.toString();
    }

    @SneakyThrows
    public static String parseParams(Object object) {
        Map<String, Object> param = (Map<String, Object>) JSON.toJSON(object);
        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                buffer.append(entry.getKey());
                buffer.append("=");
                buffer.append(URLEncoder.encode(entry.getValue().toString(),"UTF-8"));
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    public static String sendGet(String url_){
        return sendGet(url_, null);
    }

    @SneakyThrows
    public static String sendGetWithHeader(String url_, Map<String, String> headerParams){
        StringBuffer sb = new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(url_);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(false);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("GET");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;");
            // 头部参数
            if (headerParams != null && headerParams.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = headerParams.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> node = iterator.next();
                    conn.setRequestProperty(node.getKey(), node.getValue());
                }
            }
            // 开始连接请求
            conn.connect();
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                    String readLine = new String();
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
            } else {
                log.error("请求失败:Url:{}, ResponseCode:{}, ResponseMessage:{}", url_, conn.getResponseCode(), conn.getResponseMessage());
                throw new Exception("httpGet请求失败:" + conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
        } catch (Exception e) {
            throw e;
        }
        return sb.toString();
    }

    @SneakyThrows
    public static String sendPost(String url_, Object object){
        if (!(object instanceof JSON)) {
            object = JSON.toJSON(object);
        }
        StringBuffer sb = new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(url_);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (object.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;");
            // 开始连接请求
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 写入请求的字符串
            out.write((object.toString()).getBytes());
            out.flush();
            out.close();
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                    String readLine = new String();
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
            } else {
                log.error("请求失败:Url:{}, Params:{}, ResponseCode:{}, ResponseMessage:{}", url_, object, conn.getResponseCode(), conn.getResponseMessage());
                throw new Exception("httpPost请求失败:" + conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
        } catch (Exception e) {
            throw e;
        }
        return sb.toString();
    }
}
