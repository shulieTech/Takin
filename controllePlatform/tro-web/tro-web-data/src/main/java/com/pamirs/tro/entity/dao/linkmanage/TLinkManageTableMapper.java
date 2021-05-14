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

package com.pamirs.tro.entity.dao.linkmanage;

import java.util.Date;
import java.util.List;

import com.pamirs.tro.entity.annocation.DataAuth;
import com.pamirs.tro.entity.domain.dto.linkmanage.TechLinkDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.LinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.LinkQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TLinkManageTableMapper {
    int deleteByPrimaryKey(Long linkId);

    int logicDeleteByPrimaryKey(@Param("linkId") Long linkId);

    int insert(LinkManageTable record);

    int insertSelective(LinkManageTable record);

    LinkManageTable selectByPrimaryKey(Long linkId);

    int updateByPrimaryKeySelective(LinkManageTable record);

    int updateByPrimaryKey(LinkManageTable record);

    //条件查询
    List<LinkManageTable> selectBySelective(LinkManageTable table);

    //条件查询
    @DataAuth(tableAlias = "manage")
    List<TechLinkDto> selectTechLinkListBySelective2(LinkQueryVo queryVo);

    TechLinkDto selectTechLinkById(@Param("linkId") Long linkId);

    int counItemtByTechLinkIds(@Param("linkIds") List<Long> linkIds);

    //校验链路名不能重复
    int count(@Param("linkName") String linkName);

    int countByEntrance(@Param("entrance") String entrance);

    //统计系统流程的总数
    long countTotal();

    //统计系统流程变更的数量
    long countChangeNum();

    //统计应用数量
    long countApplication();

    long countSystemProcessByTime(Date date);

    long countApplicationByTime(Date date);

    long cannotdelete(@Param("linkId") Long linkId, @Param("canDelelte") Long canDelelte);

    /**
     * 模糊查询所有入口
     *
     * @param entrance
     * @return
     */
    List<String> entranceFuzzSerach(@Param("entrance") String entrance);

    /**
     * 业务活动id查询系统流程
     *
     * @param id
     * @return
     */
    List<LinkManageTable> selectByBussinessId(@Param("id") Long id);
}
