package io.shulie.amdb.run;

import io.shulie.amdb.adaptors.base.AdaptorStarter;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Order(value = 1000)
public class AdaptorStarterRunner implements ApplicationRunner {

    @Value("${zookeeper.server}")
    private String zkPath;

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.adaptor.instance.open}")
    private boolean isOpen;

    @Autowired
    private AppService appService;

    @Autowired
    private AppInstanceService appInstanceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!isOpen) {
            return;
        }
        try {
            Map<String, Object> config = new HashMap<String, Object>();
            config.put("appService", appService);
            config.put("appInstanceService", appInstanceService);
            System.setProperty("zookeeper.servers", zkPath);
            AdaptorStarter starter = new AdaptorStarter(config);
            starter.start();
        } catch (Exception e) {
            log.error("adaptor启动失败:{}", e);
        }
    }
}
