/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.amdb.adaptors.base;


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
    private Map<String, Object> config;

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
