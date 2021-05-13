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
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TFirstLinkMnt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 一级链路业务接口
 *
 * @author shulie
 * @version v1.0
 * @date 2018年5月16日
 */
@Mapper
public interface TFirstLinkMntDao {

    /**
     * 说明: 保存时根据链路名称判断链路是否存在
     *
     * @param firstLinkName 一级链路名称
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int saveLinkExist(@Param("linkName") String firstLinkName);

    /**
     * 说明: 更新时根据链路id判断链路是否存在
     *
     * @param firstLinkId 一级链路id
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int updateLinkExist(@Param("linkId") String firstLinkId);

    /**
     * 说明: 添加一级链路信息
     *
     * @param firstLinkMnt 一级链路实体类
     * @author shulie
     */
    void addLink(TFirstLinkMnt firstLinkMnt);

    /**
     * 说明: 根据链路id查询链路信息详情
     *
     * @param firstLinkId 一级链路id
     * @return 一级链路详细信息
     * @author shulie
     */
    TFirstLinkMnt queryLinkByLinkId(@Param("linkId") String firstLinkId);

    /**
     * 说明: 批量删除一级链路
     *
     * @param firstLinkIdLists 一级链路id列表
     * @author shulie
     */
    void deleteLinkByLinkIds(@Param("linkIdLists") List<String> firstLinkIdLists);

    /**
     * 说明: 根据id列表批量查询一级链路信息
     *
     * @param firstLinkIds 一级链路id列表
     * @return 一级链路集合
     * @author shulie
     * @date 2018/11/5 16:16
     */
    List<TFirstLinkMnt> queryFirstLinkByIds(@Param("firstLinkIds") List<String> firstLinkIds);

    /**
     * 说明: 更新一级链路信息
     *
     * @param firstLinkMnt 一级链路
     * @author shulie
     */
    void updateLink(TFirstLinkMnt firstLinkMnt);

    /**
     * 查询一级链路列表
     *
     * @param firstLinkName 一级链路名称
     * @return 一级链路列表
     */
    List<TFirstLinkMnt> queryLinkList(@Param("linkName") String firstLinkName);

    /**
     * 查询一级链路列表，（应用于通过二级链路名称查找一级链路列表）
     *
     * @param paramMap 一级/二级链路名称（firstLinkName,secondLinkName）
     * @return 一级链路列表
     */
    List<TFirstLinkMnt> queryLinkListByView(Map<String, Object> paramMap);

    /**
     * 根据二级链路id获取一级链路列表
     *
     * @param secondLinkId 　二级链路id
     * @return 一级链路列表
     */
    List<TFirstLinkMnt> queryLinkBySecondLinkId(@Param("secondLinkId") String secondLinkId);

}
