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
package com.shulie.instrument.simulator.module.mgr;

import com.shulie.instrument.simulator.api.*;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.ModuleManager;
import com.shulie.instrument.simulator.module.mgr.model.ModuleInf;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.shulie.instrument.simulator.api.util.StringUtil.matching;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * 仿真器模块管理模块
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "management", author = "xiaobin@shulie.io", version = "1.0.0", description = "模块管理模块")
public class ModuleManagementModule implements ExtensionModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ModuleManager moduleManager;

    // 获取参数值
    private String getParamWithDefault(final Map<String, String> param, final String name, final String defaultValue) {
        final String valueFromReq = param.get(name);
        return StringUtils.isBlank(valueFromReq)
                ? defaultValue
                : valueFromReq;
    }

    private Collection<ModuleSpec> searchByModuleId(final String moduleId) {
        final Collection<ModuleSpec> foundModules = new ArrayList<ModuleSpec>();
        for (ModuleSpec moduleSpec : moduleManager.listModuleSpecs()) {
            if (!matching(moduleSpec.getModuleId(), moduleId)) {
                continue;
            }
            foundModules.add(moduleSpec);
        }
        return foundModules;
    }

    // 搜索模块
    private Collection<ExtensionModule> search(final String idsStringPattern) {
        final Collection<ExtensionModule> foundModules = new ArrayList<ExtensionModule>();
        for (ExtensionModule module : moduleManager.list()) {
            final ModuleInfo moduleInfo = module.getClass().getAnnotation(ModuleInfo.class);
            if (!matching(moduleInfo.id(), idsStringPattern)) {
                continue;
            }
            foundModules.add(module);
        }
        return foundModules;
    }

    @Command(value = "list", description = "模块列表")
    public CommandResponse list() throws IOException {
        try {
            List<ModuleInf> moduleInfoList = new ArrayList<ModuleInf>();
            for (final ModuleSpec moduleSpec : moduleManager.listModuleSpecs()) {
                ModuleInf moduleInf = new ModuleInf();
                moduleInf.setModuleId(moduleSpec.getModuleId());
                moduleInf.setSystemModule(moduleSpec.isSystemModule());
                moduleInf.setPath(moduleSpec.getFile() != null ? moduleSpec.getFile().getCanonicalPath() : null);
                moduleInf.setSwitchControl(moduleSpec.getSwitchControls());
                moduleInf.setExportClasses(moduleSpec.getExportClasses());
                moduleInf.setExportExactlyPackages(moduleSpec.getExportExactlyPackages());
                moduleInf.setExportExactlyResources(moduleSpec.getExportExactlyResources());
                moduleInf.setExportPackages(moduleSpec.getExportPackages());
                moduleInf.setExportPrefixPackages(moduleSpec.getExportPrefixPackages());
                moduleInf.setExportPrefixResources(moduleSpec.getExportPrefixResources());
                moduleInf.setExportResources(moduleSpec.getExportResources());
                moduleInf.setExportSuffixPackages(moduleSpec.getExportSuffixPackages());
                moduleInf.setExportSuffixResources(moduleSpec.getExportSuffixResources());
                moduleInf.setAuthor(moduleSpec.getAuthor());
                moduleInf.setVersion(moduleSpec.getVersion());
                moduleInf.setPriority(moduleSpec.getPriority());
                moduleInf.setSupportedModes(moduleSpec.getSupportedModes());
                moduleInf.setActiveOnLoad(moduleSpec.isActiveOnLoad());
                moduleInf.setDescription(moduleSpec.getDescription());
                moduleInfoList.add(moduleInf);
            }
            return CommandResponse.success(moduleInfoList);
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management list module err.", e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "flush", description = "刷新模块")
    public CommandResponse flush(final Map<String, String> param) throws ModuleException {
        try {
            final String isForceString = getParamWithDefault(param, "force", EMPTY);
            final boolean isForce = BooleanUtils.toBoolean(isForceString);
            moduleManager.flush(isForce);
            return CommandResponse.success(String.format("module flush finished, total=%s;", moduleManager.list().size()));
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management flush module err.", e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "reset", description = "模块重置")
    public CommandResponse reset() throws ModuleException {
        try {
            moduleManager.reset();
            return CommandResponse.success(String.format("module reset finished, total=%s;", moduleManager.list().size()));
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management reset module err.", e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "unload", description = "模块卸载")
    public CommandResponse unload(final Map<String, String> param) {
        final String uniqueId = getParamWithDefault(param, "uniqueId", EMPTY);
        final String moduleId = getParamWithDefault(param, "moduleId", EMPTY);
        try {
            int total = 0;
            int totalModule = 0;

            if (StringUtils.isNotBlank(uniqueId)) {
                for (final ExtensionModule module : search(uniqueId)) {
                    final ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
                    try {
                        moduleManager.unload(info.id());
                        total++;
                    } catch (ModuleException me) {
                        logger.warn("SIMULATOR: unload module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                    }
                }
                return CommandResponse.success(String.format("total %s module ext frozen.", total));
            } else if (StringUtils.isNotBlank(moduleId)) {
                for (final ModuleSpec moduleSpec : searchByModuleId(moduleId)) {
                    try {
                        moduleManager.unload(moduleSpec.getModuleId());
                        total++;
                    } catch (ModuleException me) {
                        logger.warn("SIMULATOR: unload module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                    }

                }
                return CommandResponse.success(String.format("total %s module unloaded. total %s module ext unloaded.", totalModule, total));
            }
            return CommandResponse.failure("uniqueId or moduleId arguments is required.");
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management unload err. uniqueId:{} moduleId:{}", uniqueId, moduleId, e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "reload", description = "模块重新加载")
    public CommandResponse reload(final Map<String, String> args) throws ModuleException {
        String moduleId = args.get("moduleId");
        int total = 0;
        for (final ModuleSpec moduleSpec : searchByModuleId(moduleId)) {
            try {
                moduleManager.unload(moduleSpec.getModuleId());
            } catch (ModuleException me) {
                logger.warn("SIMULATOR: reload to unload module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
            }
            try {
                moduleManager.load(new File(moduleSpec.getFile().getAbsolutePath()));
            } catch (ModuleException me) {
                logger.warn("SIMULATOR: reload to load module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
            }
        }

        return CommandResponse.success(String.format("total %s module reloaded.", total));
    }

    @Command(value = "load", description = "模块加载")
    public CommandResponse load(final Map<String, String> args) throws ModuleException {
        String path = args.get("path");
        File file = new File(path);
        if (!file.exists()) {
            return CommandResponse.failure("module file is not exits" + path);
        }
        try {
            moduleManager.load(file);
            return CommandResponse.success(true);
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management load module err. {}", path, e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "active", description = "模块激活")
    public CommandResponse active(final Map<String, String> param) throws ModuleException {
        final String uniqueId = getParamWithDefault(param, "uniqueId", EMPTY);
        final String moduleId = getParamWithDefault(param, "moduleId", EMPTY);
        try {
            int total = 0;
            int totalModule = 0;
            if (StringUtils.isNotBlank(uniqueId)) {
                for (final ExtensionModule module : search(uniqueId)) {
                    final ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
                    final boolean isActivated = moduleManager.isActivated(info.id());
                    if (!isActivated) {
                        try {
                            moduleManager.active(info.id());
                            total++;
                        } catch (ModuleException me) {
                            logger.warn("SIMULATOR: active module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                        }
                    } else {
                        total++;
                    }

                }
                return CommandResponse.success(String.format("total %s module ext frozen.", total));
            } else if (StringUtils.isNotBlank(moduleId)) {
                for (final ModuleSpec moduleSpec : searchByModuleId(moduleId)) {
                    try {
                        moduleManager.active(moduleSpec.getModuleId());
                        total++;
                    } catch (ModuleException me) {
                        logger.warn("SIMULATOR: active module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                    }

                }
                return CommandResponse.success(String.format("total %s module active. total %s module ext active.", totalModule, total));
            }
            return CommandResponse.failure("uniqueId or moduleId arguments is required.");
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management active err. uniqueId:{} moduleId:{}", uniqueId, moduleId, e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "frozen", description = "模块冻结")
    public CommandResponse frozen(final Map<String, String> param) throws ModuleException {
        final String uniqueId = getParamWithDefault(param, "uniqueId", EMPTY);
        final String moduleId = getParamWithDefault(param, "moduleId", EMPTY);
        try {
            int total = 0;
            int totalModule = 0;
            if (StringUtils.isNotBlank(uniqueId)) {
                for (final ExtensionModule module : search(uniqueId)) {
                    final ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
                    final boolean isActivated = moduleManager.isActivated(info.id());
                    if (isActivated) {
                        try {
                            moduleManager.frozen(info.id());
                            total++;
                        } catch (ModuleException me) {
                            logger.warn("SIMULATOR: frozen module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                        }
                    } else {
                        total++;
                    }

                }
                return CommandResponse.success(String.format("total %s module ext frozen.", total));
            } else if (StringUtils.isNotBlank(moduleId)) {
                for (final ModuleSpec moduleSpec : searchByModuleId(moduleId)) {
                    try {
                        moduleManager.frozen(moduleSpec.getModuleId());
                        total++;
                    } catch (ModuleException me) {
                        logger.warn("SIMULATOR: frozen module[id={};] occur error={}.", me.getUniqueId(), me.getErrorCode(), me);
                    }

                }
                return CommandResponse.success(String.format("total %s module frozen. total %s module ext frozen.", totalModule, total));
            }
            return CommandResponse.failure("uniqueId or moduleId arguments is required.");
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management frozen err. uniqueId:{} moduleId:{}", uniqueId, moduleId, e);
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "detail", description = "模块详情")
    public CommandResponse detail(final Map<String, String> param) throws ModuleException {
        final String moduleId = param.get("moduleId");
        try {
            if (StringUtils.isBlank(moduleId)) {
                // 如果参数不对，则认为找不到对应的仿真器模块，返回400
                return CommandResponse.failure("moduleId parameter was required.");
            }

            if (StringUtils.isNotBlank(moduleId)) {
                for (final ModuleSpec moduleSpec : moduleManager.listModuleSpecs()) {
                    if (!StringUtils.equals(moduleSpec.getModuleId(), moduleId)) {
                        continue;
                    }
                    ModuleInf moduleInf = new ModuleInf();
                    moduleInf.setModuleId(moduleSpec.getModuleId());
                    moduleInf.setSystemModule(moduleSpec.isSystemModule());
                    moduleInf.setExportClasses(moduleSpec.getExportClasses());
                    moduleInf.setExportExactlyPackages(moduleSpec.getExportExactlyPackages());
                    moduleInf.setExportExactlyResources(moduleSpec.getExportExactlyResources());
                    moduleInf.setExportPackages(moduleSpec.getExportPackages());
                    moduleInf.setExportPrefixPackages(moduleSpec.getExportPrefixPackages());
                    moduleInf.setExportPrefixResources(moduleSpec.getExportPrefixResources());
                    moduleInf.setExportResources(moduleSpec.getExportResources());
                    moduleInf.setExportSuffixPackages(moduleSpec.getExportSuffixPackages());
                    moduleInf.setExportSuffixResources(moduleSpec.getExportSuffixResources());
                    moduleInf.setAuthor(moduleSpec.getAuthor());
                    moduleInf.setVersion(moduleSpec.getVersion());
                    moduleInf.setPriority(moduleSpec.getPriority());
                    moduleInf.setSupportedModes(moduleSpec.getSupportedModes());
                    moduleInf.setActiveOnLoad(moduleSpec.isActiveOnLoad());
                    return CommandResponse.success(moduleId);
                }

            }
            return CommandResponse.failure("uniqueId or moduleId arguments is required.");
        } catch (Throwable e) {
            logger.error("SIMULATOR: module management detail err. moduleId:{}", moduleId, e);
            return CommandResponse.failure(e);
        }

    }

}
