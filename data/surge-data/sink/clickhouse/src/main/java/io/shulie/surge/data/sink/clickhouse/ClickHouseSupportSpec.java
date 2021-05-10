package io.shulie.surge.data.sink.clickhouse;


import io.shulie.surge.data.common.factory.GenericFactorySpec;

/**
 * @author vincent
 */
public class ClickHouseSupportSpec implements GenericFactorySpec<ClickHouseSupport> {
    private String url;
    private String username;
    private String password;
    private int batchCount;
    private boolean enableRound;

    @Override
    public String factoryName() {
        return "DefaultClickHouse";
    }

    @Override
    public Class<ClickHouseSupport> productClass() {
        return ClickHouseSupport.class;
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

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public boolean isEnableRound() {
        return enableRound;
    }

    public void setEnableRound(boolean enableRound) {
        this.enableRound = enableRound;
    }
}
