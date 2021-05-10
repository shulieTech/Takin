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
