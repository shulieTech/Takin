package io.shulie.amdb.adaptors.starter;

import io.shulie.amdb.service.AppInstanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ClientAdaptorStarter extends DefaultFactoryAdaptorStarter {

    @Autowired
    private AppInstanceService appInstanceService;


    public ClientAdaptorStarter(Map<String, Object> config) {
        super(config);
    }

    @Override
    void afterStart() {

    }

    @Override
    public void beforeStart() {
        //solve problem that app instance flag synchronization failed when AMDB shutdown
        //step 1. set flag offline
        //step 2. acquire flag from zk ,ps: see InstanceAdaptor#process
        appInstanceService.initOnlineStatus();
    }
}
