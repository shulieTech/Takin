
package io.shulie.surge.data.sink.mysql;

import io.shulie.surge.data.runtime.module.BaseDataModule;


/**
 * click 模块
 *
 * @author vincent
 */

public class MysqlModule extends BaseDataModule {
    @Override
    protected void configure() {
        bindGeneric(MysqlSupport.class, MysqlSupportFactory.class, MysqlSupportSpec.class);
        bind(MysqlSupport.class).toProvider(MysqlSupportProvider.class);
    }
}
