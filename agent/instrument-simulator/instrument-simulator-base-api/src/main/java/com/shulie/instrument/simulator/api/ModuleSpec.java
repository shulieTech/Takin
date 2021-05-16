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
package com.shulie.instrument.simulator.api;

import com.shulie.instrument.simulator.api.utils.ParseUtils;

import java.io.File;
import java.util.*;

/**
 * 模块描述，描述模块的依赖关系与启动顺序
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/19 10:04 下午
 */
public class ModuleSpec {
    /**
     * 模块id
     */
    private String moduleId;

    /**
     * 是否是系统模块
     */
    private boolean isSystemModule;

    // 支持的版本号区间
    /**
     * 开始支持的版本号
     */
    private String sinceVersion;

    /**
     * 结束支持的版本号
     */
    private String untilVersion;

    /**
     * 是否是必须要启用的
     */
    private boolean mustUse;

    /**
     * 支持的启动模式
     */
    private LoadMode[] supportedModes;

    /**
     * 是否启动时就激活
     *
     * @return
     */
    private boolean isActiveOnLoad;

    /**
     * 模块描述
     */
    private String description;

    /**
     * 模块版本号
     */
    private String version;

    /**
     * 模块作者
     */
    private String author;

    /**
     * 模块优先级, 优先级默认设置成100
     */
    private int priority = 100;

    /**
     * 对应的模块
     */
    private ExtensionModule module;

    /**
     * module class
     */
    private Class classOfModule;

    /**
     * 模块的依赖列表，可以直接依赖模块，也可以依赖某个自定义开关
     */
    private Set<String> dependencies = Collections.EMPTY_SET;

    /**
     * 所有导出的包名
     */
    private Set<String> exportPackages = Collections.EMPTY_SET;

    /**
     * 所有导出的包名，精确匹配
     */
    private Set<String> exportExactlyPackages = new HashSet<String>();

    /**
     * 所有导出的包名开头
     */
    private Set<String> exportPrefixPackages = new HashSet<String>();

    /**
     * 所有导出的包名结束
     */
    private Set<String> exportSuffixPackages = new HashSet<String>();

    /**
     * 所有导入的包名
     */
    private Set<String> importPackages = Collections.EMPTY_SET;

    /**
     * 所有导入的包名，精确匹配
     */
    private Set<String> importExactlyPackages = new HashSet<String>();

    /**
     * 所有导入的包名开关
     */
    private Set<String> importPrefixPackages = new HashSet<String>();

    /**
     * 所有导入的包名结尾
     */
    private Set<String> importSuffixPackages = new HashSet<String>();

    /**
     * 所有导出的类名
     */
    private Set<String> exportClasses = Collections.EMPTY_SET;

    /**
     * 所有导入的类名
     */
    private Set<String> importClasses = Collections.EMPTY_SET;

    /**
     * 所有导出的资源
     */
    private Set<String> exportResources = Collections.EMPTY_SET;

    /**
     * 所有导出的资源，精确匹配
     */
    private Set<String> exportExactlyResources = new HashSet<String>();

    /**
     * 所有导出的资源开头
     */
    private Set<String> exportPrefixResources = new HashSet<String>();

    /**
     * 所有导出的资源结尾
     */
    private Set<String> exportSuffixResources = new HashSet<String>();

    /**
     * 所有导入的资源
     */
    private Set<String> importResources = Collections.EMPTY_SET;


    /**
     * 所有导入的资源，精确匹配
     */
    private Set<String> importExactlyResources = new HashSet<String>();

    /**
     * 导入的资源, 以xxx开头
     */
    private Set<String> importPrefixResources = new HashSet<String>();

    /**
     * 导入的资源, 以xxx结尾
     */
    private Set<String> importSuffixResources = new HashSet<String>();

    /**
     * 模块 jar 文件
     */
    private File file;

    /**
     * 是否已经加载过了
     */
    private boolean isLoaded;

    /**
     * 是否有效
     */
    private boolean isValid = true;

    /**
     * 是否是中间件扩展模块,默认是
     * 如果非中间件扩展模块，则忽略应用是否是多类加载器加载，该模块的类加载器只会有一个
     */
    private boolean isMiddlewareModule = true;

    public void loadModuleInfo(ModuleInfo moduleInfo) {
        if (moduleInfo == null) {
            return;
        }
        this.moduleId = moduleInfo.id();
        this.isActiveOnLoad = moduleInfo.isActiveOnLoad();
        this.supportedModes = moduleInfo.supportedModes();
        this.version = moduleInfo.version();
        this.author = moduleInfo.author();
        this.priority = moduleInfo.priority();
        this.description = moduleInfo.description();
    }

    public LoadMode[] getSupportedModes() {
        return supportedModes;
    }

    public void setSupportedModes(LoadMode[] supportedModes) {
        this.supportedModes = supportedModes;
    }

    public boolean isActiveOnLoad() {
        return isActiveOnLoad;
    }

    public void setActiveOnLoad(boolean activeOnLoad) {
        isActiveOnLoad = activeOnLoad;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ExtensionModule getModule() {
        return module;
    }

    public void setModule(ExtensionModule module) {
        this.module = module;
    }

    public Class getClassOfModule() {
        return classOfModule;
    }

    public void setClassOfModule(Class classOfModule) {
        this.classOfModule = classOfModule;
    }

    public boolean isMustUse() {
        return mustUse;
    }

    public ModuleSpec setMustUse(boolean mustUse) {
        this.mustUse = mustUse;
        return this;
    }

    public ModuleSpec setMiddlewareModule(boolean isMiddlewareModule) {
        this.isMiddlewareModule = isMiddlewareModule;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMiddlewareModule() {
        return isMiddlewareModule;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<String> dependencies) {
        this.dependencies = dependencies;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSystemModule() {
        return isSystemModule;
    }

    public void setSystemModule(boolean systemModule) {
        isSystemModule = systemModule;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getModuleId() {
        return moduleId;
    }

    private static Set<String> strToSet(String str, String delimiter) {
        return new LinkedHashSet<String>(strToList(str, delimiter));
    }

    private static List<String> strToList(String str, String delimiter) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> stringList = new ArrayList<String>();
        for (String s : str.split(delimiter)) {
            stringList.add(s.trim());
        }
        return stringList;
    }

    public Set<String> getExportPackages() {
        return exportPackages;
    }

    public ModuleSpec setExportPackages(String exportPackages) {
        this.exportPackages = strToSet(exportPackages, ",");
        ParseUtils.parsePackagePrefixAndSuffix(this.exportPackages, this.exportPrefixPackages,
                this.exportSuffixPackages, this.exportExactlyPackages);
        return this;
    }

    public Set<String> getExportPrefixPackages() {
        return exportPrefixPackages;
    }

    public Set<String> getExportSuffixPackages() {
        return exportSuffixPackages;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public ModuleSpec setImportPackages(String importPackages) {
        this.importPackages = strToSet(importPackages, ",");
        ParseUtils.parsePackagePrefixAndSuffix(this.importPackages, this.importPrefixPackages,
                this.importSuffixPackages, this.importExactlyPackages);
        return this;
    }

    public ModuleSpec setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }

    public ModuleSpec setSinceVersion(String sinceVersion) {
        this.sinceVersion = sinceVersion;
        return this;
    }

    public String getUntilVersion() {
        return untilVersion;
    }

    public ModuleSpec setUntilVersion(String untilVersion) {
        this.untilVersion = untilVersion;
        return this;
    }

    public Set<String> getImportPrefixPackages() {
        return importPrefixPackages;
    }

    public Set<String> getImportSuffixPackages() {
        return importSuffixPackages;
    }

    public Set<String> getExportClasses() {
        return exportClasses;
    }

    public ModuleSpec setExportClasses(Set<String> exportClasses) {
        this.exportClasses = exportClasses;
        return this;
    }

    public Set<String> getImportClasses() {
        return importClasses;
    }

    public ModuleSpec setImportClasses(Set<String> importClasses) {
        this.importClasses = importClasses;
        return this;
    }

    public Set<String> getExportResources() {
        return exportResources;
    }

    public ModuleSpec setExportResources(String exportResources) {
        this.exportResources = strToSet(exportResources, ",");
        ParseUtils.parsePackagePrefixAndSuffix(this.exportResources, this.exportPrefixResources,
                this.exportSuffixResources, this.exportExactlyResources);
        return this;
    }

    public Set<String> getExportPrefixResources() {
        return exportPrefixResources;
    }

    public Set<String> getExportSuffixResources() {
        return exportSuffixResources;
    }

    public Set<String> getImportResources() {
        return importResources;
    }

    public ModuleSpec setImportResources(String importResource) {
        this.importResources = strToSet(importResource, ",");
        ParseUtils.parsePackagePrefixAndSuffix(this.importResources, this.importPrefixResources,
                this.importSuffixResources, this.importExactlyResources);
        return this;
    }

    public Set<String> getImportPrefixResources() {
        return importPrefixResources;
    }


    public Set<String> getImportSuffixResources() {
        return importSuffixResources;
    }

    public File getFile() {
        return file;
    }

    public ModuleSpec setFile(File file) {
        this.file = file;
        return this;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public ModuleSpec setLoaded(boolean loaded) {
        isLoaded = loaded;
        return this;
    }

    public Set<String> getExportExactlyPackages() {
        return exportExactlyPackages;
    }

    public Set<String> getImportExactlyPackages() {
        return importExactlyPackages;
    }

    public Set<String> getExportExactlyResources() {
        return exportExactlyResources;
    }

    public Set<String> getImportExactlyResources() {
        return importExactlyResources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModuleSpec spec = (ModuleSpec) o;

        return moduleId != null ? moduleId.equals(spec.moduleId) : spec.moduleId == null;
    }

    @Override
    public int hashCode() {
        return moduleId != null ? moduleId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ModuleSpec{" +
                "moduleId='" + moduleId + '\'' +
                ", isSystemModule=" + isSystemModule +
                ", switchControls=" + dependencies +
                ", exportPackages=" + exportPackages +
                ", exportExactlyPackages=" + exportExactlyPackages +
                ", exportPrefixPackages=" + exportPrefixPackages +
                ", exportSuffixPackages=" + exportSuffixPackages +
                ", importPackages=" + importPackages +
                ", importExactlyPackages=" + importExactlyPackages +
                ", importPrefixPackages=" + importPrefixPackages +
                ", importSuffixPackages=" + importSuffixPackages +
                ", exportClasses=" + exportClasses +
                ", importClasses=" + importClasses +
                ", exportResources=" + exportResources +
                ", exportExactlyResources=" + exportExactlyResources +
                ", exportPrefixResources=" + exportPrefixResources +
                ", exportSuffixResources=" + exportSuffixResources +
                ", importResources=" + importResources +
                ", importExactlyResources=" + importExactlyResources +
                ", importPrefixResources=" + importPrefixResources +
                ", importSuffixResources=" + importSuffixResources +
                ", file=" + file +
                ", isLoaded=" + isLoaded +
                ", isValid=" + isValid +
                ", priority=" + priority +
                '}';
    }
}
