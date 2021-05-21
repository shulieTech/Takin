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

package io.shulie.surge.data.runtime.module;

import com.google.inject.ProvisionException;
import com.google.inject.name.Names;
import io.shulie.surge.data.common.factory.GenericFactorySpecSerializer;
import io.shulie.surge.data.runtime.common.DataRuntime;
import io.shulie.surge.data.runtime.common.impl.DefaultDataRuntime;
import io.shulie.surge.data.runtime.common.impl.DefaultGenericFactorySpecSerializer;

import java.util.Enumeration;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;
import static io.shulie.surge.data.common.utils.CommonUtils.isNullEmpty;

/**
 * 基础配置的注入
 *
 * @author pamirs
 */
public class BaseConfigModule extends BaseDataModule {

    @Override
    protected void configure() {
        checkArgument(!isNullEmpty(bootstrap.getProperties()), "Data config properties can not be empty");
        bindConfigConstants();
        bind(GenericFactorySpecSerializer.class).to(DefaultGenericFactorySpecSerializer.class);
        bindDataRuntime();
    }

    /**
     * 绑定配置信息到变量上面，例如
     * <p>
     * <pre>
     * {@code @}Inject
     * {@code @}Named("config.property.key")
     * String foo;
     * </pre>
     * <p>
     * 在配置文件中，定义： <tt>config.property.key=bar</tt>， 运行时 <tt>foo</tt> 会被绑定值 "bar"
     */
    private void bindConfigConstants() {
        Properties properties = bootstrap.getProperties();
        bind(Properties.class).annotatedWith(Names.named("runtime.properties")).toInstance(properties);
        for (Enumeration<Object> e = properties.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String value = (String) properties.get(key);
            bindConstant().annotatedWith(Names.named(key)).to(value);
        }
    }

    private void bindDataRuntime() {
        try {
            bind(DataRuntime.class).toInstance(new DefaultDataRuntime());
        } catch (Exception e) {
            throw new ProvisionException("fail to bind LogRuntime", e);
        }
    }

}
