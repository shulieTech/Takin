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

package io.shulie.tro.web.data.param.filemanage;

import java.util.Date;

import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class FileManageCreateParam {

    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小：2MB
     */
    private String fileSize;

    /**
     * 文件类型：0-脚本文件 1-数据文件 2-附件 3-jmeter 4 -shell ext jar
     */
    private Integer fileType;

    private String fileExtend;

    private Long customerId;

    private String uploadPath;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 是否删除
     * 以下两个, 更新cloud脚本文件用到
     */
    private Integer isDeleted;

    /**
     * 是否分隔
     */
    private Integer isSplit;

}
