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
package com.pamirs.pradar.upload.info;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author shiyajian
 * create: 2020-07-20
 */
public enum AppJarInfo {

    INSTANCE;

    enum PackageType {
        JAR, WAR;
    }


    private final static Logger LOGGER = LoggerFactory.getLogger(AppJarInfo.class);

    /**
     * 业务应用包对应的绝对路径
     */
    private String absPath = "";

    /**
     * jar包 或者 war包 ；
     */
    private PackageType packageType = PackageType.WAR; // default war

    /**
     * 是否是 Spring Boot 项目
     * 构成 Spring Boot 项目的两个要素：包名后面是 .jar 、包里面有 BOOT-INF 这个目录
     */
    private boolean isBoot = false;

    /**
     * 如果是 maven 编译, 里面信息有一些打包编译相关信息
     */
    private final Map<String, Object> pomProperties = new HashMap<String, Object>();

    /**
     * MANIFEST.MF：这个 manifest 文件定义了与扩展和包相关的数据。单词“manifest”的意思是“显示”
     */
    private final Map<String, Object> manifestProperties = new HashMap<String, Object>();

    /**
     * 项目依赖的 jar 包,这个是 Jar 包里面的路径，一般从 BOOT-INF/ 或者 META-INF/ 开头，不是绝对路径
     */
    private final List<String> libs = new ArrayList<String>();

    /**
     * 根据绝对路径，是不是已经把  jar 或者 war 加载进来了
     */
    private boolean loaded = false;

    /**
     * 是否对 app 的绝对路径进行了初始化和校验
     */
    private boolean checked = false;

    public synchronized boolean init(String appAbsPath) {
        if (checked) {
            return true;
        }
        if (StringUtils.isEmpty(appAbsPath)) {
            LOGGER.error("'pradar.app.abspath' can't be null ! ");
            return false;
        }
        File file = new File(appAbsPath);
        if (!file.exists()) {
            LOGGER.error("can't find application.jar/war, file not exists, please recheck 'pradar.app.abspath' ");
            return false;
        }
        if (appAbsPath.endsWith("jar")) {
            packageType = PackageType.JAR;
            checked = true;
            absPath = appAbsPath;
            return true;
        }
        if (appAbsPath.endsWith("war")) {
            packageType = PackageType.WAR;
            checked = true;
            absPath = appAbsPath;
            return true;
        }
        LOGGER.error("the file from 'pradar.app.abspath', not jar or war! ");
        return false;
    }

    /**
     * 从业务应用的绝对路径读取信息
     */
    public synchronized void load() {

        if (!checked) {
            LOGGER.error(" need init ");
            return;
        }

        if (loaded) {
            return;
        }

        // read zip package
        try {
            FileInputStream fileInputStream = new FileInputStream(absPath);
            ZipInputStream zin = new ZipInputStream(fileInputStream);
            try {
                ZipFile zf = new ZipFile(absPath);
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {

                    // jar 包中包含 BOOT-INF 则证明这个 Spring Boot 项目
                    if (!isBoot && ze.getName().startsWith("BOOT-INF")) {
                        isBoot = true;
                    }

                    // 将所有的lib保存下来
                    if (ze.getName().indexOf("/lib/") > 0 && ze.getName().endsWith(".jar")) {
                        libs.add(ze.getName());
                        continue;
                    }

                    if (ze.getName().endsWith("MANIFEST.MF")) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            int i = line.indexOf(":");
                            if (i > 0) {
                                String key = line.substring(0, i);
                                String value = line.substring(i + 1);
                                manifestProperties.put(key.trim(), value.trim());
                            }
                        }
                        br.close();
                        continue;
                    }

                    // 读取pom.xml
                    if (ze.getName().endsWith("pom.properties")) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.startsWith("#")) {
                                continue;
                            }
                            int i = line.indexOf("=");
                            if (i > 0) {
                                String key = line.substring(0, i);
                                String value = line.substring(i + 1);
                                pomProperties.put(key.trim(), value.trim());
                            }
                        }
                        br.close();
                        continue;
                    }
                    //TODO 要不要读用户的 application.properties / yml 这些文件呢？？
                    // 留点补充的空间，以后可以考虑读到 logback、spring 配置等等；
                }
            } finally {
                fileInputStream.close();
                zin.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        loaded = true;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public Map<String, Object> getPomProperties() {
        return pomProperties;
    }

    public Map<String, Object> getManifestProperties() {
        return manifestProperties;
    }

    public List<String> getLibs() {
        return libs;
    }

    public boolean isBoot() {
        return isBoot;
    }

    public String getAbsPath() {
        return absPath;
    }
}
