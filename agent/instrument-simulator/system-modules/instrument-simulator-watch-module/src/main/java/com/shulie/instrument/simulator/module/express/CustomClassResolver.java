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
package com.shulie.instrument.simulator.module.express;

import ognl.ClassResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see ognl.DefaultClassResolver
 */
public class CustomClassResolver implements ClassResolver {

    public static final CustomClassResolver customClassResolver = new CustomClassResolver();

    private Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>(101);

    private CustomClassResolver() {

    }

    @Override
    public Class<?> classForName(String className, Map context) throws ClassNotFoundException {
        Class<?> result = null;

        if ((result = classes.get(className)) == null) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader != null) {
                    result = classLoader.loadClass(className);
                } else {
                    result = Class.forName(className);
                }
            } catch (ClassNotFoundException ex) {
                if (className.indexOf('.') == -1) {
                    result = Class.forName("java.lang." + className);
                    classes.put("java.lang." + className, result);
                }
            }
            classes.put(className, result);
        }
        return result;
    }
}
