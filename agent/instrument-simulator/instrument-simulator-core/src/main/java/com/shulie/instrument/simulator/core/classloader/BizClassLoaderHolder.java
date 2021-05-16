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

/**
 * 业务类加载器持有者
 * 业务类加载器持有者是一个链式结构，因为每一级的调用都会设置自己的业务 ClassLoader,多层级之间的调用
 * 就会组成一个链式结构，每一层执行完操作后都会清除当前层的 ClassLoader
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
public class BizClassLoaderHolder {

    private static final ThreadLocal<ClassLoaderNode> holder = new ThreadLocal<ClassLoaderNode>();

    /**
     * 设置业务类加载器
     * 当前还存在业务类加载器时，则设置子级的业务类加载器，如果当前没有业务类加载器，则
     * 当前的类加载器节点是顶节点
     *
     * @param classLoader 业务类加载器
     */
    public static void setBizClassLoader(ClassLoader classLoader) {
        if (null == classLoader) {
            return;
        }
        ClassLoaderNode classLoaderNode = holder.get();
        ClassLoaderNode child = new ClassLoaderNode(classLoader, classLoaderNode);
        holder.set(child);
    }

    /**
     * 清除业务类加载器
     * 当前的业务类加载器节点是顶点时，则清空 ThreadLocal
     * 当前业务类加载器是非顶点时，则设置当前类加载器顶点为父级
     */
    public static void clearBizClassLoader() {
        ClassLoaderNode stack = holder.get();
        if (stack == null) {
            return;
        }
        ClassLoaderNode parent = stack.parent;
        if (parent == null) {
            holder.remove();
        } else {
            holder.set(parent);
        }
    }

    /**
     * 获取当前节点的业务类加载器
     * 获取不存业务类加载器则获取当前线程类加载器
     *
     * @return 业务类加载器
     */
    public static ClassLoader getBizClassLoader() {
        ClassLoaderNode stack = holder.get();
        if (stack == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader instanceof RoutingURLClassLoader || classLoader == ModuleClassLoader.class.getClassLoader()) {
                return null;
            }
            return Thread.currentThread().getContextClassLoader();
        }
        ClassLoader classLoader = stack.classLoader;
        return classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;
    }

    /**
     * 业务类加载器节点，由这些节点组成链表结构
     * 对应每一个调用层级为一个节点
     */
    static class ClassLoaderNode {
        ClassLoader classLoader;
        ClassLoaderNode parent;

        ClassLoaderNode(ClassLoader classLoader, ClassLoaderNode parent) {
            this.classLoader = classLoader;
            this.parent = parent;
        }
    }
}
