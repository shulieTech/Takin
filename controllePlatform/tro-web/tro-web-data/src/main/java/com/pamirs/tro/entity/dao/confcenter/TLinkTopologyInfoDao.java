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

import com.pamirs.tro.entity.domain.entity.LinkBottleneck;
import com.pamirs.tro.entity.domain.entity.TLinkTopologyInfo;
import com.pamirs.tro.entity.domain.vo.TLinkTopologyInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 透明流量拓扑图对象
 *
 * @author 298403
 */
@Mapper
public interface TLinkTopologyInfoDao {
    /**
     * 按主键删除
     *
     * @param tltiId
     * @return
     */
    int deleteByPrimaryKey(Long tltiId);

    /**
     * 单条插入
     *
     * @param record
     * @return
     */
    int insert(TLinkTopologyInfo record);

    /**
     * 单条不为null属性插入
     *
     * @param record
     * @return
     */
    int insertSelective(TLinkTopologyInfo record);

    /**
     * 通过主键搜索
     *
     * @param tltiId
     * @return
     */
    TLinkTopologyInfo selectByPrimaryKey(Long tltiId);

    /**
     * 通过主键更新不为null的
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TLinkTopologyInfo record);

    /**
     * 通过主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(TLinkTopologyInfo record);

    /**
     * 批量插入
     *
     * @param recordList
     * @return
     */
    int insertList(@Param("recordList") List<TLinkTopologyInfo> recordList);

    /**
     * 删除所有 数据 重新导入
     *
     * @return
     */
    int deleteAllData();

    /**
     * 通过 链路分组 二级链路名称 查询链路
     *
     * @param linkGroup
     * @param secondLinkId
     * @return
     */
    List<TLinkTopologyInfoVo> queryLinkTopologyByLinkGroup(@Param("linkGroup") String linkGroup,
        @Param("secondLinkId") String secondLinkId);

    /**
     * 查询链路拓扑
     *
     * @return
     */
    List<TLinkTopologyInfo> list(TLinkTopologyInfo record);

    /**
     * 查询链路组列表
     *
     * @return
     */
    List<Map<String, String>> queryLinkGroupInfo();

    /**
     * 查询瓶颈列表
     *
     * @param startTime 开始时间
     * @return
     */
    List<LinkBottleneck> queryBottleNeckPreTime(@Param("startTime") String startTime);
}
