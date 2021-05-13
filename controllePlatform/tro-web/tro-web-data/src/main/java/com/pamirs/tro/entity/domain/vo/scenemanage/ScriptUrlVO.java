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

package com.pamirs.tro.entity.domain.vo.scenemanage;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName ScriptUrlVO
 * @Description
 * @Author qianshui
 * @Date 2020/4/22 上午4:14
 */
@Data
public class ScriptUrlVO implements Serializable {

    private static final long serialVersionUID = 2155178590508223791L;

    private String name;

    private Boolean enable;

    private String path;

    public ScriptUrlVO() {

    }

    public ScriptUrlVO(String name, Boolean enable, String path) {
        this.name = name;
        this.enable = enable;
        this.path = path;
    }
}
