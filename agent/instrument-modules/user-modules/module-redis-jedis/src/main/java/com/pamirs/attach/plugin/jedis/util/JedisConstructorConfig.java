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
package com.pamirs.attach.plugin.jedis.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClusterHostAndPortMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.jedis.util
 * @Date 2020/11/27 5:30 下午
 */
public class JedisConstructorConfig {

    /*************************JedisPool***********************/
    private GenericObjectPoolConfig poolConfig;
    private int connectionTimeout;
    private int soTimeout;
    private String password;
    private int database;
    private String clientName;
    private boolean ssl;
    private SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;

    private String host;
    private int port;

    private URI uri;
    /*************************JedisCluster***********************/
    private HostAndPort node;
    private List<HostAndPort> nodes;
    private Set<HostAndPort> setNodes;
    private JedisClusterHostAndPortMap jedisMap;


    /*************************JedisSentinelPool***********************/
    private String master;
    private Set<String> sentinels;

    /**
     * JedisPool 1 - 99
     * 1
     * 1、JedisPool(final String host)
     * 4
     * 2、JedisPool(final String host, final SSLSocketFactory sslSocketFactory,final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
     * 7
     * 3、JedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,final int connectionTimeout, final int soTimeout, final String password, final int database,final String clientName)
     * 4、JedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,final int connectionTimeout, final int soTimeout, final String password, final int database,final String clientName, final boolean ssl, final SSLSocketFactory sslSocketFactory,final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
     * 5、JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,final int connectionTimeout, final int soTimeout)
     * 6、JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,final int connectionTimeout, final int soTimeout, final SSLSocketFactory sslSocketFactory,final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
     *
     * JedisSentinel 100 - 199
     * 100 - length(8)
     * public JedisSentinelPool(String masterName, Set<String> sentinels,
     *       final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
     *       final String password, final int database, final String clientName)
     *
     * JedisCluster 200 - 299
     * 200 - length(11)
     * JedisClusterConnectionHandler(Set<HostAndPort> nodes,final GenericObjectPoolConfig poolConfig,
     * int connectionTimeout, int soTimeout, String password, String clientName,boolean ssl,
     * SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,HostnameVerifier hostnameVerifier,
     * JedisClusterHostAndPortMap portMap) {
     */
    private int constructorType;

    public JedisConstructorConfig() {
    }



    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public JedisConstructorConfig setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public JedisConstructorConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public JedisConstructorConfig setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public JedisConstructorConfig setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public SSLParameters getSslParameters() {
        return sslParameters;
    }

    public JedisConstructorConfig setSslParameters(SSLParameters sslParameters) {
        this.sslParameters = sslParameters;
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public JedisConstructorConfig setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public String getHost() {
        return host;
    }

    public JedisConstructorConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public JedisConstructorConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public JedisConstructorConfig setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public int getConstructorType() {
        return constructorType;
    }

    public JedisConstructorConfig setConstructorType(int constructorType) {
        this.constructorType = constructorType;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public JedisConstructorConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getDatabase() {
        return database;
    }

    public JedisConstructorConfig setDatabase(int database) {
        this.database = database;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public JedisConstructorConfig setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public boolean isSsl() {
        return ssl;
    }

    public JedisConstructorConfig setSsl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public HostAndPort getNode() {
        return node;
    }

    public JedisConstructorConfig setNode(HostAndPort node) {
        this.node = node;
        return this;
    }

    public List<HostAndPort> getNodes() {
        return nodes;
    }

    public JedisConstructorConfig setNodes(List<HostAndPort> nodes) {
        this.nodes = nodes;
        return this;
    }

    public String getMaster() {
        return master;
    }

    public JedisConstructorConfig setMaster(String master) {
        this.master = master;
        return this;
    }

    public Set<String> getSentinels() {
        return sentinels;
    }

    public JedisConstructorConfig setSentinels(Set<String> sentinels) {
        this.sentinels = sentinels;
        return this;
    }

    public Set<HostAndPort> getSetNodes() {
        return setNodes;
    }

    public JedisConstructorConfig setSetNodes(Set<HostAndPort> setNodes) {
        this.setNodes = setNodes;
        return this;
    }

    public JedisClusterHostAndPortMap getJedisMap() {
        return jedisMap;
    }

    public JedisConstructorConfig setJedisMap(JedisClusterHostAndPortMap jedisMap) {
        this.jedisMap = jedisMap;
        return this;
    }
}
