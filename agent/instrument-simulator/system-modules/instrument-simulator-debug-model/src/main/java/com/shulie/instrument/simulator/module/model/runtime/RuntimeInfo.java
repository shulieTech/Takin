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
package com.shulie.instrument.simulator.module.model.runtime;


import java.io.Serializable;
import java.util.List;

public class RuntimeInfo implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 机器名
     */
    private String name;
    /**
     * jvm 启动时间
     */
    private long startTime;

    /**
     * MANAGEMENT-SPEC-VERSION
     */
    private String managementSpecVersion;

    /**
     * SPEC-NAME
     */
    private String specName;

    /**
     * SPEC-VENDOR
     */
    private String specVendor;

    /**
     * SPEC-VERSION
     */
    private String specVersion;

    /**
     * 虚拟机名称
     */
    private String vmName;

    /**
     * 虚拟机厂商
     */
    private String vmVendor;

    /**
     * 虚拟机版本号
     */
    private String vmVersion;

    /**
     * 启动入参
     */
    private List<String> inputArguments;

    /**
     * classpath
     */
    private String classPath;

    /**
     * boot classpath
     */
    private String bootClassPath;

    /**
     * lib path
     */
    private String libraryPath;
    /**
     * 架构名称
     */
    private String arch;
    /**
     * 操作系统名称
     */
    private String osName;
    /**
     * 操作系统版本号
     */
    private String osVersion;
    /**
     * java 版本号
     */
    private String javaVersion;
    /**
     * java home
     */
    private String javaHome;
    /**
     * 系统平均负载
     */
    private double systemLoadAverage;
    /**
     * 可用核数
     */
    private int processors;
    /**
     * 上次更新时间
     */
    private long uptime;

    /**
     * 当前 vm 中已加载的类个数
     */
    private int loadedClassCount;

    /**
     * vm 总共加载的类个数
     */
    private long totalLoadedClassCount;

    /**
     * vm 卸载的类个数
     */
    private long unloadedClassCount;

    /**
     * isVerbose
     */
    private boolean isVerbose;
    /**
     * 编译器名称
     */
    private String compilationName;

    /**
     * 总共编译耗时
     */
    private long totalCompilationTime;

    public RuntimeInfo() {
    }

    public int getLoadedClassCount() {
        return loadedClassCount;
    }

    public void setLoadedClassCount(int loadedClassCount) {
        this.loadedClassCount = loadedClassCount;
    }

    public long getTotalLoadedClassCount() {
        return totalLoadedClassCount;
    }

    public void setTotalLoadedClassCount(long totalLoadedClassCount) {
        this.totalLoadedClassCount = totalLoadedClassCount;
    }

    public long getUnloadedClassCount() {
        return unloadedClassCount;
    }

    public void setUnloadedClassCount(long unloadedClassCount) {
        this.unloadedClassCount = unloadedClassCount;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public String getCompilationName() {
        return compilationName;
    }

    public void setCompilationName(String compilationName) {
        this.compilationName = compilationName;
    }

    public long getTotalCompilationTime() {
        return totalCompilationTime;
    }

    public void setTotalCompilationTime(long totalCompilationTime) {
        this.totalCompilationTime = totalCompilationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getManagementSpecVersion() {
        return managementSpecVersion;
    }

    public void setManagementSpecVersion(String managementSpecVersion) {
        this.managementSpecVersion = managementSpecVersion;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecVendor() {
        return specVendor;
    }

    public void setSpecVendor(String specVendor) {
        this.specVendor = specVendor;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public void setVmVendor(String vmVendor) {
        this.vmVendor = vmVendor;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
    }

    public List<String> getInputArguments() {
        return inputArguments;
    }

    public void setInputArguments(List<String> inputArguments) {
        this.inputArguments = inputArguments;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getBootClassPath() {
        return bootClassPath;
    }

    public void setBootClassPath(String bootClassPath) {
        this.bootClassPath = bootClassPath;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public int getProcessors() {
        return processors;
    }

    public void setProcessors(int processors) {
        this.processors = processors;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", managementSpecVersion='" + managementSpecVersion + '\'' +
                ", specName='" + specName + '\'' +
                ", specVendor='" + specVendor + '\'' +
                ", specVersion='" + specVersion + '\'' +
                ", vmName='" + vmName + '\'' +
                ", vmVendor='" + vmVendor + '\'' +
                ", vmVersion='" + vmVersion + '\'' +
                ", inputArguments=" + inputArguments +
                ", classPath='" + classPath + '\'' +
                ", bootClassPath='" + bootClassPath + '\'' +
                ", libraryPath='" + libraryPath + '\'' +
                ", arch='" + arch + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", javaVersion='" + javaVersion + '\'' +
                ", javaHome='" + javaHome + '\'' +
                ", systemLoadAverage=" + systemLoadAverage +
                ", processors=" + processors +
                ", uptime=" + uptime +
                ", loadedClassCount=" + loadedClassCount +
                ", totalLoadedClassCount=" + totalLoadedClassCount +
                ", unloadedClassCount=" + unloadedClassCount +
                ", isVerbose=" + isVerbose +
                ", compilationName='" + compilationName + '\'' +
                ", totalCompilationTime=" + totalCompilationTime +
                '}';
    }
}
