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

package io.shulie.tro.cloud.web.entrypoint.controller.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.vo.cloudserver.BigFileUploadVO;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.shulie.tro.cloud.biz.service.cloudServer.BigFileService;

/**
 * @Author: mubai
 * @Date: 2020-05-12 14:47
 * @Description:
 */

@RestController
@RequestMapping("/api/bigfile/")
public class BigFileUploadController {

    public static Logger logger = LoggerFactory.getLogger(BigFileUploadController.class);
    @Autowired
    private BigFileService bigFileService;


    private static void writeFile(HttpServletResponse response, File file) {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[64];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    @PostMapping("upload")
    public ResponseResult upload(HttpServletRequest request, @RequestBody byte[] byteData) {
        //字段放在header 中，body 放字节数组
        String license = request.getHeader("license");
        String sceneId = request.getHeader("sceneId");
        String fileName = request.getHeader("fileName");
        String startPos = request.getHeader("startPos");
        if (license == null || sceneId == null || fileName == null) {
            return ResponseResult.fail("license | sceneId | fileName can not be null", "");
        }

        BigFileUploadVO param = new BigFileUploadVO();
        param.setByteData(byteData);
        param.setFileName(fileName);
        param.setLicense(license);
        param.setSceneId(Long.valueOf(sceneId));
        if (startPos != null) {
            param.setStartPos(Long.valueOf(startPos));
        }
        logger.info(" receive data ....");
        return bigFileService.upload(license, param);
    }

    @ApiOperation("客户端下载")
    @GetMapping(value = "download", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadFile(HttpServletResponse response) {
        logger.info("上传客户端下载...");
        File pradarUploadFile = null;
        try {
            pradarUploadFile = bigFileService.getPradarUploadFile();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        if (pradarUploadFile != null) {
            writeFile(response, pradarUploadFile);
        }

    }

}
