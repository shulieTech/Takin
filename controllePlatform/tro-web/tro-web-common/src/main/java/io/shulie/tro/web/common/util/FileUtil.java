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

package io.shulie.tro.web.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName FileUtil
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 上午10:31
 */
@Slf4j
public class FileUtil {

    public static final int READ_SIZE = 10 * 1024;

    public static List<File> convertMultipartFileList(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<File> fileList = Lists.newArrayList();
        multipartFiles.stream().forEach(data -> {
            File file = convertMultipartFile(data);
            if (file != null) {
                fileList.add(file);
            }
        });
        return fileList;
    }
    public static String replaceFileName(String fileName) {
        return fileName.replace(" ","");
    }

    public static File convertMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() <= 0L) {
            return null;
        }
        File toFile = new File(multipartFile.getOriginalFilename());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            outputStream = new FileOutputStream(toFile);
            int bytesRead = 0;
            byte[] buffer = new byte[READ_SIZE];
            while ((bytesRead = inputStream.read(buffer, 0, READ_SIZE)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //替换文件名中的空格
        if(multipartFile.getOriginalFilename().contains(" ")){
           return rename(toFile, replaceFileName(multipartFile.getOriginalFilename()), true);
        }
        return toFile;
    }

    public static void deleteTempFile(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.size() == 0) {
            return;
        }
        multipartFiles.stream().forEach(data -> {
            File file = new File(replaceFileName(data.getOriginalFilename()));
            if (file.exists()) {
                file.delete();
            }
        });
    }

    /**
     * 修改文件或目录的文件名，不变更路径，只是简单修改文件名<br>
     * @param file        被修改的文件
     * @param newName     新的文件名，包括扩展名
     * @param isOverride  是否覆盖目标文件
     * @return 目标文件
     */
    public static File rename(File file, String newName, boolean isOverride) {
        final Path path = file.toPath();
        final CopyOption[] options = isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
        try {
            return Files.move(path, path.resolveSibling(newName), options).toFile();
        } catch (IOException e) {
            log.error("修改文件名称异常", e);
        }
        return file;
    }

}
