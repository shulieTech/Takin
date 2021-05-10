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
package com.shulie.instrument.simulator.module.jad;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.filter.MultiClassNameFilter;
import com.shulie.instrument.simulator.api.filter.NameRegexFilter;
import com.shulie.instrument.simulator.api.resource.*;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.jad.JadInfo;
import com.shulie.instrument.simulator.module.util.ClassUtils;
import com.shulie.instrument.simulator.module.util.Decompiler;
import org.apache.commons.collections.CollectionUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 反编译模块扩展，可以通过指定一个匹配规则来反编译指定的类
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "jad", version = "1.0.0", author = "xiaobin@shulie.io", description = "反编译模块")
public class JadModule extends ParamSupported implements ExtensionModule {

    private final Logger logger = LoggerFactory.getLogger("DEBUG-JAD-LOGGER");
    private static Pattern pattern = Pattern.compile("(?m)^/\\*\\s*\\*/\\s*$" + System.getProperty("line.separator"));

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    @Resource
    private SimulatorConfig simulatorConfig;

    private CommandResponse processNoMatch(String classPattern) {
        return CommandResponse.failure("No class found for: " + classPattern);
    }

    private CommandResponse processMatches(Set<Class<?>> matchedClasses, String classPattern) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n Found more than one class for: " + classPattern + ", Please use jad's params: classloader<hashcode> " + classPattern);
        builder.append("\n");
        for (Class<?> c : matchedClasses) {
            ClassLoader classLoader = c.getClassLoader();
            builder.append("HASHCODE: ");
            builder.append(Integer.toHexString(classLoader.hashCode()));
            builder.append("\n");
            builder.append("CLASSLOADER: ");
            builder.append(drawClassLoader(c));
            builder.append("\n");
        }
        return CommandResponse.failure(builder.toString());
    }

    private static Set<Class<?>> filter(Set<Class<?>> matchedClasses, String code) {
        if (code == null) {
            return matchedClasses;
        }

        Set<Class<?>> result = new HashSet<Class<?>>();
        if (matchedClasses != null) {
            for (Class<?> c : matchedClasses) {
                if (Integer.toHexString(c.getClassLoader().hashCode()).equals(code)) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    private Set<String> toClassName(Set<Class<?>> sets) {
        if (CollectionUtils.isEmpty(sets)) {
            return Collections.EMPTY_SET;
        }
        Set<String> classNames = new HashSet<String>();
        for (Class<?> clazz : sets) {
            if (clazz == null) {
                continue;
            }
            classNames.add(clazz.getName());
        }
        return classNames;
    }

    private CommandResponse processExactMatch(Set<Class<?>> matchedClasses, Set<Class<?>> withInnerClasses, boolean sourceOnly) {
        Class<?> c = matchedClasses.iterator().next();
        Set<Class<?>> allClasses = new HashSet<Class<?>>(withInnerClasses);
        allClasses.add(c);
        final Set<String> classNames = toClassName(allClasses);
        Filter filter = new MultiClassNameFilter(classNames, true);

        DumpResult dumpResult = null;
        try {
            dumpResult = moduleEventWatcher.
                    dump(filter, new NoActionProgress());
            File classFile = dumpResult.getDumpResult().get(c.getName());
            String source = Decompiler.decompile(classFile.getAbsolutePath(), simulatorConfig.getSimulatorVersion());
            if (source != null) {
                source = pattern.matcher(source).replaceAll("");
            } else {
                return CommandResponse.failure("can't decompile class file.");
            }

            if (sourceOnly) {
                JadInfo jadInfo = new JadInfo();
                jadInfo.setSource(source);
                return CommandResponse.success(jadInfo);
            }

            JadInfo jadInfo = new JadInfo();
            jadInfo.setClassloader(drawClassLoader(c));
            jadInfo.setLocation(ClassUtils.getCodeSource(
                    c.getProtectionDomain().getCodeSource()));
            jadInfo.setSource(source);
            return CommandResponse.success(jadInfo);
        } catch (Throwable t) {
            logger.error("SIMULATOR: jad: fail to decompile class: " + c.getName(), t);
            return CommandResponse.failure(t);
        } finally {
            if (dumpResult != null) {
                //注销watchId
                moduleEventWatcher.delete(dumpResult.getWatchId());
            }
        }
    }

    private String drawClassLoader(Class clazz) {
        StringBuilder builder = new StringBuilder();
        if (clazz == null || clazz.getClassLoader() == null) {
            return builder.toString();
        }
        ClassLoader loader = clazz.getClassLoader();
        for (int i = 0; ; i++) {
            if (loader == null) {
                break;
            }
            for (int j = 0; j < i; j++) {
                builder.append("\t");
            }
            builder.append(loader.toString()).append("\n * ");
            loader = loader.getParent();
        }
        return builder.toString();
    }

    @Command(value = "jad", description = "反编译")
    public CommandResponse jad(final Map<String, String> param) {

        // --- 解析参数 ---

        try {
            final String cnPattern = getParameter(param, "class");
            final String classloader = getParameter(param, "classloader");
            final boolean sourceOnly = getBooleanParameter(param, "source-only", false);
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: param.class={}", cnPattern);
                logger.info("SIMULATOR: param.classloader={}", classloader);
                logger.info("SIMULATOR: param.source-only={}", sourceOnly);
            }

            Set<Class<?>> matchedClasses = loadedClassDataSource.find(new NameRegexFilter(cnPattern, ".*", true, true));
            if (matchedClasses == null || matchedClasses.isEmpty()) {
                return processNoMatch(cnPattern);
            } else if (matchedClasses.size() > 1) {
                return processMatches(matchedClasses, cnPattern);
            } else { // matchedClasses size is 1
                // find inner classes.
                Set<Class<?>> withInnerClasses = loadedClassDataSource.find(new NameRegexFilter(matchedClasses.iterator().next().getName() + "$*", "*", true, true));
                withInnerClasses = filter(withInnerClasses, classloader);
                if (withInnerClasses.isEmpty()) {
                    withInnerClasses = matchedClasses;
                }
                return processExactMatch(matchedClasses, withInnerClasses, sourceOnly);
            }
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }

    }
}
