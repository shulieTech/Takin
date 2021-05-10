
package io.shulie.surge.data.sink.mysql;

import com.google.inject.Inject;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.runtime.common.DataRuntime;

public class MysqlSupportFactory implements GenericFactory<MysqlSupport, MysqlSupportSpec> {
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
    public MysqlSupport create(MysqlSupportSpec spec) throws Exception {
        synchronized (MysqlSupportFactory.class) {
            MysqlSupport clickHouseSupport = new MysqlSupport(
                    spec.getUrl(),
                    spec.getUsername(),
                    spec.getPassword(),
                    spec.getInitialSize(),
                    spec.getMinIdle(),
                    spec.getMaxActive());
            runtime.inject(clickHouseSupport);
            runtime.registShutdownCall(clickHouseSupport, StopLevel.SUPPORT);
            return clickHouseSupport;
        }
    }
}
