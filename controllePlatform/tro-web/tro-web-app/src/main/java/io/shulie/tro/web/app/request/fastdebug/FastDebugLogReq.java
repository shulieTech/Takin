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

package io.shulie.tro.web.app.request.fastdebug;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2021-01-05 13:47
 * @Description:
 */
@Data
public class FastDebugLogReq implements Serializable {
    private static final long serialVersionUID = 4947210163124637630L;

    private String fileName;

    private String filePath;

    private Integer totalLines;

    private Integer endLine;

    private String logContent;
    //日志文件是否存在
    private Boolean hasLogFile = true;
}
