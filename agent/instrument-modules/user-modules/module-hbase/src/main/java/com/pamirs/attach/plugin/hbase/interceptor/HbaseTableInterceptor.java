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

import com.pamirs.attach.plugin.hbase.util.HBaseTableNameUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @Auther: vernon
 * @Date: 2020/7/21 11:46
 * @Description:
 */
public class HbaseTableInterceptor extends ParametersWrapperInterceptorAdaptor {
    private final Logger logger = LoggerFactory.getLogger(HbaseTableInterceptor.class);


    private static Charset charset = Charset.forName("UTF-8");

    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        if (GlobalConfig.getInstance().isShadowHbaseServer()) {
            return args;
        }
        if (Pradar.isClusterTest()) {
            if (args.length == 1) {
                Object obj = args[0];
                if (obj instanceof String) {
                    String tableName = (String) obj;
                    args[0] = HBaseTableNameUtils.replaceShadowTableName(tableName);
                } else if (obj instanceof byte[]) {
                    String tableName = new String((byte[]) obj);
                    args[0] = toBytes(HBaseTableNameUtils.replaceShadowTableName(tableName));
                } else if (obj instanceof char[]) {
                    String tableName = new String((char[]) obj);
                    args[0] = HBaseTableNameUtils.replaceShadowTableName(tableName).toCharArray();
                } else {
                    logger.warn("HbaseTableInterceptor arg type is {}", obj.getClass().getName());
                    throw new PressureMeasureError("HbaseTableInterceptor arg type is " + obj.getClass().getName());
                }
            } else {
                logger.warn("HbaseTableInterceptor arg length is {}", args.length);
                throw new PressureMeasureError("HbaseTableInterceptor arg length is " + args.length);
            }

        }
        return args;
    }

    private byte[] toBytes(String str) {
        try {
            return str.getBytes(charset);
        } catch (AbstractMethodError e) {
            try {
                return str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                return str.getBytes();
            }
        }
    }
}
