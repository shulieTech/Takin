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

package com.pamirs.tro.entity.domain.vo.linkmanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/8 22:36
 * @Description:
 */
@Data
@ApiModel(value = "middleWareEntity", description = "中间件实体类")
public class MiddleWareEntity {
    @ApiModelProperty(name = "id", value = "中间件类型id")
    private Long id;

    @ApiModelProperty(name = "middleWareType", value = "中间件类型")
    private String middleWareType;

    @ApiModelProperty(name = "middleWareName", value = "中间件名称")
    private String middleWareName;

    @ApiModelProperty(name = "version", value = "中间件版本号")
    private String version;

    /**
     * jar名字
     */
    private String jarName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof MiddleWareEntity) {
            MiddleWareEntity t = (MiddleWareEntity)o;
            return this.middleWareName.equalsIgnoreCase(t.middleWareName)
                && this.version.equalsIgnoreCase(t.version)

                && this.middleWareType.equalsIgnoreCase(t.middleWareType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return
            +this.middleWareType.hashCode()
                + this.middleWareName.hashCode()
                + this.version.hashCode();
    }

}
