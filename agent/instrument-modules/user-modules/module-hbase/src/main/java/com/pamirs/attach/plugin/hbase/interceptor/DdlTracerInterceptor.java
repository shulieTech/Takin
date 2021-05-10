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
import org.apache.hadoop.hbase.client.TableDescriptor;

/**
 * @Auther: vernon
 * @Date: 2021/2/22 14:31
 * @Description:
 */
public class DdlTracerInterceptor extends TraceInterceptorAdaptor {
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
        /* AliHBaseUEAdmin admin = (AliHBaseUEAdmin) target;*/
        SpanRecord spanRecord = new SpanRecord();
        if ("createTable".equals(advice.getBehavior().getName())) {
            TableDescriptor descriptor = (TableDescriptor) advice.getParameterArray()[0];
            spanRecord.setService(descriptor.getTableName().getNameAsString());
        } else {
            org.apache.hadoop.hbase.TableName tableName
                    = (org.apache.hadoop.hbase.TableName) advice.getParameterArray()[0];
            spanRecord.setService(tableName.getNameAsString());
        }
        spanRecord.setMethod(advice.getBehavior().getName());
        spanRecord.setRequest(advice.getParameterArray()[0]);
        return spanRecord;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        String methodName = advice.getBehavior().getName();
        Object[] args = advice.getParameterArray();
        SpanRecord spanRecord = new SpanRecord();
        if ("createTable".equals(methodName)) {
            TableDescriptor descriptor = (TableDescriptor) args[0];
            spanRecord.setService(descriptor.getTableName().getNameAsString());
        } else {
            org.apache.hadoop.hbase.TableName tableName
                    = (org.apache.hadoop.hbase.TableName) args[0];
            spanRecord.setService(tableName.getNameAsString());
        }
        spanRecord.setMethod(methodName);
        spanRecord.setRequest(args[0]);
        spanRecord.setResponse(advice.getReturnObj());
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        String methodName = advice.getBehavior().getName();
        Object[] args = advice.getParameterArray();
        SpanRecord spanRecord = new SpanRecord();
        if ("createTable".equals(methodName)) {
            TableDescriptor descriptor = (TableDescriptor) args[0];
            spanRecord.setService(descriptor.getTableName().getNameAsString());
        } else {
            org.apache.hadoop.hbase.TableName tableName
                    = (org.apache.hadoop.hbase.TableName) args[0];
            spanRecord.setService(tableName.getNameAsString());
        }
        spanRecord.setMethod(methodName);
        spanRecord.setRequest(args[0]);
        spanRecord.setResponse(advice.getThrowable());
        return spanRecord;
    }
}
