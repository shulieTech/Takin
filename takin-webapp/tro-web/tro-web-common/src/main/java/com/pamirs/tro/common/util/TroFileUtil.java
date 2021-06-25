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

package com.pamirs.tro.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明: 文件操作工具类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/16 17:07
 */
public class TroFileUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(TroFileUtil.class);

    private static final String SEPARATOR = "/";
    private static final String[] fileShell = {"ddl.sh", "basic.sh", "cache.sh", "clean.sh", "ready.sh"};

    /**
     * 说明：递归删除所有文件以及目录
     *
     * @param dir 要删除的目录
     * @author shulie
     * @time：2018年1月15日 上午10:44:24
     */
    public static void cleanFile(File dir) {

        Arrays.stream(dir.listFiles()).forEach(file -> {
            if (file.isDirectory()) {
                cleanFile(file);
            }
            file.delete();
        });

        dir.delete();
    }

    /**
     * 说明: 递归删除文件夹以及子文件
     *
     * @param dirFile 文件目录
     * @author shulie
     * @date 2018/6/16 17:15
     */
    public static void recursiveDeleteFile(File dirFile) {
        File[] files = dirFile.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    recursiveDeleteFile(file1);
                    LOGGER.info(" 进入 " + dirFile + " 目录");
                } else {
                    file1.setExecutable(true);
                    file1.delete();
                    LOGGER.info(file1 + " 文件已删除......");
                }
            }
        }
        dirFile.delete();
        //LOGGER.info(dirFile + " 目录已删除......");
    }

    /**
     * 说明: 递归删除指定文件
     *
     * @param dirFile 目录文件
     * @param suffix  多个文件后缀名
     * @author shulie
     * @date 2018/6/16 17:32
     */
    public static void recursiveDeleteSpecialFile(File dirFile, String... suffix) {
        if (suffix == null || suffix.length == 0) {
            return;
        }

        File[] files = dirFile.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                recursiveDeleteSpecialFile(file1, suffix);
                System.out.println(" 进入 " + dirFile + " 目录");
            } else {
                file1.setExecutable(true);
                StringBuffer sb = new StringBuffer();
                for (String string : suffix) {
                    sb.append(file1.getName().endsWith(string));
                    sb.append("||");
                }
                boolean ifParam = StringUtils.contains(StringUtils.substringBeforeLast(sb.toString(), "||"), "true")
                    ? true : false;
                if (ifParam) {
                    file1.delete();
                    LOGGER.info(file1.getName() + " 文件已删除......");
                }
            }
        }
    }

    /**
     * 说明：递归删除指定的后缀名文件
     *
     * @param file
     * @param suffix
     * @author shulie
     * @time：2017年1月5日 下午3:39:50
     */
    public static void recursiveDeleteSpecialFileByFileFilter(final File file, final String suffix) {
        file.listFiles(pathname -> {
            if (pathname.isFile() && pathname.getName().endsWith(suffix)) {
                pathname.delete();
                LOGGER.info(pathname.getName() + " 文件已删除......");
                return true;
            } else {
                recursiveDeleteSpecialFileByFileFilter(pathname, suffix);
                return false;
            }
        });
    }

    /**
     * 说明: 获取目录下的指定文件
     *
     * @param dirFile 目录文件
     * @param suffix  文件后缀名
     * @author shulie
     * @date 2018/6/16 17:33
     */
    public static File[] getSpecialFile(File dirFile, String suffix) {

        return dirFile.listFiles((dir, name) -> {
            return name.endsWith(suffix);
        });
    }

    /**
     * 说明: 创建文件
     *
     * @param path 创建文件路径
     * @author shulie
     */
    public static void createFile(String path) throws TROModuleException {
        if (StringUtils.isEmpty(path)) {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_ADD_APPLICATION_CREATEDIR_EXCEPTION);
        }
        File file = new File(path);
        if (!file.exists()) {
            file.setWritable(true, false);
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                LOGGER.info("创建目录:" + file.getAbsolutePath() + "成功");
                try {
                    //创建脚本
                    for (int i = 0; i < fileShell.length; i++) {
                        File creatrFile = new File(path + File.separator + fileShell[i]);
                        boolean newFile = creatrFile.createNewFile();
                        if (newFile) {
                            LOGGER.info("创建文件:" + creatrFile.getAbsolutePath() + ",成功");
                        }
                    }
                    //赋予权限
                    String cmd = "chmod -R a+x " + path + File.separator + "*";
                    Process exec = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});
                    exec.waitFor();
                    LOGGER.info("文件权限赋予:" + file.getAbsolutePath() + ",成功");
                } catch (Exception e) {
                    LOGGER.info(e.getMessage(), e);
                }
            }
        }

    }

    /**
     * 说明: 创建文件夹
     *
     * @param dirPath 文件夹路径
     * @author shulie
     * @date 2018/9/5 20:13
     */
    public static void createDir(String dirPath) throws IOException, TROModuleException {
        if (StringUtils.isEmpty(dirPath)) {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_ADD_APPLICATION_CREATEDIR_EXCEPTION);
        }
        Path path = Paths.get(dirPath);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }

    /**
     * 说明: 根据文件路径判断文件目录是否存在,不存在则创建
     *
     * @param filePath 文件路径
     * @author shulie
     * @date 2018/9/5 20:13
     */
    public static void createDirByString(String filePath) throws TROModuleException {
        if (StringUtils.isEmpty(filePath)) {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_ADD_APPLICATION_CREATEDIR_EXCEPTION);
        }
        File dest = new File(filePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
    }

    /**
     * 删除某个文件
     *
     * @param filePath
     */
    public static void deleteFileByString(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 获取文件名的后缀
     * @Date 2019/4/17 0017 16:54
     * @Param [name]
     **/
    public static String suffix(String name) {
        int c = name.lastIndexOf(".");
        if (c >= 0) {
            return name.substring(c + 1);
        }
        return "";
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 使用uuid生成新的文件名
     * @Date 2019/4/17 0017 16:54
     * @Param [oldName]
     **/
    public static String UUIDFileName(String oldName) {
        return UUID.randomUUID().toString().replace("-", "") + "." + suffix(oldName);
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 保存文件并根据uuid生成新的文件名
     * @Date 2019/4/18 0018 16:48
     * @Param [file, path]
     **/
    public static String saveFileWithUUIDName(MultipartFile file, String path) throws TROModuleException {
        try {
            if (file != null && !file.isEmpty()) {
                File parent = new File(path);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                String fileName = UUIDFileName(file.getOriginalFilename());
                Path filePath = Paths.get(path, fileName);
                file.transferTo(filePath.toFile());
                return fileName;
            }
            return null;
        } catch (IOException oe) {
            throw new TROModuleException("保存文件异常" + oe.getMessage());
        }
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 去掉路径的前后 "/"
     * @Date 2019/4/22 0022 22:04
     * @Param [path]
     **/
    public static String normalize(String path) {
        return after(before(path));
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 去掉路径前面的"/"
     * @Date 2019/4/22 0022 22:03
     * @Param [path]
     **/
    public static String before(String path) {
        String temp = path;
        if (path.startsWith("/") || path.startsWith("\\")) {
            temp = temp.substring(1);
            return normalize(temp);
        }
        return temp;
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 去掉路径后面的"/"
     * @Date 2019/4/22 0022 22:03
     * @Param [path]
     **/
    public static String after(String path) {
        String temp = path;
        if (path.endsWith("/") || path.endsWith("\\")) {
            temp = temp.substring(0, temp.length() - 1);
            return normalize(temp);
        }
        return temp;
    }

    /**
     * @return java.lang.String
     * @Author 710524
     * @Description 将两个路径合并
     * @Date 2019/4/22 0022 22:03
     * @Param [parent, sub]
     **/
    public static String path(String parent, String sub) {
        return after(parent) + SEPARATOR + normalize(sub);
    }
}
