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

import com.pamirs.attach.plugin.hbase.HbaseConstants;
import com.pamirs.attach.plugin.hbase.util.HBaseTableNameUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.hadoop.hbase.client.HTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.hbase.interceptor
 * @Date 2019-09-12 17:04
 */
public class HbaseInterceptor extends TraceInterceptorAdaptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbaseInterceptor.class.getName());

    @Override
    public String getPluginName() {
        return HbaseConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return HbaseConstants.PLUGIN_TYPE;
    }

    @Override
    public void beforeFirst(Advice advice) {
        ClusterTestUtils.validateClusterTest();
        check(getTableName((HTable) advice.getTarget()));
    }


    @Override
    public SpanRecord beforeTrace(Advice advice) {
        HTable hTable = (HTable) advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        String tableName = getTableName(hTable);
        spanRecord.setService(tableName);
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        return spanRecord;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        HTable hTable = (HTable) advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        String tableName = getTableName(hTable);
        spanRecord.setService(tableName);
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        spanRecord.setResponse(advice.getReturnObj());
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        HTable hTable = (HTable) advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        String tableName = getTableName(hTable);
        spanRecord.setService(tableName);
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        spanRecord.setResponse(advice.getThrowable());
        return spanRecord;
    }

    private String getTableName(HTable target) {
        String nameAsString = target.getName().getNameAsString();
        return nameAsString;
    }


    /**
     * 检查
     *
     * @param tableName
     */
    private void check(String tableName) {
        tableName = HBaseTableNameUtils.getTableNameNoContainsNameSpace(tableName);
        if (GlobalConfig.getInstance().isShadowHbaseServer()) {
            return;
        }
        if (Pradar.isClusterTest()) {
            if (!Pradar.isClusterTestPrefix(tableName)) {
                throw new PressureMeasureError("[error] forbidden pressurement folwing into biz table.");
            }
        } else if (!Pradar.isClusterTest()) {
            if (Pradar.isClusterTestPrefix(tableName)) {
                LOGGER.error("biz data flowing into pressure table.");
            }
        }
    }

}
