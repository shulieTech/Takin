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
package com.pamirs.pradar.internal.config;

import java.util.Map;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 1:24 下午
 */
public class ShadowHbaseConfig {

    private String port;
    private String quorum;
    private String znode;
    private String token;
    private String username;

    private Map<String, String> params;

    public ShadowHbaseConfig(String port, String quorum, String znode, String token, String username, Map<String, String> params) {
        this.port = port;
        this.quorum = quorum;
        this.znode = znode;
        this.token = token;
        this.username = username;
        this.params = params;
    }

    public ShadowHbaseConfig(String quorum, String port, String znode, Map<String, String> params) {
        this.port = port;
        this.quorum = quorum;
        this.znode = znode;
        this.params = params;
    }

    public ShadowHbaseConfig() {
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getQuorum() {
        return quorum;
    }

    public void setQuorum(String quorum) {
        this.quorum = quorum;
    }

    public String getZnode() {
        return znode;
    }

    public void setZnode(String znode) {
        this.znode = znode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object obj) {
        ShadowHbaseConfig that = (ShadowHbaseConfig) obj;
        if (this == that) {
            return true;
        }
        return String.valueOf(this.quorum).equals(String.valueOf(that.getQuorum()))
                && String.valueOf(port).equals(String.valueOf(that.getPort()))
                && String.valueOf(this.znode).equals(String.valueOf(that.getZnode()))
                ;
    }

    public String toKey(){
        String key = quorum.concat("|").concat(port).concat("|").concat(znode).concat("|");
        if (token != null) {
            key = key.concat(token).concat("|");
        }
        if (username != null) {
            key = key.concat(username);
        }
        return key;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ShadowHbaseConfig{" +
                "port='" + port + '\'' +
                ", quorum=" + quorum +
                ", znode='" + znode + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", params=" + params +
                '}';
    }
}
