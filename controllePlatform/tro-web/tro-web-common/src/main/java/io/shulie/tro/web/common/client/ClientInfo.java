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

package io.shulie.tro.web.common.client;

import java.util.Date;
import java.util.List;

/**
 * @author shiyajian
 * create: 2020-10-04
 */
public class ClientInfo {

    public static final ClientInfo INSTANCE = new ClientInfo();
    /**
     * 客户端版本
     */
    private String version;
    /**
     * 客户端构建时间
     */
    private String buildTimestamp;
    /**
     * tro-web 控制台的key;
     */
    private String clientKey;
    /**
     * 控制台支持的客户集合
     */
    private List<String> customers;
    /**
     * 控制台的秘钥过期时间
     */
    private Date invalidDate;

    private ClientInfo() { /* no instance */ }

    public static class CustomerInfo {

        /**
         * 客户名称
         */
        private String customerName;

        /**
         * 客户的key
         */
        private String customerKey;
    }
}
