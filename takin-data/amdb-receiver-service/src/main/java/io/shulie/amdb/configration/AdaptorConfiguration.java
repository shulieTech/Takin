package io.shulie.amdb.configration;

import io.shulie.amdb.adaptors.starter.ClientAdaptorStarter;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AdaptorConfiguration {

    @Value("${zookeeper.server}")
    private String zkPath;
    @Autowired
    private AppService appService;
    @Autowired
    private AppInstanceService appInstanceService;

    @Bean
    public ClientAdaptorStarter adaptorStarter() throws Exception {
        Map<String, Object> config = new HashMap<>(2);
        config.put("appService", appService);
        config.put("appInstanceService", appInstanceService);
        System.setProperty("zookeeper.servers", zkPath);
        return new ClientAdaptorStarter(config);
    }
}
