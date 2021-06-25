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

package com.pamirs.tro.entity.domain.entity.linkmanage;

import java.util.Set;

import lombok.Data;

/**
 * @description: 节点IP信息、MQ TOPIC信息、DB  链接  表信息等
 * @author: CaoYanFei@ShuLie.io
 * @create: 2020-07-19 13:56
 **/
@Data
public class VertexOpData {
    private Set<String> ipList;
    private Set<String> dataList;
    private Set<String> unKnowIpList;
}
