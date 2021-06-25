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

package io.shulie.tro.cloud.open.api;

import java.util.Map;

import io.shulie.tro.cloud.open.req.filemanager.FileContentParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileCopyParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileCreateByStringParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileDeleteParamReq;
import io.shulie.tro.cloud.open.req.filemanager.FileZipParamReq;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @author shiyajian
 * create: 2020-10-19
 */
public interface CloudFileApi {

    /**
     * 获取文件内容
     * @param fileContentParamReq
     * @return
     */
    ResponseResult<Map<String, Object>> getFileContent(FileContentParamReq fileContentParamReq);

    /**
     * 删除文件
     *
     * @param fileDeleteParamReq
     * @return
     */
    ResponseResult<Boolean> deleteFile(FileDeleteParamReq fileDeleteParamReq);

    /**
     * 复制文件到指定目录
     *
     * @param fileCopyParamReq
     * @return
     */
    ResponseResult<Boolean> copyFile(FileCopyParamReq fileCopyParamReq);

    /**
     * 将指定文件打包到指定目录
     *
     * @param fileZipParamReq
     * @return
     */
    ResponseResult<Boolean> zipFile(FileZipParamReq fileZipParamReq);

    /**
     * 将字符串转为指定文件
     * @param fileCreateByStringParamReq
     * @return
     */
    ResponseResult<Boolean>  createFileByPathAndString(FileCreateByStringParamReq fileCreateByStringParamReq);
}
