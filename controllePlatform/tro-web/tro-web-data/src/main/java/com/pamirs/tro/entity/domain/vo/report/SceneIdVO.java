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

package com.pamirs.tro.entity.domain.vo.report;

import java.io.Serializable;

import io.shulie.tro.web.common.domain.WebRequest;
import lombok.Data;

/**
 * @ClassName SceneIdVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 下午3:47
 */
@Data
public class SceneIdVO extends WebRequest implements Serializable {

    private static final long serialVersionUID = -9013282538536892377L;

    private Long sceneId;
}
