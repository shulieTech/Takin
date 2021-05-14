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

package com.pamirs.tro.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.common.http.HttpResult;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求工具类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
public class HttpUtils {

    /**
     * 发送GET请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    static public String sendGet0(String url, Map<String, String> parameters) {
        String result = "";
        BufferedReader in = null;// 读取响应输入流
        StringBuilder sb = new StringBuilder();// 存储参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {

                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    sb.append(name).append("=").append(
                        java.net.URLEncoder.encode(value,
                            "UTF-8"));
                }

                params = sb.toString();
            } else if (parameters.size() > 1) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    sb.append(name).append("=").append(
                        java.net.URLEncoder.encode(value,
                            "UTF-8")).append("&");
                }

                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            String full_url = url + "?" + params;
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(full_url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection)connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 建立实际的连接
            httpConn.connect();

            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送GET请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    static public String sendGetByCookie(String url, Map<String, String> parameters, String cookie) {
        String result = "";
        BufferedReader in = null;// 读取响应输入流
        StringBuilder sb = new StringBuilder();// 存储参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {

                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    sb.append(name).append("=").append(
                        java.net.URLEncoder.encode(value,
                            "UTF-8"));
                }

                params = sb.toString();
            } else if (parameters.size() > 1) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    sb.append(name).append("=").append(
                        java.net.URLEncoder.encode(value,
                            "UTF-8")).append("&");
                }

                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            String full_url = url + "?" + params;
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(full_url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection)connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.setRequestProperty("Cookie", cookie);
            // 建立实际的连接
            httpConn.connect();

            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送POST请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    static public String sendPost0(String url, Map<String, String> parameters) {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuilder sb = new StringBuilder();// 处理请求参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(java.net.URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                params = sb.toString();
            } else {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(java.net.URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection)connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                .getInputStream(), StandardCharsets.UTF_8));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 发送POST请求
     *
     * @param url        目的地址
     * @param parameters 请求参数
     * @return 远程响应结果
     */
    static public String sendPost1(String url, Map<String, Object> parameters) {
        String jsonParams = JSON.toJSONString(parameters);
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        //StringBuffer sb = new StringBuffer();// 处理请求参数
        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection)connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.setRequestProperty("Content-Type", "application/json");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(jsonParams);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                .getInputStream(), StandardCharsets.UTF_8));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 发送GET请求
     *
     * @param url 目的地址
     * @return 远程响应结果
     */
    static public HttpResult sendGet(String url) {
        HttpResult httpResult = new HttpResult();
        Map<String, String> parameters = new HashMap<>();
        String result = sendGet0(url, parameters);
        if (StringUtils.isNotBlank(result)) {
            httpResult = JSON.parseObject(result, HttpResult.class);
            httpResult = JSON.parseObject(httpResult.getData().toString(), HttpResult.class);
        }
        return httpResult;
    }

    /**
     * 发送POST请求
     *
     * @param url        目的地址
     * @param parameters 请求参数
     * @return 远程响应结果
     */
    static public HttpResult sendPost(String url, Map<String, String> parameters) {
        HttpResult httpResult = new HttpResult();
        String result = sendPost0(url, parameters);
        if (StringUtils.isNotBlank(result)) {
            httpResult = JSON.parseObject(result, HttpResult.class);
            httpResult = JSON.parseObject(httpResult.getData().toString(), HttpResult.class);
        }
        return httpResult;
    }

    /**
     * 发送POST请求
     *
     * @param url    目的地址
     * @param params 请求参数
     * @return 远程响应结果
     */
    static public String post(String strURL, String params) {
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection)
                url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");// 设置请求方式
            //connection.addRequestProperty("Cookie",
            // "csrftoken=FJOA2XTF9R0yuQIqaSybntZjXO20qgmQsK7ob1sMsVhr1YQaEjRE4GbZYVASmZu8;
            // sessionid=mllum7i5p6etaphqawshftwau3cfhuk5");//设置获取的cookie
            //connection.addRequestProperty("X-CSRFTOKEN",
            // "FJOA2XTF9R0yuQIqaSybntZjXO20qgmQsK7ob1sMsVhr1YQaEjRE4GbZYVASmZu8");
            connection.setRequestProperty("Accept", "application/json");// 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json");// 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");// utf-8编码
            out.append(params);
            out.flush();
            out.close(); // 读取响应
            int length = (int)connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "error";

    }

}
