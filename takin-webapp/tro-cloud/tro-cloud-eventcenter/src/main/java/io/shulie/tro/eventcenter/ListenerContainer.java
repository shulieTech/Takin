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

package io.shulie.tro.eventcenter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import org.springframework.stereotype.Component;

/**
 * @author vincent
 */
@Component
public class ListenerContainer {
    private HashMap<String, Map<String,Listener>> LISTENERS = new HashMap<>();


    /**
     * 获取监听器列表
     *
     * @return
     */
    HashMap<String, Map<String,Listener>> getListeners() {
        return LISTENERS;
    }

    /**
     * @param event
     * @param method
     */
    protected void addListener(IntrestFor event, Object obj, Method method) {

        if (!LISTENERS.containsKey(event.event())) {
            Map<String,Listener> map = Maps.newHashMap();
            LISTENERS.put(event.event(), map);
        }
        Map<String,Listener> map = LISTENERS.get(event.event());
        Class[] parameters = method.getParameterTypes();
        String parameter = Arrays.asList(parameters).stream().map(Class::getName).collect(Collectors.joining(","));
        map.put(method.getClass() + "-" + method.getName() + "-" + parameter,new Listener(event, obj, method));
    }

    /**
     *
     */
    protected static class Listener {
        private IntrestFor intrestFor;
        private Object object;
        private Method method;

        public Listener(IntrestFor intrestFor, Object object, Method method) {
            this.intrestFor = intrestFor;
            this.object = object;
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }

        public IntrestFor getIntrestFor() {
            return intrestFor;
        }

        public Object getObject() {
            return object;
        }



    }
}
