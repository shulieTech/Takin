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

package io.shulie.tro.cloud.biz.service.cloudServer.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.shulie.tro.cloud.common.constants.SceneManageConstant;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-05-14 17:59
 * @Description:
 */

@Service
@Slf4j
public class FileWriteService {
    @Autowired
    private RedisClientUtils redisClientUtils;

    @Value("${script.path}")
    private String filePath;

    /**
     * 获取字节起始位置
     *
     * @param fileSceneUniqueKey
     * @param requirePos
     * @return
     */
    public synchronized Long calculateStartPos(String fileSceneUniqueKey, long requirePos) {
        //每次获取新位置
        Object dbPos = redisClientUtils.getObject(fileSceneUniqueKey);
        Long startPos = 0L;
        if (dbPos != null) {
            startPos = Long.valueOf(dbPos.toString());
        }
        return startPos;

    }

    public void write(Long startPos, String filename, Long sceneId, byte[] bytes) throws Exception {
        if (StringUtils.isBlank(filename) || sceneId == null || bytes == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(sceneId);
        builder.append("-");
        builder.append(filename);
        /**
         * 起始位置不为空则将数据写入指定的pos，否则是多线程、多客户端上传，计算出新的起始位置
         */
        if (startPos == null) {
            startPos = calculateStartPos(builder.toString(), bytes.length);
        }
        /**
         * 抢占字节数组占用的位置
         */
        redisClientUtils.incrementAndNotExpire(builder.toString(), bytes.length);
        String path = filePath + SceneManageConstant.FILE_SPLIT + sceneId + SceneManageConstant.FILE_SPLIT + filename;

        if (!exitFile(path)) {
            createFile(path, (long)bytes.length);
        }
        writeByte(path, bytes, startPos, null);

    }

    public void writeByte(String filePath, byte[] bytes, Long startPos, Integer fileSize) throws IOException {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(filePath, "rw");
            file.seek(startPos);
            file.write(bytes);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }

    }

    /**
     * 创建特定大小的空文件
     *
     * @param filepath
     * @param sizeInBytes
     * @return
     * @throws IOException
     */
    private File createFile(String filepath, final Long sizeInBytes) throws IOException {

        File file = new File(filepath);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            file.createNewFile();
        }
        return file;
    }

    public boolean exitFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
