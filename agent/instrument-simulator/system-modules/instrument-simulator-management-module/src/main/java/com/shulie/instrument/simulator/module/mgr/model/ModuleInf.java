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
package com.shulie.instrument.simulator.module.mgr.model;

import com.shulie.instrument.simulator.api.LoadMode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 10:25 上午
 */
public class ModuleInf implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 模块id
     */
    private String moduleId;

    /**
     * 是否是系统模块
     */
    private boolean isSystemModule;

    /**
     * 加载的 模块 jar 文件路径
     */
    private String path;

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
     * 模块描述
     */
    private String description;

    /**
     * 所有导出的包名
     */
    private Set<String> exportPackages;

    /**
     * 所有导出的包名，精确匹配
     */
    private Set<String> exportExactlyPackages;

    /**
     * 所有导出的包名开头
     */
    private Set<String> exportPrefixPackages;

    /**
     * 所有导出的包名结束
     */
    private Set<String> exportSuffixPackages;

    /**
     * 所有导入的包名
     */
    private Set<String> importPackages;

    /**
     * 所有导入的包名，精确匹配
     */
    private Set<String> importExactlyPackages;

    /**
     * 所有导入的包名开关
     */
    private Set<String> importPrefixPackages;

    /**
     * 所有导入的包名结尾
     */
    private Set<String> importSuffixPackages;

    /**
     * 所有导出的类名
     */
    private Set<String> exportClasses;

    /**
     * 所有导入的类名
     */
    private Set<String> importClasses;

    /**
     * 所有导出的资源
     */
    private Set<String> exportResources;

    /**
     * 所有导出的资源，精确匹配
     */
    private Set<String> exportExactlyResources;

    /**
     * 所有导出的资源开头
     */
    private Set<String> exportPrefixResources;

    /**
     * 所有导出的资源结尾
     */
    private Set<String> exportSuffixResources;

    /**
     * 所有导入的资源
     */
    private Set<String> importResources;


    /**
     * 所有导入的资源，精确匹配
     */
    private Set<String> importExactlyResources;

    /**
     * 导入的资源, 以xxx开头
     */
    private Set<String> importPrefixResources;

    /**
     * 导入的资源, 以xxx结尾
     */
    private Set<String> importSuffixResources;

    /**
     * 模块依赖控制
     */
    private  Set<String> switchControl;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<String> getSwitchControl() {
        return switchControl;
    }

    public void setSwitchControl(Set<String> switchControl) {
        this.switchControl = switchControl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSystemModule() {
        return isSystemModule;
    }

    public void setSystemModule(boolean systemModule) {
        isSystemModule = systemModule;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Set<String> getExportPackages() {
        return exportPackages;
    }

    public void setExportPackages(Set<String> exportPackages) {
        this.exportPackages = exportPackages;
    }

    public Set<String> getExportExactlyPackages() {
        return exportExactlyPackages;
    }

    public void setExportExactlyPackages(Set<String> exportExactlyPackages) {
        this.exportExactlyPackages = exportExactlyPackages;
    }

    public Set<String> getExportPrefixPackages() {
        return exportPrefixPackages;
    }

    public void setExportPrefixPackages(Set<String> exportPrefixPackages) {
        this.exportPrefixPackages = exportPrefixPackages;
    }

    public Set<String> getExportSuffixPackages() {
        return exportSuffixPackages;
    }

    public void setExportSuffixPackages(Set<String> exportSuffixPackages) {
        this.exportSuffixPackages = exportSuffixPackages;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public void setImportPackages(Set<String> importPackages) {
        this.importPackages = importPackages;
    }

    public Set<String> getImportExactlyPackages() {
        return importExactlyPackages;
    }

    public void setImportExactlyPackages(Set<String> importExactlyPackages) {
        this.importExactlyPackages = importExactlyPackages;
    }

    public Set<String> getImportPrefixPackages() {
        return importPrefixPackages;
    }

    public void setImportPrefixPackages(Set<String> importPrefixPackages) {
        this.importPrefixPackages = importPrefixPackages;
    }

    public Set<String> getImportSuffixPackages() {
        return importSuffixPackages;
    }

    public void setImportSuffixPackages(Set<String> importSuffixPackages) {
        this.importSuffixPackages = importSuffixPackages;
    }

    public Set<String> getExportClasses() {
        return exportClasses;
    }

    public void setExportClasses(Set<String> exportClasses) {
        this.exportClasses = exportClasses;
    }

    public Set<String> getImportClasses() {
        return importClasses;
    }

    public void setImportClasses(Set<String> importClasses) {
        this.importClasses = importClasses;
    }

    public Set<String> getExportResources() {
        return exportResources;
    }

    public void setExportResources(Set<String> exportResources) {
        this.exportResources = exportResources;
    }

    public Set<String> getExportExactlyResources() {
        return exportExactlyResources;
    }

    public void setExportExactlyResources(Set<String> exportExactlyResources) {
        this.exportExactlyResources = exportExactlyResources;
    }

    public Set<String> getExportPrefixResources() {
        return exportPrefixResources;
    }

    public void setExportPrefixResources(Set<String> exportPrefixResources) {
        this.exportPrefixResources = exportPrefixResources;
    }

    public Set<String> getExportSuffixResources() {
        return exportSuffixResources;
    }

    public void setExportSuffixResources(Set<String> exportSuffixResources) {
        this.exportSuffixResources = exportSuffixResources;
    }

    public Set<String> getImportResources() {
        return importResources;
    }

    public void setImportResources(Set<String> importResources) {
        this.importResources = importResources;
    }

    public Set<String> getImportExactlyResources() {
        return importExactlyResources;
    }

    public void setImportExactlyResources(Set<String> importExactlyResources) {
        this.importExactlyResources = importExactlyResources;
    }

    public Set<String> getImportPrefixResources() {
        return importPrefixResources;
    }

    public void setImportPrefixResources(Set<String> importPrefixResources) {
        this.importPrefixResources = importPrefixResources;
    }

    public Set<String> getImportSuffixResources() {
        return importSuffixResources;
    }

    public void setImportSuffixResources(Set<String> importSuffixResources) {
        this.importSuffixResources = importSuffixResources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ModuleInf{" +
                "moduleId='" + moduleId + '\'' +
                ", isSystemModule=" + isSystemModule +
                ", path='" + path + '\'' +
                ", supportedModes=" + Arrays.toString(supportedModes) +
                ", isActiveOnLoad=" + isActiveOnLoad +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", priority=" + priority +
                ", description=" + description +
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
                ", switchControl=" + switchControl +
                '}';
    }
}
