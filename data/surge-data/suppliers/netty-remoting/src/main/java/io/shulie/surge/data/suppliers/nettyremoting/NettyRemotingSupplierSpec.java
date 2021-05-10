package io.shulie.surge.data.suppliers.nettyremoting;


import io.shulie.surge.data.common.factory.GenericFactorySpec;

import java.util.Map;

public class NettyRemotingSupplierSpec implements GenericFactorySpec<NettyRemotingSupplier> {
    private Map<String, String> hostMap;
    private boolean registerZk;

    public NettyRemotingSupplierSpec() {
    }

    public Map<String, String> getHostMap() {
        return hostMap;
    }

    public void setHostMap(Map<String, String> hostMap) {
        this.hostMap = hostMap;
    }

    public boolean isRegisterZk() {
        return registerZk;
    }

    public void setRegisterZk(boolean registerZk) {
        this.registerZk = registerZk;
    }

    @Override
    public String factoryName() {
        return "NettyRemotingSupplier";
    }


    @Override
    public Class<NettyRemotingSupplier> productClass() {
        return NettyRemotingSupplier.class;
    }

}
