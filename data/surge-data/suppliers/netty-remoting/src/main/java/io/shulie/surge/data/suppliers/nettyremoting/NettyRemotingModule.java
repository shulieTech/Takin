package io.shulie.surge.data.suppliers.nettyremoting;

import io.shulie.surge.data.runtime.module.BaseDataModule;

public class NettyRemotingModule extends BaseDataModule {

    @Override
    protected void configure() {
        bindGeneric(NettyRemotingSupplier.class, NettyRemotingSupplierFactory.class, NettyRemotingSupplierSpec.class);
    }
}
