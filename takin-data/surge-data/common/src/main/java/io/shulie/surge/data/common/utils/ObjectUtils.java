/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.common.utils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author pamirs
 */
public class ObjectUtils {

    private static Field cons;

    static {
        try {
            cons = ObjectStreamClass.class.getDeclaredField("cons");
            cons.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final <T> T newIntsance(Class<T> clazz) {
        // 先尝试用构造函数去生成
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor(new Class[0]);
            ctor.setAccessible(true);
            return ctor.newInstance(new Object[0]);
        } catch (Exception e) {
            if (!Serializable.class.isAssignableFrom(clazz)) {
                throw new RuntimeException("can not new " + clazz.getName() +
                        " from default constructor", e);
            }
        }

        // 再使用 Java 序列化机制去"强制"生成
        try {
            ObjectStreamClass osc = ObjectStreamClass.lookup(clazz);
            Constructor<?> constructor = (Constructor<?>) cons.get(osc);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("can not new " + clazz.getName() +
                    " thru Java deserialization", e);
        }
    }

    public static final Field getField(Class clazz, String fieldName) {
        try {
            Class c = clazz;
            while (c != null) {
                Field field = c.getDeclaredField(fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    return field;
                }
                c = c.getSuperclass();
            }
        } catch (Exception e) {
            throw new RuntimeException("fail to get field \"" + fieldName + "\"" +
                    " from class: " + clazz.getName(), e);
        }

        throw new RuntimeException("field \"" + fieldName + "\" " +
                "not found from class: " + clazz.getName());
    }

    public static final <T> T getFieldValue(Object obj, String fieldName) {
        Class clazz;
        if (obj instanceof Class) {
            clazz = (Class) obj;
            obj = null;
        } else {
            clazz = obj.getClass();
        }

        try {
            return (T) getField(clazz, fieldName).get(obj);
        } catch (Exception e) {
            throw new RuntimeException("fail to get value of field \"" + fieldName + "\"" +
                    " from class: " + clazz.getName(), e);
        }
    }

    public static final void setFieldValue(Object obj, String fieldName, Object value) {
        Class clazz;
        if (obj instanceof Class) {
            clazz = (Class) obj;
            obj = null;
        } else {
            clazz = obj.getClass();
        }

        try {
            getField(clazz, fieldName).set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("fail to get value of field \"" + fieldName + "\"" +
                    " from class: " + clazz.getName(), e);
        }
    }

    public static final boolean isImplementsInterface(Class clazz, Class interfaceClass) {
        while (clazz != null) {
            Class[] interfaces = clazz.getInterfaces();
            for (Class itf : interfaces) {
                if (itf == interfaceClass) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static final boolean isExtendsClass(Class clazz, Class baseClass) {
        while (clazz != null) {
            clazz = clazz.getSuperclass();
            if (clazz == baseClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扫描目标包获取所有Class
     *
     * @param targetClassLoader 目标ClassLoader
     * @param targetPackageName 目标包名称
     * @return 目标包路径下所有类集合
     * <p>
     * 代码摘抄自 http://www.oschina.net/code/snippet_129830_8767</p>
     */
    public static Set<Class<?>> scanPackage(final ClassLoader targetClassLoader, final String targetPackageName) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        // 获取包的名字 并进行替换
        String packageName = targetPackageName;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
//            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            dirs = targetClassLoader.getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
//					System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            true, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
//					System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(
                                            packageName.length() + 1,
                                            name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class
                                                .forName(packageName + '.'
                                                        + className));
                                    } catch (ClassNotFoundException e) {
                                        // log
                                        // .error("添加用户自定义视图类错误 找不到此类的.class文件");
//											e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
//						e.printStackTrace();
                    }
                } else if ("vfs".equalsIgnoreCase(protocol)) {
                    Object object = url.openConnection().getContent();
                    classes.addAll(getClasses(object, packageName));
                }
            }
        } catch (IOException e) {
//			e.printStackTrace();
        }

        return classes;
    }

    private static Set<Class<?>> getClasses(Object obj, String packageName) {
        List list = listChildren(obj);
        if (list.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (Object object : list) {
            String fileName = getClassName(object);
//            if (fileName == null) {
//                continue;
//            }
            if (isFile(object)) {
                try {
                    classes.add(Class.forName(packageName + "." + fileName));
                } catch (ClassNotFoundException e) {
                    continue;
                }
            } else {
                classes.addAll(getClasses(object, packageName + "." + fileName));
            }
        }
        return classes;
    }

    private static boolean isFile(Object obj) {
        Class clazz = obj.getClass();
        Method method = ReflectUtils.getMethod(clazz, "isFile", new Class[0]);
        if (method == null) {
            return true;
        }
        try {
            boolean isFile = (boolean) method.invoke(obj);
            return isFile;
        } catch (Exception e) {
            return true;
        }
    }

    private static String getClassName(Object obj) {
        Class clazz = obj.getClass();
        Method method = ReflectUtils.getMethod(clazz, "getName", new Class[0]);
        if (method == null) {
            return null;
        }
        try {
            String className = (String) method.invoke(obj);
            if (!className.endsWith(".class")) {
                return className;
            }
            return className.substring(0,className.indexOf(".class"));
        } catch (Exception e) {
            return null;
        }
    }

    private static List listChildren(Object obj) {
        Class clazz = obj.getClass();
        Method method = ReflectUtils.getMethod(clazz, "getChildren", new Class[0]);
        if (method == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            List list = (List) method.invoke(obj);
            return list;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     * <p/>
     * <p>
     * 代码摘抄自 http://www.oschina.net/code/snippet_129830_8767</p>
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(
                        packageName + "." + file.getName(),
                        file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' +
                    // className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
//                    e.printStackTrace();
                }
            }
        }
    }

    public static final Set<Class<?>> findClassesUnderPackage(String packageName, ClassLoader classloader) {
        if (classloader == null) {
            classloader = Thread.currentThread().getContextClassLoader();
        }

        return scanPackage(classloader, packageName);
    }

    private static final void findClasses(String directory, String packageName,
                                          Collection<String> classNames) throws Exception {
        if (directory.startsWith("file:") && directory.contains(".jar!/")) {
            URL jar = new URL(directory.substring(0, directory.indexOf('!')));
            String packagePath = packageName.replace('.', '/') + '/';
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                    String className = getClassNameFromFileName(entryName);
                    classNames.add(className);
                }
            }
        }
        File dir = new File(directory);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file.getAbsolutePath(), packageName + '.' + file.getName(), classNames);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + getClassNameFromFileName(file.getName());
                classNames.add(className);
            }
        }
    }

    private static final String getClassNameFromFileName(String fileName) {
        String className = fileName.replace('/', '.').replace('$', '.');
        return className.substring(0, className.length() - ".class".length());
    }
}
