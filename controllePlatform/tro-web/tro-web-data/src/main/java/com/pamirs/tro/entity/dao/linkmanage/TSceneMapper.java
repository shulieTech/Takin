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
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SceneDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.Scene;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.SceneQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TSceneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Scene record);

    int insertSelective(Scene record);

    Scene selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Scene record);

    int updateByPrimaryKey(Scene record);

    //根据场景名集合将场景的状态该为ischange
    void updateBySceneIdList(@Param("list") List<Long> changeToZeroSceneNameList, @Param("ischange") Integer ischange);

    @DataAuth(tableAlias = "scene")
    List<SceneDto> selectByRelatedQuery(SceneQueryVo vo);

    long count();

    long countByTime(java.util.Date date);

    int updateIsChangeByTechId(Long techLinkId);

    int valiteSceneName(@Param("sceneName") String sceneName);

    List<BusinessFlowIdAndNameDto> businessFlowIdFuzzSearch(@Param("businessFlowName") String businessFlowName);

    /**
     * 根据id列表查询业务流程名称
     * @param ids
     * @return
     */
    List<Scene> selectBusinessFlowNameByIds(@Param("list") List<Long> ids);
}
