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

package io.shulie.tro.web.app.request.filemanage;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class FileManageUpdateRequest implements Serializable {
    private static final long serialVersionUID = -4697425775712998397L;

    private Long id;

    private String uploadId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小：2MB
     */
    private String fileSize;

    /**
     * 文件类型：0-脚本文件 1-数据文件 2-脚本jar文件 3-jmeter ext jar
     */
    private Integer fileType;

    /**
     * 脚本内容，前端编辑之后的脚本，需要转化为文件存储到指定目录
     */
    private String scriptContent;

    @JsonProperty("uploadedData")
    private Long dataCount;

    private Integer isSplit;

    /**
     * 数据已被删除，新版本不新增进去
     */
    private Integer isDeleted;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;
}
