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

package io.shulie.tro.web.app.controller.pmassist;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.exception.TROModuleException;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.FileUploadService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明: 文件的上传下载
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/4 15:00
 */
@Api(tags = "文件的上传下载")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class FileUploadController {

    private final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 说明:  API.05.02.008 抽数sql上传接口(上传单个文件)
     *
     * @param file A representation of an uploaded file received in a multipart request.
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/5 20:10
     */
    @PostMapping(value = APIUrls.API_TRO_SQL_UPLOAD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> fileupload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseError.create("文件为空,请重新上传");
            }
            return ResponseOk.create(fileUploadService.executeFile(file));
        } catch (TROModuleException e) {
            LOGGER.error("FileUploadController.fileupload" + e.getErrorMessage(), e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("文件上传失败", e);
            return ResponseError.create("文件上传失败");
        }
    }

    /**
     * 说明: API.05.02.009 抽数sql批量上传接口
     *
     * @param files A representation of an uploaded file received in a multipart request.
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/5 20:22
     */
    @PostMapping(value = APIUrls.API_TRO_SQL_BATCH_UPLOAD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> batchFileUpload(MultipartFile[] files) {
        try {
            return ResponseOk.create(fileUploadService.batchUploadFile(files));
        } catch (TROModuleException e) {
            LOGGER.error("FileUploadController.batchFileUpload" + e.getErrorMessage(), e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("FileUploadController.batchFileUpload" + e.getMessage(), e);
            return ResponseError.create("文件上传失败");
        }
    }

    /**
     * 说明: 下载文件
     *
     * @param fileName 文件名称
     * @author shulie
     * @date 2018/6/4 16:28
     */
    @GetMapping(value = APIUrls.API_TRO_SQL_DOWNLOAD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> downloadFile(@RequestParam("file") String fileName, HttpServletResponse response)
        throws IOException {
        try {
            fileUploadService.downloadFile(fileName, response);
            return ResponseOk.create("下载文件成功");
        } catch (TROModuleException e) {
            LOGGER.error("FileUploadController.downloadFile" + e.getMessage(), e);
            return ResponseOk.create("下载文件失败");
        }

    }
}


