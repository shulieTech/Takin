package io.shulie.surge.data.sink.influxdb;

import io.shulie.surge.data.runtime.module.BaseDataModule;

/**
 * hbase 模块
 *
 * @author vincent
 */
public class InfluxDBModule extends BaseDataModule {

    @Override
    protected void configure() {
        bind(InfluxDBSupport.class).toProvider(InfluxDBSupportProvider.class);
    }
}