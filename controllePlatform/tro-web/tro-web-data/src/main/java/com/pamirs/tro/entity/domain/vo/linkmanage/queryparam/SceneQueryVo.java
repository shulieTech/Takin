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

package com.pamirs.tro.entity.domain.vo.linkmanage.queryparam;

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/2 10:46
 * @Description:场景查询入参
 */
@ApiModel(value = "SceneQueryVo", description = "场景查询入参")
@Data
public class SceneQueryVo extends PagingDevice {
    @ApiModelProperty(name = "sceneId", value = "场景id")
    private Long sceneId;
    @ApiModelProperty(name = "sceneName", value = "业务流程名字")
    private String sceneName;
    /*  @ApiModelProperty(name = "linkName", value = "链路名字")
      private String linkName;*/
    @ApiModelProperty(name = "entrace", value = "入口")
    private String entrace;
    @ApiModelProperty(name = "ischanged", value = "是否变更")
    private String ischanged;

    @ApiModelProperty(name = "businessName", value = "业务活动名字")
    private String businessName;

    @ApiModelProperty(name = "middleWareType", value = "中间件类型")
    private String middleWareType;

    @ApiModelProperty(name = "middleWareName", value = "中间件名字")
    private String middleWareName;

    @ApiModelProperty(name = "version", value = "中间件版本")
    private String middleWareVersion;

}
