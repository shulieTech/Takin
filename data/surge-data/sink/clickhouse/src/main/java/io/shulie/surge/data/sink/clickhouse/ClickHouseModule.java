package io.shulie.surge.data.sink.clickhouse;

import io.shulie.surge.data.runtime.module.BaseDataModule;

/**
 * click 模块
 *
 * @author vincent
 */
public class ClickHouseModule extends BaseDataModule {
    @Override
    protected void configure() {
        bindGeneric(ClickHouseSupport.class, ClickHouseSupportFactory.class, ClickHouseSupportSpec.class);
        bind(ClickHouseSupport.class).toProvider(ClickHouseSupportProvider.class);
    }
}