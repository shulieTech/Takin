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
package com.pamirs.attach.plugin.hbase.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ResultInterceptorAdaptor;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.hadoop.hbase.TableName;

/**
 * @author angju
 * @date 2021/1/20 14:06
 */
public class WrappedHBTableGetNameInterceptor extends ResultInterceptorAdaptor {

    @Override
    public Object getResult0(Advice advice) {
        Object result = advice.getReturnObj();
        if (GlobalConfig.getInstance().isShadowHbaseServer()) {
            return result;
        }
        if (!Pradar.isClusterTest()) {
            return result;
        }
        TableName busTableName = (TableName) result;
        if (Pradar.isClusterTestPrefix(busTableName.getNameAsString())) {
            return result;
        }
        TableName ptTableName = null;
        if (busTableName.getNameAsString().contains(":")) {
            String[] bus = busTableName.getNameAsString().split(":");
            ptTableName = TableName.valueOf(bus[0] + ":" + Pradar.addClusterTestPrefix(bus[1]));
        } else {
            ptTableName = TableName.valueOf(busTableName.getNamespaceAsString(), Pradar.addClusterTestPrefix(busTableName.getNameAsString()));
        }

        return ptTableName;
    }
}
