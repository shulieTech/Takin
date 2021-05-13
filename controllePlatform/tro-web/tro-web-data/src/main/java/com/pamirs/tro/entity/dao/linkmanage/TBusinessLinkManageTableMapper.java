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

import java.util.List;

import com.pamirs.tro.entity.annocation.DataAuth;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessLinkDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.BusinessLinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.LinkQueryVo;
import org.apache.ibatis.annotations.Param;

public interface TBusinessLinkManageTableMapper {
    int deleteByPrimaryKey(Long linkId);

    int insert(BusinessLinkManageTable record);

    int insertSelective(BusinessLinkManageTable record);

    BusinessLinkManageTable selectByPrimaryKey(Long linkId);

    int updateByPrimaryKeySelective(BusinessLinkManageTable record);

    int updateByPrimaryKey(BusinessLinkManageTable record);

    int countByBussinessName(@Param("linkName") String linkName);

    @DataAuth(tableAlias = "manage")
    List<BusinessLinkDto> selectBussinessLinkListBySelective2(LinkQueryVo queryVo);

    BusinessLinkDto selectBussinessLinkById(@Param("id") Long id);

    List<BusinessLinkManageTable> selectBussinessLinkByIdList(@Param("list") List<Long> businessIds);

    int updateByTechId(BusinessLinkManageTable businessLinkManageTable);

    //统计有效的业务活动
    long count();

    int updateIsChangeByTechId(@Param("techLinkId") Long techLinkId);

    List<BusinessActiveIdAndNameDto> bussinessActiveNameFuzzSearch(
        @Param("bussinessActiveName") String bussinessActiveName);

    /**
     * 按照系统流程的id修改业务活动中的入口名
     *
     * @param linkId
     * @param newEntrance
     * @return
     */
    int updateEntranceNameBySystemProcessId(@Param("linkId") String linkId, @Param("newEntrance") String newEntrance);

    /**
     * 根据业务活动id集合查询系统流程id集合
     *
     * @param ids
     * @return
     */
    List<String> selectTechIdsByBusinessIds(@Param("list") List<Long> ids);

    List<BusinessLinkManageTable> selectByPrimaryKeys(@Param("list") List<Long> businessIds);

    long cannotdelete(@Param("list") List<Long> relateBusinessLinkIds, @Param("canDelete") Long canDelete);
}
