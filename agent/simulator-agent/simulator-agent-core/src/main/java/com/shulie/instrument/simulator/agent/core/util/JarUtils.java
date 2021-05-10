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
package com.shulie.instrument.simulator.agent.core.util;

import org.slf4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 2:57 下午
 */
public final class JarUtils {
    private static final String CLASS_EXT = ".class";
    private static final String JAR_FILE_EXT = ".jar";
    private static final String ERROR_MESSAGE = "packageName can't be null";
    private static final String DOT = ".";
    private static final String ZIP_SLASH = "/";
    private static final String BLACK = "";
    /**
     * (1) 文件过滤器，过滤掉不需要的文件
     **/
    private static FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return isClass(pathname.getName()) || isDirectory(pathname) || isJarFile(pathname.getName());
        }
    };

    /**
     * 读取目录下所有的文件
     *
     * @param dir    目录
     * @param filter 过滤器
     * @return 文件列表
     */
    public static List<File> readFiles(File dir, FileFilter filter) {
        if (dir == null || !dir.exists() || !dir.isDirectory() || !dir.canRead()) {
            return Collections.EMPTY_LIST;
        }
        if (filter != null) {
            return Arrays.asList(dir.listFiles(filter));
        } else {
            return Arrays.asList(dir.listFiles());
        }
    }

    /**
     * 将文件列表转换成 url 列表
     *
     * @param files 文件列表
     * @return 返回 URL数组
     */
    public static URL[] toURLs(List<File> files) {
        if (files == null || files.isEmpty()) {
            return new URL[0];
        }
        URL[] urls = new URL[files.size()];
        for (int i = 0, len = files.size(); i < len; i++) {
            try {
                urls[i] = files.get(i).toURI().toURL();
            } catch (MalformedURLException e) {
                continue;
            }
        }
        return urls;
    }

    /**
     * 如果packageName为空，就抛出空指针异常。</br>
     * (本工具类有一个bug，如果扫描文件时需要一个包路径为截取字符串的条件，现在还没有修复,所以加上该条件)
     *
     * @param packageName
     */
    private static void ckeckNullPackageName(String packageName) {
        if (packageName == null || packageName.trim().length() == 0) {
            throw new NullPointerException(ERROR_MESSAGE);
        }
    }

    /**
     * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."
     * </br>那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
     *
     * @param packageName
     * @return
     */
    private static String getWellFormedPackageName(String packageName) {
        return packageName.lastIndexOf(DOT) != packageName.length() - 1 ? packageName + DOT : packageName;
    }

    /**
     * 扫描jar包下所有的package
     *
     * @return
     */
    public static Set<String> scanPackages(JarFile jarFile) {
        return processJarFile(jarFile);
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，</br> 如果包路径为 com.abs + A.class
     * 但是输入 abs 会产生classNotFoundException</br> 因为className 应该为 com.abs.A 现在却成为
     * abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return
     */
    public static List<String> scanClasses(String packageName) {
        //检测packageName 是否为空，如果为空就抛出NullPointException
        ckeckNullPackageName(packageName);
        final List<String> classes = new ArrayList<String>();
        // 遍历在classpath 下面的jar包，class文件夹(现在没有包括 java jre)
        for (String classPath : getClassPathArray()) {
            // 填充 classes
            fillClasses(new File(classPath), getWellFormedPackageName(packageName), classes);
        }
        return classes;
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，</br> 如果包路径为 com.abs + A.class
     * 但是输入 abs 会产生classNotFoundException</br> 因为className 应该为 com.abs.A 现在却成为
     * abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return
     */
    public static List<String> scanClasses(String packageName, String path) {
        //检测packageName 是否为空，如果为空就抛出NullPointException
        ckeckNullPackageName(packageName);
        final List<String> classes = new ArrayList<String>();
        // 遍历在classpath 下面的jar包，class文件夹(现在没有包括 java jre)
        // 填充 classes
        fillClasses(new File(path), getWellFormedPackageName(packageName), classes);
        return classes;
    }

    /**
     * 扫面改包路径下所有class文件
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return
     */
    public static List<String> scanPackageClasses(String packageName) {
        return scanClasses(packageName);
    }

    /**
     * 填充满足条件的class 填充到 classes
     *
     * @param file        类路径下的文件
     * @param packageName 需要扫面的包名
     * @param classes     List 集合
     */
    private static void fillClasses(File file, String packageName, List<String> classes) {
        if (isDirectory(file)) {
            processDirectory(file, packageName, classes);
        } else if (isClass(file.getName())) {
            processClassFile(file, packageName, classes);
        } else if (isJarFile(file.getName())) {
            processJarFile(file, packageName, classes);
        }
    }

    /**
     * 处理如果为目录的情况,需要递归调用 fillClasses方法
     *
     * @param directory
     * @param packageName
     * @param classes
     */
    private static void processDirectory(File directory, String packageName, List<String> classes) {
        for (File file : directory.listFiles(fileFilter)) {
            fillClasses(file, packageName, classes);
        }
    }

    /**
     * 处理为class文件的情况,填充满足条件的class 到 classes
     *
     * @param file
     * @param packageName
     * @param classes
     */
    private static void processClassFile(File file, String packageName, List<String> classes) {
        final String filePathWithDot = file.getAbsolutePath().replace(File.separator, DOT);
        int subIndex = -1;
        if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
            final String className = filePathWithDot.substring(subIndex).replace(CLASS_EXT, BLACK);
            fillClass(className, packageName, classes);
        }
    }

    /**
     * 处理为jar文件的情况，填充满足条件的class 到 classes
     *
     * @param file
     * @param packageName
     * @param classes
     */
    private static void processJarFile(File file, String packageName, List<String> classes) {
        try {
            for (ZipEntry entry : Collections.list(new ZipFile(file).entries())) {
                if (isClass(entry.getName())) {
                    final String className = entry.getName().replace(ZIP_SLASH, DOT).replace(CLASS_EXT, BLACK);
                    fillClass(className, packageName, classes);
                }
            }
        } catch (Throwable ex) {
            // ignore this ex
        }
    }

    private static Set<String> processJarFile(JarFile file) {
        Set<String> packageNames = new HashSet<String>();
        try {
            for (JarEntry entry : Collections.list(file.entries())) {
                String entryName = entry.getName();
                if (entryName.startsWith("META-INF")) {
                    continue;
                }
                if (entry.isDirectory()) {
                    continue;
                }
                if (!entryName.endsWith(".class")) {
                    continue;
                }
                entryName = entryName.substring(0, entryName.lastIndexOf('/'));
                if (entryName == null || entryName.trim().length() == 0) {
                    continue;
                }
                String packageName = entryName.replace("/", ".");
                packageNames.add(packageName);
            }
        } catch (Throwable ex) {
            // ignore this ex
        }
        return packageNames;
    }

    /**
     * 填充class 到 classes
     *
     * @param className
     * @param packageName
     * @param classes
     */
    private static void fillClass(String className, String packageName, List<String> classes) {
        if (checkClassName(className, packageName)) {
            classes.add(className);
        }
    }

    private static String[] getClassPathArray() {
        return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
    }

    private static boolean checkClassName(String className, String packageName) {
        return className.indexOf(packageName) == 0;
    }

    private static boolean isClass(String fileName) {
        return fileName.endsWith(CLASS_EXT);
    }

    private static boolean isDirectory(File file) {
        return file.isDirectory();
    }

    private static boolean isJarFile(String fileName) {
        return fileName.endsWith(JAR_FILE_EXT);
    }

    /**
     * 返回写入文件的目录
     *
     * @param in
     * @param dirPath
     * @param fileName
     * @param fileLength
     * @param logger
     * @return
     * @throws IOException
     */
    public static void writeFile(InputStream in, String dirPath, String fileName, int fileLength, Logger logger) throws IOException {
        File file = new File(dirPath, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            int percent = -1;
            while ((size = in.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                int newPercent = (int) Math.round((len*1.0 / fileLength) * 100);
                if (percent < newPercent) {
                    logger.info("Downloading file {} -------> {}%\n", fileName, newPercent);
                    percent = newPercent;
                }
            }
        } finally {
            in.close();
            out.close();
        }
    }
}
