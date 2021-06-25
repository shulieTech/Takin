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

package io.shulie.tro.cloud.biz.output.scenetask;

import java.util.List;

import lombok.Data;

/**
 * @ClassName SceneActionOutput
 * @Description
 * @Author qianshui
 * @Date 2020/11/13 上午10:58
 */
@Data
public class SceneActionOutput {

    private Long data;

    private Long reportId;

    private List<String> msg;
}
