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

package io.shulie.tro.cloud.data.result.scenemanage;

import java.io.Serializable;

import io.shulie.tro.cloud.common.enums.machine.EnumResult;
import lombok.Data;

/**
 * @ClassName SlaDetailResult
 * @Description
 * @Author qianshui
 * @Date 2020/5/18 下午11:44
 */
@Data
public class SlaDetailResult implements Serializable {

    private static final long serialVersionUID = 9171434959213456889L;

    private String ruleName;

    private String businessActivity;

    private String rule;

    private EnumResult status;
}
