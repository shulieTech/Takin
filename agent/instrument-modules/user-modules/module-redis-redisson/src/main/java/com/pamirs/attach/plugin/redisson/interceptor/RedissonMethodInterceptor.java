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
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.attach.plugin.redisson.RedissonConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ModificationInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.redisson.Redisson;
import org.redisson.RedissonBatch;
import org.redisson.RedissonReactive;
import org.redisson.RedissonRx;
import org.redisson.config.Config;
import org.redisson.reactive.RedissonBatchReactive;
import org.redisson.rx.RedissonBatchRx;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Description org.redisson.Redisson增强
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/8 11:03 上午
 */
public class RedissonMethodInterceptor extends ModificationInterceptorAdaptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        if (!Pradar.isClusterTest()) {
            return args;
        }
        if (args.length == 0) {
            return args;
        }
        if (!(args[0] instanceof String)) {
            return args;
        }

        String name = (String) args[0];
        if (Pradar.isClusterTestPrefix(name)) {
            return args;
        }
        args[0] = Pradar.addClusterTestPrefix(name);

        return args;
    }

    @Override
    public Object getResult0(Advice advice) {
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();

        setConfig(target, result);
        return result;
    }

    private void setConfig(Object target, Object result) {
        if (target instanceof Redisson) {
            Config config = ((Redisson) target).getConfig();
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (target instanceof RedissonRx) {
            Config config = ((RedissonRx) target).getConfig();
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (target instanceof RedissonReactive) {
            Config config = ((RedissonReactive) target).getConfig();
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (target instanceof RedissonBatchReactive) {
            Config config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (target instanceof RedissonBatch) {
            Config config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (target instanceof RedissonBatchRx) {
            Config config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
            manager.setDynamicField(result, RedissonConstants.DYNAMIC_FIELD_CONFIG, config);
        } else if (result instanceof Collection) {
            Collection coll = (Collection) result;
            for (Object obj : coll) {
                setConfig(target, obj);
            }
        } else if (result instanceof Object[]) {
            Object[] arr = (Object[]) result;
            for (Object obj : arr) {
                setConfig(target, obj);
            }
        } else if (result instanceof Iterator) {
            Iterator it = (Iterator) result;
            while (it.hasNext()) {
                setConfig(target, it.next());
            }
        } else if (result instanceof Iterable) {
            Iterator it = ((Iterable) result).iterator();
            if (it != null) {
                while (it.hasNext()) {
                    setConfig(target, it.next());
                }
            }
        }
    }

}
