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
package com.shulie.instrument.simulator.core.classloader;

import com.shulie.instrument.simulator.api.annotation.Stealth;
import com.shulie.instrument.simulator.api.spi.ModuleJarUnLoadSpi;
import com.shulie.instrument.simulator.core.util.ReflectUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;

import static com.shulie.instrument.simulator.api.util.StringUtil.getJavaClassName;

/**
 * 模块类加载器
 */
@Stealth
public class ModuleClassLoader extends ModuleRoutingURLClassLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final File moduleJarFile;
    private final File tempModuleJarFile;
    private final long checksumCRC32;

    private static File copyToTempFile(final File moduleJarFile) throws IOException {
        File tempFile = File.createTempFile("instrument_simulator_module_jar_" + System.nanoTime(), ".jar");
        tempFile.deleteOnExit();
        FileUtils.copyFile(moduleJarFile, tempFile);
        return tempFile;
    }

    public ModuleClassLoader(final ClassLoaderService classLoaderService, final File moduleJarFile, final String moduleId) throws IOException {
        this(classLoaderService, moduleJarFile, copyToTempFile(moduleJarFile), moduleId);
    }

    private ModuleClassLoader(final ClassLoaderService classLoaderService,
                              final File moduleJarFile,
                              final File tempModuleJarFile,
                              final String moduleId) throws IOException {
        /**
         * 定义优化从框架加载的包列表
         * 每次使用拷贝临时文件的方式来加载模块，防止原有模式文件的变更导致运行期出现错误
         */
        super(moduleId,
                classLoaderService,
                new URL[]{new URL("file:" + tempModuleJarFile.getCanonicalPath())},
                new Routing(
                        ModuleClassLoader.class.getClassLoader(),
                        //声明所有模块需要找框架加载的类列表,模块类加载会优先从这份列表中加载
                        "^com\\.shulie\\.instrument\\.simulator\\.api\\..*",
                        "^com\\.shulie\\.instrument\\.simulator\\.spi\\..*",
                        "^org\\.apache\\.commons\\.lang\\..*",
                        "^org\\.codehaus\\.groovy\\..*",
                        "^groovy\\..*",
                        "^org\\.slf4j\\..*",
                        "^ch\\.qos\\.logback\\..*",
                        "^org\\.objectweb\\.asm\\..*",
                        "^javax\\.annotation\\.Resource.*$"
                )
        );
        this.checksumCRC32 = FileUtils.checksumCRC32(moduleJarFile);
        this.moduleJarFile = moduleJarFile;
        this.tempModuleJarFile = tempModuleJarFile;

        try {
            cleanProtectionDomainWhichCameFromModuleJarClassLoader();
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: clean ProtectionDomain in {}'s acc success.", this);
            }
        } catch (Throwable e) {
            logger.warn("SIMULATOR: clean ProtectionDomain in {}'s acc failed.", this, e);
        }

    }

    /**
     * 清理来自URLClassLoader.acc.ProtectionDomain[]中，来自上一个ModuleClassLoader的ProtectionDomain
     * 这样写好蛋疼，而且还有不兼容的风险，从JDK6+都必须要这样清理，但我找不出更好的办法。
     * 在重置仿真器时，遇到MgrModule模块无法正确卸载类的情况，主要的原因是在于URLClassLoader.acc.ProtectionDomain[]中包含了上一个ModuleClassLoader的引用
     * 所以必须要在这里清理掉，否则随着重置次数的增加，类会越累积越多
     */
    private void cleanProtectionDomainWhichCameFromModuleJarClassLoader() {

        // got ProtectionDomain[] from URLClassLoader's acc
        final AccessControlContext acc = ReflectUtils.getDeclaredJavaFieldValueUnCaught(URLClassLoader.class, "acc", this);
        final ProtectionDomain[] protectionDomainArray = ReflectUtils.invokeMethodUnCaught(
                ReflectUtils.getDeclaredJavaMethodUnCaught(AccessControlContext.class, "getContext"),
                acc
        );

        // remove ProtectionDomain which loader is ModuleJarClassLoader
        final Set<ProtectionDomain> cleanProtectionDomainSet = new LinkedHashSet<ProtectionDomain>();
        if (ArrayUtils.isNotEmpty(protectionDomainArray)) {
            for (final ProtectionDomain protectionDomain : protectionDomainArray) {
                if (protectionDomain.getClassLoader() == null
                        || !StringUtils.equals(ModuleClassLoader.class.getName(), protectionDomain.getClassLoader().getClass().getName())) {
                    cleanProtectionDomainSet.add(protectionDomain);
                }
            }
        }

        // rewrite acc
        final AccessControlContext newAcc = new AccessControlContext(cleanProtectionDomainSet.toArray(new ProtectionDomain[]{}));
        ReflectUtils.setDeclaredJavaFieldValueUnCaught(URLClassLoader.class, "acc", this, newAcc);

    }

    private void onJarUnLoadCompleted() {
        try {
            final ServiceLoader<ModuleJarUnLoadSpi> moduleJarUnLoadSpiServiceLoader
                    = ServiceLoader.load(ModuleJarUnLoadSpi.class, this);
            for (final ModuleJarUnLoadSpi moduleJarUnLoadSpi : moduleJarUnLoadSpiServiceLoader) {
                if (logger.isInfoEnabled()) {
                    logger.info("SIMULATOR: unloading module-jar: onJarUnLoadCompleted() loader={};moduleJarUnLoadSpi={};",
                            this,
                            getJavaClassName(moduleJarUnLoadSpi.getClass())
                    );
                }
                moduleJarUnLoadSpi.onJarUnLoadCompleted();
            }
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: unloading module-jar: onJarUnLoadCompleted() occur error! loader={};", this, cause);
        }
    }

    public void closeIfPossible() {
        onJarUnLoadCompleted();
        try {

            // 如果是JDK7+的版本, URLClassLoader实现了Closeable接口，直接调用即可
            if (this instanceof Closeable) {
                if (logger.isDebugEnabled()) {
                    logger.debug("SIMULATOR: JDK is 1.7+, use URLClassLoader[file={}].close()", moduleJarFile);
                }
                try {
                    ((Closeable) this).close();
                } catch (Throwable cause) {
                    logger.warn("SIMULATOR: close ModuleJarClassLoader[file={}] failed. JDK7+", moduleJarFile, cause);
                }
                return;
            }


            // 对于JDK6的版本，URLClassLoader要关闭起来就显得有点麻烦，这里弄了一大段代码来稍微处理下
            // 而且还不能保证一定释放干净了，至少释放JAR文件句柄是没有什么问题了
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("SIMULATOR: JDK is less then 1.7+, use File.release()");
                }
                final Object ucp = ReflectUtils.getDeclaredJavaFieldValueUnCaught(URLClassLoader.class, "ucp", this);
                final Object loaders = ReflectUtils.getDeclaredJavaFieldValueUnCaught(ucp.getClass(), "loaders", ucp);

                for (Object loader :
                        ((Collection) loaders).toArray()) {
                    try {
                        final JarFile jarFile = ReflectUtils.getDeclaredJavaFieldValueUnCaught(
                                loader.getClass(),
                                "jar",
                                loader
                        );
                        jarFile.close();
                    } catch (Throwable t) {
                        // if we got this far, this is probably not a JAR loader so skip it
                    }
                }

            } catch (Throwable cause) {
                logger.warn("SIMULATOR: close ModuleJarClassLoader[file={}] failed. probably not a HOTSPOT VM", moduleJarFile, cause);
            }

        } finally {

            // 在这里删除掉临时文件
            FileUtils.deleteQuietly(tempModuleJarFile);

        }

    }

    @Override
    protected Class<?> loadClassInternal(String name, boolean resolve) throws ClassNotFoundException {
        if (classLoaderService.isSunReflectClass(name)) {
            throw new ModuleLoaderException(
                    String
                            .format(
                                    "[ModuleClassLoader] %s : can not load class: %s, this class can only be loaded by sun.reflect.DelegatingClassLoader",
                                    moduleId, name));
        }
        return super.loadClassInternal(name, resolve);
    }

    public File getModuleJarFile() {
        return moduleJarFile;
    }

    @Override
    public String toString() {
        return String.format("ModuleClassLoader[crc32=%s;file=%s;]", checksumCRC32, moduleJarFile);
    }

    public long getChecksumCRC32() {
        return checksumCRC32;
    }

    @Override
    boolean shouldFindExportedClass(String className) {
        return classLoaderService.isClassInImport(moduleId, className);
    }

    @Override
    boolean shouldFindExportedResource(String resourceName) {
        return classLoaderService.isResourceInImport(moduleId, resourceName);
    }
}
