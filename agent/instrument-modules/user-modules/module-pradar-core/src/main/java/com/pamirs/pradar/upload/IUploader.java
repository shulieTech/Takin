/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.upload;

import com.alibaba.fastjson.JSON;
import com.pamirs.pradar.common.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 上传器，负责将 Agent 以及 Agent 中应用的信息上传到远程
 *
 * @author shiyajian
 * create: 2020-07-20
 */
public interface IUploader {

    /**
     * 是否启用，未启用的不会上报
     */
    boolean enabled();
}

/**
 * 通过 Http 方式上传信息的接口
 */
abstract class HttpUploader<T> implements IUploader {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUploader.class);

    protected String name;

    /**
     * Http 上传对应的接口地址
     */
    protected String postUrl;

    public HttpUploader(String name, String postUrl) {
        this.name = name;
        this.postUrl = postUrl;
    }

    /**
     * 上传到 Http 的数据
     */
    protected abstract T getData();

    /**
     * 开始上报
     */
    public abstract void start();

    /**
     * 内部实际的上报方式，已经封装好了发送 http 的逻辑
     */
    protected HttpUtils.HttpResult innerUpload() {
        String url = buildUrl();
        if (StringUtils.isEmpty(url)) {
            return HttpUtils.HttpResult.result(500, "request error！please check property `pradar.config.center.url` has value in pradar.conf");
        }
        String body = buildBody();
        return HttpUtils.doPost(url, body);
    }

    protected String buildUrl() {
        String configCenterUrl = System.getProperty("pradar.config.center.url");
        if (StringUtils.isEmpty(configCenterUrl)) {
            LOGGER.error("No config center settings, will not to upload agent info, please check property `pradar.config.center.url` has value in pradar.conf");
            return "";
        }
        // 如果不是 http 或者 https 开头的地址，默认增加 http 开头
        // 例如  http://www.baidu.com   -> 无变动
        //     https://www.jd.com      -> 无变动
        //       shulie.io:8080        -> http://shulie.io:8080     (默认是www的)
        //       127.0.0.1:9876        -> http://127.0.0.1:9876
        if (!configCenterUrl.startsWith("http://") && !configCenterUrl.startsWith("https://")) {
            configCenterUrl = "http://" + configCenterUrl;
        }
        // 地址后面如果有斜杠，则把斜杠取消掉
        // 例如  http://www.baidu.com   -> 无变动
        //     https://www.jd.com      -> 无变动
        //       shulie.io:8080/////   -> http://shulie.io:8080     (默认是www的)
        //       127.0.0.1:9876/       -> http://127.0.0.1:9876
        while (!StringUtils.isEmpty(configCenterUrl) && configCenterUrl.endsWith("/")) {
            configCenterUrl = configCenterUrl.substring(0, configCenterUrl.length() - 1);
        }
        if (postUrl == null) {
            LOGGER.error("upload failed, no url setting");
            return "";
        }
        // 如果地址前面有一个或者多个斜杠，把斜杠全部清空掉
        // 例如 `////test/hello`  -> `test/hello` -> `/test/hello`
        while (!StringUtils.isEmpty(postUrl) && postUrl.startsWith("/")) {
            postUrl = postUrl.substring(1);
        }
        postUrl = "/" + postUrl; // 后面再拼上斜杠
        return configCenterUrl + postUrl;
    }

    private String buildBody() {
        T data = getData();
        if (data == null) {
            LOGGER.warn("upload failed,body is null");
            return "";
        }
        String postData;
        if (data instanceof String) {
            postData = (String) data;
        } else {
            postData = JSON.toJSONString(data);
        }
        return postData;
    }
}

