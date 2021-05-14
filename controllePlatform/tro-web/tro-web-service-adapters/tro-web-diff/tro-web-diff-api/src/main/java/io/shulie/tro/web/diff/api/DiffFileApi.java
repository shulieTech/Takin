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

package io.shulie.tro.web.diff.api;

import java.util.List;
import java.util.Map;

import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @author shiyajian
 * create: 2020-10-19
 */
public interface DiffFileApi {

    /**
     * 获取文件上传、下载的路径
     * 私有化版本：获得tro-web的路径
     * 云版本：获得阿里云tro-cloud的路径
     */
    String getFileManageContextPath();

    /**
     * 删除文件
     * @param paths
     * @return
     */
    public Boolean deleteFile(List<String> paths);

    /**
     * 复制文件到指定目录
     * @param targetPath
     * @param sourcePaths
     * @return
     */
    public Boolean copyFile(String targetPath,List<String> sourcePaths);

    /**
     * 将指定文件打包到指定目录
     * @param targetPath
     * @param sourcePaths
     * @param zipFileName
     * @return
     */
    public Boolean zipFile(String targetPath,List<String> sourcePaths,String zipFileName);

    /**
     * 将字符串转为指定文件
     * @param filePath
     * @param fileContent
     * @return
     */
    Boolean createFileByPathAndString(String filePath,String fileContent);
}
