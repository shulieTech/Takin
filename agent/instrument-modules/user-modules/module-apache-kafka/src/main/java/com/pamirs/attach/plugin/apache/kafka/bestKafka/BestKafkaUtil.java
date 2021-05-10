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
package com.pamirs.attach.plugin.apache.kafka.bestKafka;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import org.apache.commons.lang.ObjectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author angju
 * @date 2020/8/20 14:31
 */
public class BestKafkaUtil {

    public static Object consturctConsumerFactory(Object targetConsumerFactory) throws Exception {
        if ("org.springframework.kafka.core.DefaultKafkaConsumerFactory".equals(targetConsumerFactory.getClass().getName())) {
            Field field = null;
            try {
                Constructor<?> method = targetConsumerFactory.getClass().getConstructor(Map.class);
                field = targetConsumerFactory.getClass().getDeclaredField("configs");
                field.setAccessible(true);
                Map<String, Object> targetConfigs = (Map<String, Object>) field.get(targetConsumerFactory);
                Map<String, Object> ptConfigs = new HashMap<String, Object>();
                ptConfigs.putAll(targetConfigs);
                ptConfigs.put("group.id", Pradar.addClusterTestPrefix(ObjectUtils.toString(targetConfigs.get("group.id"))));
                return method.newInstance(ptConfigs);
            } catch (Throwable e) {
                throw new PressureMeasureError(e);
            } finally {
                if (field != null) {
                    field.setAccessible(false);
                }
            }

        } else {
            Object ptConsumerFactory = targetConsumerFactory.getClass().newInstance();
            Map<String, Object> targetConfigs = (Map<String, Object>) targetConsumerFactory.getClass().getMethod("getConfigs").invoke(targetConsumerFactory);
            Map<String, Object> targetParentConfigs = (Map<String, Object>) targetConsumerFactory.getClass().getMethod("getParentConfig").invoke(targetConsumerFactory);
            Method setConfigsMethod = targetConsumerFactory.getClass().getMethod("setConfigs", java.util.Map.class);
            Method setParentConfigMethod = targetConsumerFactory.getClass().getMethod("setParentConfig", java.util.Map.class);

            Map<String, Object> ptConfigs = new LinkedHashMap<String, Object>();
            ptConfigs.putAll(targetConfigs);
            ptConfigs.put("group.id", Pradar.addClusterTestPrefix(ObjectUtils.toString(targetConfigs.get("group.id"))));

            setConfigsMethod.invoke(ptConsumerFactory, ptConfigs);
            setParentConfigMethod.invoke(ptConsumerFactory, targetParentConfigs);
            return ptConsumerFactory;
        }

    }
}
