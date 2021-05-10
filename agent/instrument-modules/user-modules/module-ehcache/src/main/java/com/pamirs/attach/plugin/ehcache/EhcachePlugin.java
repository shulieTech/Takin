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
package com.pamirs.attach.plugin.ehcache;


import com.pamirs.attach.plugin.ehcache.interceptor.*;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * @author xiaobin.zfb | xiaobin@shulie.io
 * @since 2020/8/19 10:26 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = EhcacheConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "ehcache 本地缓存")
public class EhcachePlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    @Override
    public void onActive() throws Throwable {

        enhanceTemplate.enhance(this, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getMethod = target.getDeclaredMethod("get", "java.lang.Object");
                getMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod putMethod = target.getDeclaredMethod("put", "java.lang.Object", "java.lang.Object");
                putMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod putIfAbsentMethod = target.getDeclaredMethod("putIfAbsent", "java.lang.Object", "java.lang.Object");
                putIfAbsentMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod containsKeyMethod = target.getDeclaredMethod("containsKey", "java.lang.Object");
                containsKeyMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod removeMethod = target.getDeclaredMethod("remove", "java.lang.Object");
                removeMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod removeMethod0 = target.getDeclaredMethod("remove", "java.lang.Object", "java.lang.Object");
                removeMethod0.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod replaceMethod = target.getDeclaredMethod("replace", "java.lang.Object", "java.lang.Object");
                replaceMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod replaceMethod0 = target.getDeclaredMethod("replace", "java.lang.Object", "java.lang.Object", "java.lang.Object");
                replaceMethod0.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod iteratorMethod = target.getDeclaredMethod("iterator");
                iteratorMethod.addInterceptor(Listeners.of(CacheIteratorInterceptor.class));

                InstrumentMethod getAllMethod = target.getDeclaredMethod("getAll", "java.util.Set");
                getAllMethod.addInterceptor(Listeners.of(CacheGetAllRemoveAllInterceptor.class));

                InstrumentMethod getAllMethod0 = target.getDeclaredMethod("getAll", "java.util.Collection");
                getAllMethod0.addInterceptor(Listeners.of(CacheGetAllRemoveAllInterceptor.class));

                InstrumentMethod removeAllMethod = target.getDeclaredMethod("removeAll", "java.util.Set");
                removeAllMethod.addInterceptor(Listeners.of(CacheGetAllRemoveAllInterceptor.class));
            }
        }, "org.ehcache.core.Ehcache", "org.ehcache.core.EhcacheWithLoaderWriter", "org.ehcache.core.PersistentUserManagedEhcache");


        enhanceTemplate.enhance(this, new EnhanceCallback() {
                    @Override
                    public void doEnhance(InstrumentClass target) {
                        InstrumentMethod putInternalMethod = target.getDeclaredMethods(
                                "put", "putQuiet", "putWithWriter", "putIfAbsent", "removeElement",
                                "replace", "isExpired");
                        putInternalMethod.addInterceptor(Listeners.of(CacheKeyInterceptor0.class));

                        InstrumentMethod method = target.getDeclaredMethods("get", "getQuiet"
                                , "load", "tryRemoveImmediately", "remove", "removeQuiet"
                                , "removeWithWriter", "isElementInMemory", "isElementOffHeap", "isElementOnDisk"
                                , "isKeyInCache", "asynchronousPut", "asynchronousLoad", "acquireReadLockOnKey"
                                , "acquireWriteLockOnKey", "tryReadLockOnKey", "tryWriteLockOnKey"
                                , "releaseReadLockOnKey", "releaseWriteLockOnKey", "isReadLockedByCurrentThread"
                                , "isWriteLockedByCurrentThread", "");
                        method.addInterceptor(Listeners.of(CacheKeyInterceptor1.class));

                        InstrumentMethod getKeysMethod = target.getDeclaredMethods(
                                "getKeys", "getKeysWithExpiryCheck", "getKeysNoDuplicateCheck"
                        );
                        getKeysMethod.addInterceptor(Listeners.of(CacheGetKeysInterceptor.class));

                        InstrumentMethod getWithLoaderMethod = target.getDeclaredMethod("getWithLoader"
                                , "java.lang.Object"
                                , "net.sf.ehcache.loader.CacheLoader"
                                , "java.lang.Object"
                        );
                        getWithLoaderMethod.addInterceptor(Listeners.of(CacheGetWithLoaderInterceptor.class));

                        InstrumentMethod getAllWithLoaderMethod = target.getDeclaredMethod("getAllWithLoader", "java.util.Collection", "java.lang.Object");
                        getAllWithLoaderMethod.addInterceptor(Listeners.of(CacheGetAllWithLoaderInterceptor.class));

                        InstrumentMethod loadAllMethod = target.getDeclaredMethod("loadAll", "java.util.Collection", "java.lang.Object");
                        loadAllMethod.addInterceptor(Listeners.of(CacheLoadAllInterceptor.class));

                        InstrumentMethod collectionMethod = target.getDeclaredMethods("removeAll");
                        collectionMethod.addInterceptor(Listeners.of(CacheParameterCollectionInterceptor.class));

                        InstrumentMethod putAllMethod = target.getDeclaredMethod("putAll", "java.util.Collection");
                        putAllMethod.addInterceptor(Listeners.of(CacheParameterCollectionInterceptor.class));
                    }
                }, "net.sf.ehcache.Cache"
                , "net.sf.ehcache.constructs.blocking.BlockingCache");

        enhanceTemplate.enhance(this, "net.sf.ehcache.Element", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getKeyMethod = target.getDeclaredMethods("getKey", "getObjectKey");
                getKeyMethod.addInterceptor(Listeners.of(ElementGetObjectKeyInterceptor.class));
            }
        });
    }
}
