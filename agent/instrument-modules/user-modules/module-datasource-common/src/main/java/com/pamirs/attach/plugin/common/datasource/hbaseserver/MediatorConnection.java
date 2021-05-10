/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.common.datasource.hbaseserver;

import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowHbaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Hbase影子库协调者
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.hbase.interceptor.shadowserver
 * @Date 2021/4/19 4:45 下午
 */
public abstract class MediatorConnection<T> {

    public static final Map<String, MediatorConnection<?>> mediatorMap = new HashMap<String, MediatorConnection<?>>();

    protected final static Logger logger = LoggerFactory.getLogger(MediatorConnection.class);

    protected T businessConnection;
    protected T performanceTestConnection;
    protected Object businessConfig;

    protected Object [] args;

    public static final String sf_token= "hbase.sf.token";

    public static final String sf_username= "hbase.sf.username";

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setConfiguration(Object businessConfig) {
        this.businessConfig = businessConfig;
    }

    public T getPerformanceTestConnection() {
        if (performanceTestConnection == null) {
            performanceTestConnection = initConnection();
        }
        return performanceTestConnection;
    }

    public void setPerformanceTestConnection(T performanceTestConnection) {
        this.performanceTestConnection = performanceTestConnection;
    }

    public void setBusinessConnection(T businessConnection) {
        this.businessConnection = businessConnection;
    }

    public abstract void closeClusterTest();

    public abstract T initConnection();

    public static void dynamic(ShadowHbaseConfig config) {
        for (Map.Entry<String, ShadowHbaseConfig> entry : GlobalConfig.getInstance().getShadowHbaseServerConfigs().entrySet()) {
            ShadowHbaseConfig hbaseConfig = entry.getValue();
            if (!hbaseConfig.equals(config)) {
                continue;
            }
            MediatorConnection mediator = mediatorMap.get(entry.getKey());
            if (null != mediator) {
                if (mediator.getPerformanceTestConnection() != null) {
                    mediator.closeClusterTest();
                }
                Object ptConfig =  mediator.matching(mediator.businessConfig);
                if (ptConfig != null) {
                    try{
                        mediator.setPerformanceTestConnection(mediator.initConnection());
                    }catch(Exception e){
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    public static void disable(String businessKey) throws Exception{
        for (Map.Entry<String, MediatorConnection<?>> entry : mediatorMap.entrySet()) {
            if (entry.getKey().equals(businessKey)) {
                entry.getValue().closeClusterTest();
            }
        }
    }

    /**
     * 根据业务配置找到对应的影子配置、并创建
     * @param arg1
     * @return
     */
    public abstract Object matching(Object arg1);
}
