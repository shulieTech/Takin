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

package com.pamirs.tro.entity.domain.query;

/**
 * 说明: 数据源实体类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/8 16:59
 */
public class Conf {

    /**
     * 数据库连接地址
     */
    private String url;

    /**
     * 数据库登陆用户名
     */
    private String username;

    /**
     * 数据库登陆密码
     */
    private String passwd;

    /**
     * 数据库驱动
     */
    private String driverClassName;

    /**
     * 公钥解密字符串
     */
    private String publicKey;

    /**
     * Gets the value of url.
     *
     * @return the value of url
     * @author shulie
     * @version 1.0
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * <p>You can use getUrl() to get the value of url</p>
     *
     * @param url url
     * @author shulie
     * @version 1.0
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the value of username.
     *
     * @return the value of username
     * @author shulie
     * @version 1.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * <p>You can use getUsername() to get the value of username</p>
     *
     * @param username username
     * @author shulie
     * @version 1.0
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the value of passwd.
     *
     * @return the value of passwd
     * @author shulie
     * @version 1.0
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Sets the passwd.
     *
     * <p>You can use getPasswd() to get the value of passwd</p>
     *
     * @param passwd passwd
     * @author shulie
     * @version 1.0
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * Gets the value of driverClassName.
     *
     * @return the value of driverClassName
     * @author shulie
     * @version 1.0
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Sets the driverClassName.
     *
     * <p>You can use getDriverClassName() to get the value of driverClassName</p>
     *
     * @param driverClassName driverClassName
     * @author shulie
     * @version 1.0
     */
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
        return "Conf{" +
            "url='" + url + '\'' +
            ", username='" + username + '\'' +
            ", passwd='" + passwd + '\'' +
            ", driverClassName='" + driverClassName + '\'' +
            ", publicKey='" + publicKey + '\'' +
            '}';
    }

}
