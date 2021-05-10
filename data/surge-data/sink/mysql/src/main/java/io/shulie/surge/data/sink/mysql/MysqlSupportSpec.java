package io.shulie.surge.data.sink.mysql;


import io.shulie.surge.data.common.factory.GenericFactorySpec;

/**
 * @author vincent
 */

public class MysqlSupportSpec implements GenericFactorySpec<MysqlSupport> {
    private String url;
    private String username;
    private String password;
    private Integer minIdle;
    private Integer initialSize;
    private Integer maxActive;

    @Override
    public String factoryName() {
        return "DefaultClickHouse";
    }

    @Override
    public Class<MysqlSupport> productClass() {
        return MysqlSupport.class;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }
}