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

package io.shulie.tro.web.data.dao.application;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import io.shulie.tro.web.data.model.mysql.LinkGuardEntity;
import io.shulie.tro.web.data.param.application.LinkGuardCreateParam;
import io.shulie.tro.web.data.param.application.LinkGuardUpdateUserParam;
import io.shulie.tro.web.data.result.linkguard.LinkGuardResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 8:44 下午
 * @Description:
 */
public interface LinkGuardDAO extends IService<LinkGuardEntity> {

    /**
     * 新建挡板, 起名为2, 为了防止与
     * mp 冲突
     *
     * @param param 创建所需参数
     * @return 操作行数
     */
    int insert2(LinkGuardCreateParam param);

    List<LinkGuardResult> selectByAppNameUnderCurrentUser(String appName, Long customerId);

    int allocationUser(LinkGuardUpdateUserParam param);

    /**
     * 通过应用id, 获得挡板列表
     * 来自导出请求, 需要的字段不多
     *
     * @param applicationId 应用id
     * @return 挡板列表
     */
    List<LinkGuardEntity> listFromExportByApplicationId(Long applicationId);

    /**
     * 更新应用名
     * @param applicationId
     * @param appName
     */
    void updateAppName(Long applicationId,String appName);

}
