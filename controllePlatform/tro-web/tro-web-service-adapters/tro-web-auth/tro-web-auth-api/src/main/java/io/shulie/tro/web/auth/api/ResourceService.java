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

package io.shulie.tro.web.auth.api;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.auth.Resource;
import io.shulie.tro.web.auth.api.enums.ResourceTypeEnum;

/**
 * @Author: fanxx
 * @Date: 2020/9/1 下午9:45
 * @Description:
 */
public interface ResourceService {
    /**
     * 获取全量菜单
     *
     * @return
     */
    List<Resource> getAllMenuResource();

    /**
     * 获取全量数据
     *
     * @return
     */
    List<Resource> getAllDataResource();

    /**
     * 获取用户拥有的资源
     *
     * @param userId
     * @return
     */
    List<Resource> getUserResourceById(ResourceTypeEnum resourceTypeEnum, String userId);

    /**
     * 根据资源id获取资源信息
     *
     * @param resourceIds
     * @return
     */
    List<Resource> getResourceByIds(List<Long> resourceIds);

    /**
     * 根据应用id查询资源信息
     *
     * @param applicationIds
     * @return
     */
    List<Resource> getResourceByApplicationIds(List<String> applicationIds);

    /**
     * 添加应用资源信息
     *
     * @param applicationMnts
     * @return
     */
    int addResourceForApplication(List<TApplicationMnt> applicationMnts);

    /**
     * 删除应用资源信息
     *
     * @param applicationIds
     * @return
     */
    int deleteResourceByApplicationIds(List<String> applicationIds);
}
