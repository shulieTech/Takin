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

package com.pamirs.tro.common.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * 说明:
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/7 16:09
 */
//@Component
public class MysqlDBConf {

    /**
     * 数据库连接地址
     */
    @Value("${mysqldbconf.url}")
    private String url;

    /**
     * 数据库登陆用户名
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * 数据库登陆密码
     */
    @Value("${spring.datasource.password}")
    private String passwd;

    /**
     * 数据库驱动
     */
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    private String publicKey = "xx";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "DBConf{" +
            "url='" + url + '\'' +
            ", username='" + username + '\'' +
            ", passwd='" + passwd + '\'' +
            ", driverClassName='" + driverClassName + '\'' +
            '}';
    }
}
