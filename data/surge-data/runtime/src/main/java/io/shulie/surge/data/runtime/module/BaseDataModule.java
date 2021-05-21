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

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.factory.GenericFactorySpec;
import io.shulie.surge.data.runtime.common.*;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 基础的 Log {@link Module 模块}，建议需要 install 到 {@link DataBootstrap}
 * 的模块都继承这个类。实现如果自己另外有设置 Field，需要考虑到序列化问题，
 * 或设置成 <code>transient</code>。
 *
 * @author pamirs
 */
public abstract class BaseDataModule extends AbstractModule
        implements Serializable, DataBootstrapAware {

    private static final long serialVersionUID = 1L;

    private static final transient Field BIND_FIELD;
    private static final transient Method GENERIC_FACTORY_TYPE_LITERAL;
    protected DataBootstrap bootstrap;


    static {
        Field f;
        try {
            f = AbstractModule.class.getDeclaredField("binder");
            f.setAccessible(true);
        } catch (Throwable t) {
            f = null;
        }
        BIND_FIELD = f;

        Method m;
        try {
            m = Class.forName("io.shulie.surge.data.runtime.common.impl.DefaultDataRuntime").getDeclaredMethod(
                    "getGenericFactoryTypeLiteral", Class.class);
            m.setAccessible(true);
        } catch (Throwable t) {
            throw new NoSuchMethodError("fail to get method DefaultLogRuntime."
                    + "getGenericFactoryTypeLiteral(Class), error=" + t.getMessage());
        }
        GENERIC_FACTORY_TYPE_LITERAL = m;
    }

    protected Object writeReplace() throws ObjectStreamException {
        // 把父类中无法被序列化的 binder 设置为 null
        if (super.binder() != null && BIND_FIELD != null) {
            try {
                BIND_FIELD.set(this, null);
            } catch (Throwable t) {
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    protected <T> TypeLiteral<GenericFactory<T, GenericFactorySpec<T>>>
    getGenericFactoryTypeLiteral(Class<T> productInterface) {
        try {
            return (TypeLiteral<GenericFactory<T, GenericFactorySpec<T>>>) GENERIC_FACTORY_TYPE_LITERAL.invoke(null,
                    productInterface);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提供配置化的服务绑定
     *
     * @param productInterface 工厂对象生成的接口实例
     * @param factoryClass     工厂实现
     * @param specClass        配置描述的实现
     *                         使用 {@link GenericFactorySpec#factoryName()} 获取
     * @see DataRuntime#getGenericFactory(Class, String)
     * @see DataRuntime#getGenericFactorySpec(Class, String)
     * @see DataRuntime#createGenericInstance(Class, String, String)
     */
    protected <T, S0 extends GenericFactorySpec<T>, F extends GenericFactory<T, S0>, S1 extends S0>
    void bindGeneric(Class<T> productInterface, Class<F> factoryClass, Class<S1> specClass) {
        bindGeneric(productInterface, factoryClass, specClass, null);
    }

    /**
     * 提供配置化的服务绑定
     *
     * @param productInterface 工厂对象生成的接口实例
     * @param factoryClass     工厂实现，需要返回 T 的具体实例
     * @param specClass        配置描述的实现，
     *                         这个实现需要能和 {@link GenericFactory#create(GenericFactorySpec)} 一起使用
     * @see DataRuntime#getGenericFactory(Class, String)
     * @see DataRuntime#getGenericFactorySpec(Class, String)
     * @see DataRuntime#createGenericInstance(Class, String, String)
     */
    @SuppressWarnings("unchecked")
    protected <T, S0 extends GenericFactorySpec<T>, F extends GenericFactory<T, S0>, S1 extends S0>
    void bindGeneric(Class<T> productInterface, Class<F> factoryClass,
                     Class<S1> specClass, String name) {
        S1 specInstance;
        try {
            specInstance = specClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("the spec class can not be created by "
                    + "default constructor: " + specClass.getName() + "()", e);
        }

        if (productInterface != specInstance.productClass()) {
            throw new IllegalArgumentException(specClass.getName() + ".getProductClass() "
                    + "does not return " + productInterface.getName());
        }

        String factoryName = (name == null ? specInstance.factoryName() : name);

        // 绑定后，可以在代码中实现下面的注入：
        // @Inject
        // @Named("theName")
        // private GenericFactory<ProductInterface, GenericFactorySpec<ProductInterface>> factory;
        Class<?> tmp = factoryClass;
        bind(getGenericFactoryTypeLiteral(productInterface))
                .annotatedWith(Names.named(factoryName)).to(
                (Class<GenericFactory<T, GenericFactorySpec<T>>>) tmp);
        bind(GenericFactorySpec.class).annotatedWith(
                Names.named(productInterface.getName() + "@" + factoryName)).toInstance(specInstance);
    }

    @Override
    public void setDataBootstrap(DataBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
}
