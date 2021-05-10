package io.shulie.surge.data.sink.clickhouse;

import com.google.inject.Inject;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.runtime.common.DataRuntime;

public class ClickHouseSupportFactory implements GenericFactory<ClickHouseSupport, ClickHouseSupportSpec> {
    @Inject
    private DataRuntime runtime;

    /**
     * 按照参数配置来创建 T
     *
     * @param spec
     * @return
     * @throws Exception 参数不正确，或创建失败时抛出异常
     */
    @Override
    public ClickHouseSupport create(ClickHouseSupportSpec spec) {
        synchronized (ClickHouseSupportFactory.class) {
            ClickHouseSupport clickHouseSupport = new ClickHouseSupport(spec.getUrl(), spec.getUsername(), spec.getPassword(), spec.getBatchCount(), spec.isEnableRound());
            runtime.inject(clickHouseSupport);
            runtime.registShutdownCall(clickHouseSupport, StopLevel.SUPPORT);
            return clickHouseSupport;
        }
    }
}
