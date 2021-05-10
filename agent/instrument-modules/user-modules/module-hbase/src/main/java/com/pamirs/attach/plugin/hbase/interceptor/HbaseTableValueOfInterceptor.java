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
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * 两个参数的valueOf
 */
public class HbaseTableValueOfInterceptor extends ParametersWrapperInterceptorAdaptor {
    private static Charset charset = Charset.forName("UTF-8");

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

    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args.length != 2) {
            return args;
        }
        if (GlobalConfig.getInstance().isShadowHbaseServer()) {
            return args;
        }
        ClusterTestUtils.validateClusterTest();
        if (Pradar.isClusterTest()) {
            Object obj = args[1];
            if (obj instanceof String) {
                String tableName = (String) obj;
                args[1] = HBaseTableNameUtils.replaceShadowTableName(tableName);
            } else if (obj instanceof byte[]) {
                String tableName = new String((byte[]) obj);
                args[1] = toBytes(HBaseTableNameUtils.replaceShadowTableName(tableName));
            } else if (obj instanceof char[]) {
                String tableName = new String((char[]) obj);
                args[1] = HBaseTableNameUtils.replaceShadowTableName(tableName).toCharArray();
            } else if (obj instanceof ByteBuffer) {
                ByteBuffer tableNameBuffer = (ByteBuffer) obj;
                String tableName = HBaseTableNameUtils.replaceShadowTableName(toString(tableNameBuffer));
                args[1] = ByteBuffer.wrap(toBytes(tableName));

            }
        }
        return args;
    }

    private String toString(ByteBuffer buffer) {
        try {
            return charset.newDecoder().decode(buffer.asReadOnlyBuffer()).toString();
        } catch (CharacterCodingException e) {
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            return new String(data);
        }
    }
}
