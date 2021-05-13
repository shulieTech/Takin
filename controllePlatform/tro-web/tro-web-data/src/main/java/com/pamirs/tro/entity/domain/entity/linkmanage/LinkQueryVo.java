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

import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/9 01:22
 * @Description:系统流程查询封装
 */
@Data
public class LinkQueryVo {
    //链路id
    private Long id;
    //业务链路名字
    private String name;
    //入口名字
    private String entrance;
    //是否变更
    private String isChange;
    //中间件类型
    private String middleWareType;
    //中间件名字
    private String middleWareName;
    //中间件版本
    private String middleWareVersion;
    //业务域
    private String domain;
    //系统流程名字
    private String systemProcessName;
}
