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

package io.shulie.tro.web.data.result.linkmange;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/10/19 7:44 下午
 * @Description:
 */
@Data
public class LinkManageResult {

    private Long linkId;
    private String linkName;
    private String entrace;
    private String ptEntrace;
    private String changeBefore;
    private String changeAfter;
    private String changeRemark;
    private Integer isChange;
    private Integer isJob;
    private Integer isDeleted;
    private Long customerId;
    private Long userId;
    private Date createTime;
    private Date updateTime;
    private String applicationName;
    private Integer changeType;
    private Integer canDelete;
    private String features;

}
