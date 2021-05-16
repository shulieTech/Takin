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

package io.shulie.surge.data.runtime.common.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.shulie.surge.data.common.factory.GenericFactorySpec;
import io.shulie.surge.data.common.factory.GenericFactorySpecSerializer;
import io.shulie.surge.data.runtime.common.ComplexSpec;
import io.shulie.surge.data.runtime.common.DataRuntime;

@Singleton
public class DefaultGenericFactorySpecSerializer implements GenericFactorySpecSerializer {

    @Inject
    private DataRuntime runtime;

    @Override
    public <T> String toJSONString(GenericFactorySpec<T> spec) {
        if (spec instanceof ComplexSpec) {
            return JSON.toJSONString(spec, SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteClassName);
        }
        return JSON.toJSONString(spec, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public <T> GenericFactorySpec<T> fromJSONString(Class<T> productInterface,
                                                    String name, String jsonString) throws Exception {
        GenericFactorySpec<T> spec = runtime.getGenericFactorySpec(productInterface, name);
        return JSON.<GenericFactorySpec<T>> parseObject(jsonString, spec.getClass());
    }
}
