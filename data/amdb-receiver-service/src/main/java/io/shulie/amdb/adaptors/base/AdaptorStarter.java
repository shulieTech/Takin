package io.shulie.amdb.adaptors.base;


import com.google.common.collect.Maps;
import io.shulie.amdb.adaptors.Adaptor;
import io.shulie.amdb.adaptors.AdaptorTemplate;

import java.util.Map;
import java.util.Set;

/**
 * @author vincent
 */
public class AdaptorStarter {

    private AdaptorTemplate adaptorTemplate;
    private Set<Adaptor> adaptors;
    private Map<String, Object> config = Maps.newHashMap();

    public AdaptorStarter(Map<String, Object> config) throws Exception {
        this.config = config;
    }

    /**
     * adaptor 启动类
     *
     * @throws Exception
     */
    public void start() throws Exception {
        adaptorTemplate = new DefaultAdaptorTemplate();
        adaptors = AdaptorFactory.getFactory().getAdaptors();
        //注入template
        for (Adaptor adaptor : adaptors) {
            adaptor.setAdaptorTemplate(adaptorTemplate);
            adaptor.addConfig(config);
            //添加连接器
            adaptor.addConnector();
        }

        //template 初始化
        adaptorTemplate.init();
        for (Adaptor adaptor : adaptors) {
            adaptor.registor();
        }
        adaptorTemplate.start();
    }

    public void stop() throws Exception {
        adaptorTemplate.close();
    }
}
