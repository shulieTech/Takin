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

package io.shulie.amdb.adaptors;


import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 适配器工厂类
 * @author vincent
 */
public class AdaptorFactory {

    private Set<Adaptor> adaptors = new HashSet<Adaptor>();

    private void init() {
        ServiceLoader<Adaptor> adaptorLoader = ServiceLoader.load(Adaptor.class);
        for (Adaptor adaptor : adaptorLoader) {
            adaptors.add(adaptor);
        }
    }

    private AdaptorFactory() {
        init();
    }

    private static class AdaptorFactoryHolder {
        public final static AdaptorFactory INSTANCE = new AdaptorFactory();
    }

    public static AdaptorFactory getFactory() {
        return AdaptorFactoryHolder.INSTANCE;
    }

    /**
     * 获取适配器列表
     * @return
     */
    public Set<Adaptor> getAdaptors() {
        return adaptors;
    }

}
