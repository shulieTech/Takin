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

package com.pamirs.tro.entity.domain.dto.scenemanage;

import com.pamirs.tro.entity.domain.vo.scenemanage.SceneBusinessActivityRefVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneBusinessActivityRefDTO
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午9:47
 */
@Data
public class SceneBusinessActivityRefDTO extends SceneBusinessActivityRefVO {

    private static final long serialVersionUID = -6384484202725660595L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "绑定关系")
    private String bindRef;

    @ApiModelProperty(value = "应用IDS")
    private String applicationIds;

}
