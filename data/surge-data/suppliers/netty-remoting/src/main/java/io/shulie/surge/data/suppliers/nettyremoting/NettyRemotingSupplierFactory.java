package io.shulie.surge.data.suppliers.nettyremoting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.lifecycle.LifecycleObserver;
import io.shulie.surge.data.runtime.common.DataRuntime;
import io.shulie.surge.data.runtime.supplier.Supplier;

@Singleton
public class NettyRemotingSupplierFactory implements GenericFactory<NettyRemotingSupplier, NettyRemotingSupplierSpec> {
    @Inject
    private DataRuntime runtime;

    @Override
    public NettyRemotingSupplier create(NettyRemotingSupplierSpec syncSpec) {
        NettyRemotingSupplier supplier = new NettyRemotingSupplier();
        runtime.inject(supplier);
        LifecycleObserver<Supplier> logSupplierConfigSynchronize = new NettyRemotingSupplierObserver(syncSpec.getHostMap(), syncSpec.isRegisterZk());
        runtime.inject(logSupplierConfigSynchronize);
        supplier.addObserver(logSupplierConfigSynchronize);
        return supplier;
    }
}
