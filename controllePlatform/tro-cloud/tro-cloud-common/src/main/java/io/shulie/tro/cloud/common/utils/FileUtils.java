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

package io.shulie.tro.cloud.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/4/18 16:00.
 */
public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static File createFileDE(String filePathName) {
        File file = new File(filePathName);
        if (file.exists()) {
            if (!file.delete())
                return null;
        }
        if (!makeDir(file.getParentFile()))
            return null;
        try {
            if (!file.createNewFile())
                return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return file;
    }

    public static boolean makeDir(File dir) {
        if (!dir.exists()) {
            File parent = dir.getParentFile();
            if (parent != null)
                makeDir(parent);
            return dir.mkdir();
        }
        return true;
    }

    public static List<File> getDirectoryFiles(String dir, String fileEndsWith) {
        List<File> scriptFiles = new ArrayList<>();
        if (dir == null) {
            return null;
        }
        File fileDir = new File(dir);
        if (!fileDir.isDirectory()) {
            logger.warn("Expected a dir, but not: '{}'", fileDir.getPath());
        }
        if (!fileDir.isAbsolute()) {
            logger.warn("Expected a absolute path, bu not: '{}'", fileDir.getPath());
        }
        File[] files = fileDir.listFiles(file -> {
            if (fileEndsWith == null) {
                return true;
            } else {
                return file.getName().endsWith(fileEndsWith);
            }
        });
        if (files == null || files.length == 0) {
            return null;
        }

        scriptFiles.addAll(Arrays.asList(files));
        return scriptFiles;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    if (!deleteDir(new File(child))) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir：要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName：要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static boolean writeTextFile(String content, String filePathName) {
        File file = createFileDE(filePathName);
        if (file == null) {
            return false;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return true;
    }

    public static String readTextFileContent(File file) {
        InputStreamReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            char[] buffer = new char[32];
            int length;
            while ((length = reader.read(buffer)) > 0) {
                stringBuilder.append(buffer, 0, length);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 构建目录
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir,String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    public static void tarGzFileToFile(String sourcePath,String extractPath){
        TarInputStream tarIn = null;
        try{
            tarIn = new TarInputStream(new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(sourcePath))),
                1024 * 2);

            createDirectory(extractPath,null);//创建输出目录

            TarEntry entry = null;
            while( (entry = tarIn.getNextEntry()) != null ){

                if(entry.isDirectory()){//是目录
                    entry.getName();
                    createDirectory(extractPath,entry.getName());//创建空目录
                }else{//是文件
                    File tmpFile = new File(extractPath + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/",null);//创建输出目录
                    OutputStream out = null;
                    try{
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while((length = tarIn.read(b)) != -1){
                            out.write(b, 0, length);
                        }

                    }catch(IOException ex){
                        throw ex;
                    }finally{
                        if(out!=null){
                            out.close();
                        }
                    }
                }
            }
        }catch(IOException ex){
            logger.error("解压归档文件出现异常{}",ex);
        } finally{
            try{
                if(tarIn != null){
                    tarIn.close();
                }
            }catch(IOException ex){
                logger.error("关闭tarFile出现异常{}",ex);
            }
        }
    }
}
