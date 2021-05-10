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

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 11:05 上午
 */
public class ModuleExtInf implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 模块扩展唯一ID
     */
    private String uniqueId;

    /**
     * 支持的启动模式
     */
    private List<String> supportedModes;

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
     * 模块优先级
     */
    private int priority;

    /**
     * 是否激活状态
     */
    private boolean isActivated;

    /**
     * 是否已经加载
     */
    private boolean isLoaded;

    /**
     * 影响类的个数
     */
    private int effectClassCount;

    /**
     * 影响方法个数
     */
    private int effectMethodCount;

    /**
     * module class 名称
     */
    private String moduleClass;

    /**
     * module class loader
     */
    private String moduleClassLoader;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<String> getSupportedModes() {
        return supportedModes;
    }

    public void setSupportedModes(List<String> supportedModes) {
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

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getEffectClassCount() {
        return effectClassCount;
    }

    public void setEffectClassCount(int effectClassCount) {
        this.effectClassCount = effectClassCount;
    }

    public int getEffectMethodCount() {
        return effectMethodCount;
    }

    public void setEffectMethodCount(int effectMethodCount) {
        this.effectMethodCount = effectMethodCount;
    }

    public String getModuleClass() {
        return moduleClass;
    }

    public void setModuleClass(String moduleClass) {
        this.moduleClass = moduleClass;
    }

    public String getModuleClassLoader() {
        return moduleClassLoader;
    }

    public void setModuleClassLoader(String moduleClassLoader) {
        this.moduleClassLoader = moduleClassLoader;
    }

    @Override
    public String toString() {
        return "ModuleExtInf{" +
                "uniqueId='" + uniqueId + '\'' +
                ", supportedModes=" + supportedModes +
                ", isActiveOnLoad=" + isActiveOnLoad +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", priority=" + priority +
                ", isActivated=" + isActivated +
                ", isLoaded=" + isLoaded +
                ", effectClassCount=" + effectClassCount +
                ", effectMethodCount=" + effectMethodCount +
                ", moduleClass='" + moduleClass + '\'' +
                ", moduleClassLoader='" + moduleClassLoader + '\'' +
                '}';
    }
}
