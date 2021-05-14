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

package io.shulie.tro.cloud.open.api.impl;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.cloud.open.api.CloudFileApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.filemanager.FileContentParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileCopyParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileCreateByStringParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileDeleteParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileZipParamReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;
import io.shulie.tro.utils.http.TroResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-10-19
 */
@Component
public class CloudFileApiImpl extends CloudCommonApi implements CloudFileApi {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;

    @Override
    public ResponseResult<Map<String, Object>> getFileContent(FileContentParamReq req) {
        TroResponseEntity<ResponseResult<Map<String, Object>>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.FILE_CONTENT_BY_PATHS,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<Map<String, Object>>>() {}, req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");

    }

    @Override
    public ResponseResult<Boolean> deleteFile(FileDeleteParamReq req) {
        TroResponseEntity<Boolean> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.FILE_DELETE_URL,
                getHeaders(req.getLicense()), Boolean.class,req);
        if(troResponseEntity.getSuccess()) {
            return ResponseResult.success(troResponseEntity.getBody());
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<Boolean> copyFile(FileCopyParamReq req) {
        TroResponseEntity<Boolean> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.FILE_COPY_URL,
                getHeaders(req.getLicense()), Boolean.class,req);
        if(troResponseEntity.getSuccess()) {
            return ResponseResult.success(troResponseEntity.getBody());
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");

    }

    @Override
    public  ResponseResult<Boolean>  zipFile(FileZipParamReq req) {
        TroResponseEntity<Boolean> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.FILE_ZIP_URL,
                getHeaders(req.getLicense()), Boolean.class,req);
        if(troResponseEntity.getSuccess()) {
            return ResponseResult.success(troResponseEntity.getBody());
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");

    }

    @Override
    public  ResponseResult<Boolean>  createFileByPathAndString(FileCreateByStringParamReq req) {
        TroResponseEntity<Boolean> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.FILE_CREATE_BY_STRING,
                getHeaders(req.getLicense()), Boolean.class,req);
        if(troResponseEntity.getSuccess()) {
            return ResponseResult.success(troResponseEntity.getBody());
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }



}
