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

import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TSecondLinkMnt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 二级链路业务接口
 *
 * @author shulie
 * @version v1.0
 * @date 2018年5月16日
 */
@Mapper
public interface TSecondLinkMntDao {

    /**
     * 说明: 保存时根据链路名称判断链路是否存在
     *
     * @param linkName 链路名称
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int saveSecondLinkExist(@Param("linkName") String linkName);

    /**
     * 说明: 更新时根据二级链路id判断链路是否存在
     *
     * @param secondLinkId 二级链路id
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int updateLinkExist(@Param("secondLinkId") String secondLinkId);

    /**
     * 说明: 添加二级链路信息
     *
     * @param secondLinkMnt 二级链路实体类
     * @author shulie
     */
    void addSecondLink(TSecondLinkMnt secondLinkMnt);

    /**
     * 说明: 根据链路id查询链路信息详情
     *
     * @param linkId 链路id
     * @return 二级链路详细信息
     * @author shulie
     */
    TSecondLinkMnt queryLinkByLinkId(@Param("linkId") String linkId);

    /**
     * 说明: 批量删除二级链路
     *
     * @param linkIdLists 链路id列表
     * @author shulie
     */
    void deleteLinkByLinkIds(@Param("linkIdLists") List<String> linkIdLists);

    /**
     * 说明: 根据id列表批量查询二级链路信息
     *
     * @param secondLinkIds 二级链路id列表
     * @return 二级链路集合
     * @author shulie
     * @date 2018/11/5 16:37
     */
    List<Map<String, Object>> querySecondLinkListByIds(@Param("secondLinkIds") List<String> secondLinkIds);

    /**
     * 说明: 更新二级链路信息
     *
     * @param secondLinkMnt 链路服务实体类
     * @author shulie
     */
    void updateLink(TSecondLinkMnt secondLinkMnt);

    /**
     * 查询二级链路列表
     *
     * @param linkName 二级链路名称
     * @return 二级链路列表
     */
    List<TSecondLinkMnt> queryLinkList(@Param("linkName") String linkName);

    /**
     * 从视图中查询二级链路列表
     *
     * @param paramMap 二级/基础链路名称（linkName/baseLinkName）
     * @return 二级链路列表
     */
    @Deprecated
    List<TSecondLinkMnt> querySecondLinkListByView(Map<String, Object> paramMap);

    /**
     * 根据基础链路查询应用列表
     *
     * @param linkId 基础链路id
     * @return 应用列表
     */
    List<TApplicationMnt> queryApplicationByBaseLinkId(@Param("linkId") String linkId);

    /**
     * @param paramMap 二级/基础链路名称（linkName/baseLinkName）
     * @return java.util.List<com.pamirs.tro.entity.domain.TSecondLinkMnt>
     * @description 查询二级链路列表
     * @author shulie
     * @create 2018/6/20 10:45
     */
    List<TSecondLinkMnt> querySecondLinkList(Map<String, Object> paramMap);

    String queryAppIdByAppName(@Param("linkName") String appName);

    String queryIdByAswId(@Param("aswid") String aswId);

}
