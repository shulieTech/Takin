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
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TSecondLinkMnt;

/**
 * @author shulie
 * @description 二级链路管理业务接口
 * @create 2018/6/18 16:09
 */
public interface TSecondLinkMntService {

    /**
     * 保存二级链路
     *
     * @param secondLinkMnt 二级链路信息
     * @version v1.0
     * @Date:
     */
    void saveSecondLink(TSecondLinkMnt secondLinkMnt) throws Exception;

    /**
     * 查询二级链路列表
     *
     * @param linkName     二级链路名称
     * @param baseLinkName 基础链路名称
     * @param pageNum      当前页
     * @param pageSize     每页记录数
     * @return 二级链路列表
     * @throws TROModuleException 查询出错抛出异常
     * @version v1.0
     * @Date:
     */
    PageInfo<TSecondLinkMnt> queryLinkList(String linkName, String baseLinkName,
        Integer pageNum, Integer pageSize) throws TROModuleException;

    /**
     * 根据二级链路id查询链路信息(Map格式)
     *
     * @param linkId 链路id
     * @return 二级链路详情
     * @throws TROModuleException 查询出错抛出异常
     * @version v1.0
     * @Date:
     */
    Map<String, Object> queryLinkMapByLinkId(String linkId) throws TROModuleException;

    List<List<Map<String, Object>>> getBasicLinkBySecondLinkId(String secondLinkId);

    /**
     * 根据二级链路id查询链路信息
     *
     * @param linkId 链路id
     * @return 二级链路详情
     * @throws TROModuleException 查询出错抛出异常
     * @version v1.0
     * @Date:
     */
    TSecondLinkMnt queryLinkByLinkId(String linkId) throws TROModuleException;

    /**
     * 批量删除二级链路
     *
     * @param linkIds 二级链路id列表,逗号分隔
     * @throws TROModuleException 删除失败抛出异常
     * @version v1.0
     * @Date:
     */
    void deleteLinkByLinkIds(String linkIds) throws TROModuleException;

    /**
     * 更新二级链路信息
     *
     * @param secondLinkMnt 二级链路信息
     * @throws TROModuleException 修改失败抛出异常
     * @version v1.0
     * @Date:
     */
    void updateLinkinfo(TSecondLinkMnt secondLinkMnt) throws TROModuleException;

    /**
     * 更新二级链路的测试状态
     *
     * @param secondLinkId 二级链路信息
     * @param testStatus   状态值
     * @throws TROModuleException 修改失败抛出异常
     * @version v1.0
     * @Date:
     */
    void updateSecondLinkStatus(String secondLinkId, String testStatus);

    /**
     * 根据链路信息查询应用列表
     *
     * @param linkId    链路id
     * @param linkLevel 链路级别（一级/二级）
     * @return 应用列表
     * @throws TROModuleException 查询出错抛出异常
     * @version v1.0
     * @Date:
     */
    Map<String, List<TApplicationMnt>> queryApplicationListByLinkInfo(String linkId, String linkLevel)
        throws TROModuleException;
}
