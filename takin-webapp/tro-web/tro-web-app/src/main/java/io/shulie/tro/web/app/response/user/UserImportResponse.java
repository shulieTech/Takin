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

package io.shulie.tro.web.app.response.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/3/8 2:50 下午
 * @Description:
 */
@Data
public class UserImportResponse {

    @ApiModelProperty(name = "success", value = "是否导入成功")
    private Boolean success;

    @ApiModelProperty(name = "errorMsg", value = "错误消息")
    private String errorMsg;

    @ApiModelProperty(name = "writeBack", value = "是否需要回写文件")
    private Boolean writeBack;

    @ApiModelProperty(name = "filePath", value = "文件地址")
    private String filePath;

}
