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

package io.shulie.tro.web.data.param.blacklist;

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.blacklist
 * @date 2021/4/6 2:22 下午
 */
@Data
public class BlacklistUpdateParam {

    /**
     * 主键id
     */
    private Long blistId;

    /**
     * 黑名单类型
     */
    private Integer type ;

    /**
     * redisKey
     */
    private String redisKey ;


    /**
     * 应用id
     */
    private Long applicationId ;

    /**
     * 插入时间
     */
    private Date gmtCreate;

    /**
     * 变更时间
     */
    private Date gmtModified;


    /**
     * 是否可用(0表示未启动,1表示启动,2表示启用未校验)
     */
    private Integer useYn;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    public BlacklistUpdateParam() {
        gmtModified = new Date();
    }
}
