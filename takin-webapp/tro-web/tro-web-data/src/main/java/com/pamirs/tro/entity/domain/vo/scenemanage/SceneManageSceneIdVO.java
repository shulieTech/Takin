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

import io.shulie.tro.web.common.domain.WebRequest;
import lombok.Data;

/**
 * @ClassName SceneManageIdVO
 * @Description
 * @Author qianshui
 * @Date 2020/4/21 下午5:01
 */
@Data
public class SceneManageSceneIdVO extends WebRequest implements Serializable {

    private static final long serialVersionUID = 5258828941952507100L;

    private Long sceneId;
}
