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
package com.shulie.instrument.simulator.core.server.jetty.servlet;

import com.alibaba.fastjson.JSON;
import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于处理模块的HTTP请求
 */
public class ModuleHttpServlet extends HttpServlet {
    private static final String SLASH = "/";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreConfigure config;
    private final CoreModuleManager coreModuleManager;

    public ModuleHttpServlet(final CoreConfigure config,
                             final CoreModuleManager coreModuleManager) {
        this.config = config;
        this.coreModuleManager = coreModuleManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(config.getServerCharset().name());
        doMethod(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(config.getServerCharset().name());
        doMethod(req, resp);
    }

    private void doResponse(Object content, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String useApi = StringUtils.trim(req.getParameter("useApi"));
        PrintWriter writer = resp.getWriter();
        try {
            if (Boolean.valueOf(useApi)) {
                String json = JSON.toJSONString(content);
                resp.setContentType("application/json");
                writer.write(json);
                writer.flush();
            } else {
                if (content instanceof CommandResponse) {
                    CommandResponse commandResponse = (CommandResponse) content;
                    if (commandResponse.isSuccess()) {
                        writer.write(commandResponse.getResult() == null ? "" : commandResponse.getResult().toString());
                    } else {
                        writer.write(commandResponse.getMessage() == null ? "" : commandResponse.getMessage());
                    }
                } else {
                    writer.write(content == null ? "" : content.toString());
                }
            }
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private void doMethod(final HttpServletRequest req,
                          final HttpServletResponse resp) throws ServletException, IOException {

        // 获取请求路径
        final String path = req.getPathInfo();

        // 获取模块ID
        final String uniqueId = parseUniqueId(path);
        if (StringUtils.isBlank(uniqueId)) {
            logger.warn("SIMULATOR: path={} is not matched any module.", path);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 获取模块
        final CoreModule coreModule = coreModuleManager.get(uniqueId);
        if (null == coreModule) {
            logger.warn("SIMULATOR: path={} is matched module {}, but not existed.", path, uniqueId);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 匹配对应的方法
        final Method method = matchingModuleMethod(
                path,
                uniqueId,
                coreModule.getModule().getClass()
        );

        if (null == method) {
            logger.warn("SIMULATOR: path={} is not matched any method in module {}",
                    path,
                    uniqueId
            );
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: path={} is matched method {} in module {}", path, method.getName(), uniqueId);
            }
        }

        // 自动释放I/O资源
        final List<Closeable> autoCloseResources = coreModule.append(new ReleaseResource<List<Closeable>>(new ArrayList<Closeable>()) {
            @Override
            public void release() {
                final List<Closeable> list = get();
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                for (final Closeable closeable : list) {
                    if (closeable instanceof Flushable) {
                        try {
                            ((Flushable) closeable).flush();
                        } catch (Exception cause) {
                            logger.warn("SIMULATOR: path={} flush I/O occur error!", path, cause);
                        }
                    }
                    try {
                        closeable.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

        // 生成方法调用参数
        final Object[] parameterObjectArray = generateParameterObjectArray(method, req, resp);

        final boolean isAccessible = method.isAccessible();
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            method.setAccessible(true);
            //将当前的 ClassLoader置成是模块的 ClassLoader，处理模块执行时的类加载问题
            Thread.currentThread().setContextClassLoader(coreModule.getClassLoaderFactory().getDefaultClassLoader());
            Object value = method.invoke(coreModule.getModule(), parameterObjectArray);
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: path={} invoke module {} method {} success.", path, uniqueId, method.getName());
            }
            doResponse(value, req, resp);
        } catch (IllegalAccessException iae) {
            logger.warn("SIMULATOR: path={} invoke module {} method {} occur access denied.", path, uniqueId, method.getName(), iae);
            throw new ServletException(iae);
        } catch (InvocationTargetException ite) {
            logger.warn("SIMULATOR: path={} invoke module {} method {} occur error.", path, uniqueId, method.getName(), ite.getTargetException());
            final Throwable targetCause = ite.getTargetException();
            if (targetCause instanceof ServletException) {
                throw (ServletException) targetCause;
            }
            if (targetCause instanceof IOException) {
                throw (IOException) targetCause;
            }
            throw new ServletException(targetCause);
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
            method.setAccessible(isAccessible);
            coreModule.release(autoCloseResources);
        }

    }


    /**
     * 提取模块ID
     * 模块ID应该在PATH的第一个位置
     *
     * @param path servlet访问路径
     * @return 路径解析成功则返回模块的ID，如果解析失败则返回null
     */
    private String parseUniqueId(final String path) {
        final String[] pathSegmentArray = StringUtils.split(path, "/");
        return ArrayUtils.getLength(pathSegmentArray) >= 1
                ? pathSegmentArray[0]
                : null;
    }


    /**
     * 匹配模块中复合HTTP请求路径的方法
     * 匹配方法的方式是：HttpMethod和HttpPath全匹配
     *
     * @param path          HTTP请求路径
     * @param uniqueId      模块ID
     * @param classOfModule 模块类
     * @return 返回匹配上的方法，如果没有找到匹配方法则返回null
     */
    private Method matchingModuleMethod(final String path,
                                        final String uniqueId,
                                        final Class<?> classOfModule) {

        // 查找@Command注解的方法
        for (final Method method : getMethodsListWithAnnotation(classOfModule, Command.class)) {
            final Command commandAnnotation = method.getAnnotation(Command.class);
            if (null == commandAnnotation) {
                continue;
            }
            // 兼容 value 是否以 / 开头的写法
            String cmd = appendSlash(commandAnnotation.value());
            final String pathOfCmd = "/" + uniqueId + cmd;
            if (StringUtils.equals(path, pathOfCmd)) {
                return method;
            }
        }
        // 找不到匹配方法，返回null
        return null;
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getMethodsListWithAnnotation(cls, annotationCls, false, false);
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls,
                                                             final Class<? extends Annotation> annotationCls,
                                                             boolean searchSupers, boolean ignoreAccess) {

        List<Class<?>> classes = (searchSupers ? getAllSuperclassesAndInterfaces(cls)
                : new ArrayList<Class<?>>());
        classes.add(0, cls);
        final List<Method> annotatedMethods = new ArrayList<Method>();
        for (Class<?> acls : classes) {
            final Method[] methods = (ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods());
            for (final Method method : methods) {
                if (method.getAnnotation(annotationCls) != null) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<Class<?>>();
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
        int superClassIndex = 0;
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() ||
                superClassIndex < allSuperclasses.size()) {
            Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if (superClassIndex >= allSuperclasses.size()) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (interfaceIndex < superClassIndex) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (superClassIndex < interfaceIndex) {
                acls = allSuperclasses.get(superClassIndex++);
            } else {
                acls = allInterfaces.get(interfaceIndex++);
            }
            allSuperClassesAndInterfaces.add(acls);
        }
        return allSuperClassesAndInterfaces;
    }

    private String appendSlash(String cmd) {
        // 若不以 / 开头，则添加 /
        if (!cmd.startsWith(SLASH)) {
            cmd = SLASH + cmd;
        }
        return cmd;
    }

    private boolean isMapWithGenericParameterTypes(final Method method,
                                                   final int parameterIndex,
                                                   final Class<?> keyClass,
                                                   final Class<?> valueClass) {
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length < parameterIndex
                || !(genericParameterTypes[parameterIndex] instanceof ParameterizedType)) {
            return false;
        }
        final Type[] actualTypeArguments = ((ParameterizedType) genericParameterTypes[parameterIndex]).getActualTypeArguments();
        return actualTypeArguments.length == 2
                && keyClass.equals(actualTypeArguments[0])
                && valueClass.equals(actualTypeArguments[1]);
    }

    /**
     * 生成方法请求参数数组
     * 主要用于填充HttpServletRequest和HttpServletResponse
     *
     * @param method 模块Java方法
     * @param req    HttpServletRequest
     * @param resp   HttpServletResponse
     * @return 请求方法参数列表
     */
    private Object[] generateParameterObjectArray(final Method method,
                                                  final HttpServletRequest req,
                                                  final HttpServletResponse resp) throws IOException {

        final Class<?>[] parameterTypeArray = method.getParameterTypes();
        if (ArrayUtils.isEmpty(parameterTypeArray)) {
            return null;
        }
        final Object[] parameterObjectArray = new Object[parameterTypeArray.length];
        for (int index = 0; index < parameterObjectArray.length; index++) {
            final Class<?> parameterType = parameterTypeArray[index];

            // HttpServletRequest
            if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                parameterObjectArray[index] = req;
            }

            // HttpServletResponse
            else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                parameterObjectArray[index] = resp;
            }

            // ParameterMap<String,String[]>
            else if (Map.class.isAssignableFrom(parameterType)
                    && isMapWithGenericParameterTypes(method, index, String.class, String[].class)) {
                parameterObjectArray[index] = req.getParameterMap();
            }

            // ParameterMap<String,String>
            else if (Map.class.isAssignableFrom(parameterType)
                    && isMapWithGenericParameterTypes(method, index, String.class, String.class)) {
                final Map<String, String> param = new HashMap<String, String>();
                for (final Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                    param.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
                }
                parameterObjectArray[index] = param;
            }

            // QueryString
            else if (String.class.isAssignableFrom(parameterType)) {
                parameterObjectArray[index] = req.getQueryString();
            }


        }

        return parameterObjectArray;
    }

}
