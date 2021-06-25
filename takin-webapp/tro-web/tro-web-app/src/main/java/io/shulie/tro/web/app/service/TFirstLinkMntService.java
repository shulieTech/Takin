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

package io.shulie.tro.web.app.service;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.domain.entity.TFirstLinkMnt;

/**
 * @author shulie
 * @description 一级链路管理业务接口
 * @create 2018/7/11 13:54
 */
public interface TFirstLinkMntService {

    /**
     * 保存一级链路信息
     *
     * @param firstLinkMnt 一级链路信息
     * @throws TROModuleException 保存失败抛出异常
     * @version v1.0
     * @Date:
     */
    void saveLink(TFirstLinkMnt firstLinkMnt) throws TROModuleException;

    /**
     * 查询一级链路列表
     *
     * @param firstLinkName  一级链路名称
     * @param secondLinkName 二级链路名称
     * @param pageNum        当前页
     * @param pageSize       每页记录数
     * @return 一级链路列表
     * @throws TROModuleException 查询失败抛出异常
     * @version v1.0
     * @Date:
     */
    PageInfo<TFirstLinkMnt> queryLinkList(String firstLinkName, String secondLinkName,
        Integer pageNum, Integer pageSize) throws TROModuleException;

    /**
     * 根据链路id查询一级链路信息
     *
     * @param firstLinkId 链路id
     * @return 一级链路信息
     * @throws TROModuleException 查询失败抛出异常
     * @version v1.0
     * @Date:
     */
    TFirstLinkMnt queryLinkByLinkId(String firstLinkId) throws TROModuleException;

    /**
     * 查询一级链路详情（Map格式）
     *
     * @param firstLinkId 链路id
     * @return 一级链路详情
     * @throws TROModuleException 查询失败抛出异常
     * @version v1.0
     * @Date:
     */
    Map<String, Object> queryLinkMapByLinkId(String firstLinkId) throws TROModuleException;

    /**
     * 批量删除一级链路
     *
     * @param firstLinkIds 一级链路id列表
     * @throws TROModuleException 删除失败抛出异常
     * @version v1.0
     * @Date:
     */
    void deleteLinkByLinkIds(String firstLinkIds) throws TROModuleException;

    /**
     * 修改一级链路信息
     *
     * @param firstLinkMnt 一级链路信息
     * @throws TROModuleException 修改失败抛出异常
     * @version v1.0
     * @Date:
     */
    void updateLinkinfo(TFirstLinkMnt firstLinkMnt) throws TROModuleException;

    /**
     * 根据一级链路id获取链路拓扑图
     *
     * @param firstLinkId 一级链路id
     * @return 一级/二级/基础链路对应关系
     * @throws TROModuleException 查询失败抛出异常
     * @version v1.0
     * @Date:
     */
    Map<String, Object> getLinkTopologyByFirstLinkId(String firstLinkId) throws TROModuleException;

    /**
     * 根据二级链路id查询一级链路
     *
     * @param secondLinkId 二级链路id
     * @return 包含二级链路的一级链路列表
     * @version v1.0
     * @Date:
     */
    List<TFirstLinkMnt> queryLinkBySecondLinkId(String secondLinkId);
}
