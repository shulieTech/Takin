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

package com.pamirs.tro.entity.domain.bo.scenemanage;

import java.util.Date;

import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-04-18
 */
@Data
public class WarnBO {

    /**
     * 报告ID
     */
    private Long reportId;

    /**
     * SLA ID
     */
    private Long slaId;

    /**
     * SLA名称
     */
    private String slaName;

    /**
     * 活动ID
     */
    private Long businessActivityId;

    /**
     * 活动名称
     */
    private String businessActivityName;

    /**
     * 总数
     */
    private Long total;

    /**
     * 报警内容
     */
    private String content;

    /**
     * 最后更新时间
     */
    private Date lastWarnTime;

}
