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

package com.pamirs.tro.entity.dao.confcenter;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TApplicationIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: application id relationship dao
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface TApplicationIpDao {

    /**
     * 说明: query application - ip by application name and type
     *
     * @param applicationName 应用名称
     * @return 应用列表
     * @author shulie
     */
    public List<TApplicationIp> queryApplicationIpByNameTypeList(@Param("applicationName") String applicationName,
        @Param("type") String type);

    /**
     * 说明: query application - ip by application name and type
     *
     * @param applicationName 应用名称
     * @return 应用列表
     * @author shulie
     */
    public List<TApplicationIp> queryApplicationIpByNameList(@Param("applicationName") String applicationName);

    /**
     * 说明: query application - ip by application ip
     *
     * @param applicationIp ip地址
     * @return 应用列表
     * @author shulie
     */
    public List<TApplicationIp> queryApplicationIpByIpList(@Param("applicationIp") String applicationIp);
}
