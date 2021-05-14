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

package io.shulie.tro.web.diff.cloud.impl;

import io.shulie.tro.cloud.open.api.CloudFileApi;
import io.shulie.tro.cloud.open.req.filemanager.FileCopyParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileCreateByStringParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileDeleteParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileZipParamReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.string.StringUtil;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.diff.api.DiffFileApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author shiyajian
 * create: 2020-10-19
 */
@Component
@Slf4j
public class DiffFileApiImpl implements DiffFileApi {
    @Autowired
    private CloudFileApi cloudFileApi;

    @Override
    public String getFileManageContextPath() {
        return null;
    }

    @Override
    public Boolean deleteFile(List<String> paths) {
        if (CollectionUtils.isEmpty(paths)){
            return true;
        }
        FileDeleteParamReq fileDeleteParamReq = new FileDeleteParamReq();
        fileDeleteParamReq.setPaths(paths);
        fileDeleteParamReq.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult<Boolean> result= cloudFileApi.deleteFile(fileDeleteParamReq);
        if(result.getSuccess()) {
            return result.getData();
        }
        return false;
    }

    @Override
    public Boolean copyFile(String targetPath, List<String> sourcePaths) {
        if (CollectionUtils.isEmpty(sourcePaths) || StringUtil.isBlank(targetPath)){
            return true;
        }
        FileCopyParamReq fileCopyParamReq = new FileCopyParamReq();
        fileCopyParamReq.setTargetPath(targetPath);
        fileCopyParamReq.setSourcePaths(sourcePaths);
        fileCopyParamReq.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult<Boolean> result= cloudFileApi.copyFile(fileCopyParamReq);
        if(result.getSuccess()) {
            return result.getData();
        }
        return false;
    }

    @Override
    public Boolean zipFile(String targetPath, List<String> sourcePaths, String zipFileName) {
        if (CollectionUtils.isEmpty(sourcePaths) || StringUtil.isBlank(targetPath) || StringUtil.isBlank(zipFileName)){
            return false;
        }
        FileZipParamReq fileZipParamReq = new FileZipParamReq();
        fileZipParamReq.setSourcePaths(sourcePaths);
        fileZipParamReq.setTargetPath(targetPath);
        fileZipParamReq.setIsCovered(false);
        fileZipParamReq.setZipFileName(zipFileName);
        fileZipParamReq.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult<Boolean> result= cloudFileApi.zipFile(fileZipParamReq);
        if(result.getSuccess()) {
            return result.getData();
        }
        return false;
    }

    @Override
    public Boolean createFileByPathAndString(String filePath, String fileContent) {
        if ( StringUtil.isBlank(fileContent) || StringUtil.isBlank(filePath)){
            return false;
        }
        FileCreateByStringParamReq fileCreateByStringParamReq = new FileCreateByStringParamReq();
        fileCreateByStringParamReq.setFileContent(fileContent);
        fileCreateByStringParamReq.setFilePath(filePath);
        fileCreateByStringParamReq.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult<Boolean> result= cloudFileApi.createFileByPathAndString(fileCreateByStringParamReq);
        if(result.getSuccess()) {
            return result.getData();
        }
        return false;
    }


}
