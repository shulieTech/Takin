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

package io.shulie.tro.web.data.dao.filemanage;

import java.util.List;

import io.shulie.tro.web.data.param.filemanage.FileManageCreateParam;
import io.shulie.tro.web.data.result.filemanage.FileManageResult;

/**
 * @author zhaoyong
 */
public interface FileManageDAO {

    /**
     * 根据id查询文件信息
     * @param fileIds
     * @return
     */
    List<FileManageResult> selectFileManageByIds(List<Long> fileIds);

    /**
     * 批量删除
     * @param fileIds
     */
    void deleteByIds(List<Long> fileIds);

    /**
     * 批量创建文件
     * @param fileManageCreateParams
     * @return
     */
    List<Long> createFileManageList(List<FileManageCreateParam> fileManageCreateParams);
}
