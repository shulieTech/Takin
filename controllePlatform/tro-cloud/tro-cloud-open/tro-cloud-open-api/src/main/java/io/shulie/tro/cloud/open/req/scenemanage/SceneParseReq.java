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

package io.shulie.tro.cloud.open.req.scenemanage;

import java.io.Serializable;

import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.open.bean.scenemanage
 * @description:
 * @date 2020/10/22 8:16 下午
 */
@Data
public class SceneParseReq extends HttpCloudRequest implements Serializable {

    private static final long serialVersionUID = 33734315777916535L;

    private Long scriptId;

    private String uploadPath;
    /**
     * 是否绝对路径
     */
    private boolean absolutePath;
}
