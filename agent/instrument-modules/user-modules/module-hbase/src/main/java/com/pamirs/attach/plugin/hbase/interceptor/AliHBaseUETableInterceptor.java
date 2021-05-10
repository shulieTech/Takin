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
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.hadoop.hbase.client.AliHBaseUETable;

/**
 * @author angju
 * @date 2020/12/29 21:06
 */
public class AliHBaseUETableInterceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return HbaseConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return HbaseConstants.PLUGIN_TYPE;
    }


    @Override
    public SpanRecord beforeTrace(Advice advice) {
        AliHBaseUETable hTable = (AliHBaseUETable) advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        String tableName = getTableName(hTable);
        spanRecord.setService(tableName);
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        return spanRecord;
    }


    @Override
    public SpanRecord afterTrace(Advice advice) {
        AliHBaseUETable hTable = (AliHBaseUETable) advice.getTarget();
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
        AliHBaseUETable hTable = (AliHBaseUETable) advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        String tableName = getTableName(hTable);
        spanRecord.setService(tableName);
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        spanRecord.setResponse(advice.getThrowable());
        return spanRecord;
    }


    private String getTableName(AliHBaseUETable target) {
        String nameAsString = target.getName().getNameAsString();
        return nameAsString;
    }
}
