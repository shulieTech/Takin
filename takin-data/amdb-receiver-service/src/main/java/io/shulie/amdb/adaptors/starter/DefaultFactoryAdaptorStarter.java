package io.shulie.amdb.adaptors.starter;

import io.shulie.amdb.adaptors.Adaptor;
import io.shulie.amdb.adaptors.AdaptorTemplate;
import io.shulie.amdb.adaptors.base.AdaptorFactory;
import io.shulie.amdb.adaptors.base.DefaultAdaptorTemplate;

import java.util.Map;
import java.util.Set;

public class DefaultFactoryAdaptorStarter extends AbstractAdaptorStarter {

    private AdaptorTemplate adaptorTemplate;
    private final Map<String, Object> config;
    private final AdaptorFactory adaptorFactory;

    public DefaultFactoryAdaptorStarter(Map<String, Object> config) {
        adaptorFactory = AdaptorFactory.getFactory();
        this.config = config;
    }

    protected void doStart() throws Exception {
        adaptorTemplate = new DefaultAdaptorTemplate();
        Set<Adaptor> adaptors = adaptorFactory.getAdaptors();
        //注入template
        for (Adaptor adaptor : adaptors) {
            adaptor.setAdaptorTemplate(adaptorTemplate);
            adaptor.addConfig(config);
            //添加连接器
            adaptor.addConnector();
        }

        //template 初始化
        adaptorTemplate.init();
        adaptors.forEach(Adaptor::registor);
        adaptorTemplate.start();
    }

    @Override
    void afterStart() {

    }

    @Override
    void beforeStart() {

    }

    public void stop() throws Exception {
        adaptorTemplate.close();
    }
}
