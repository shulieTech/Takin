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

import com.pamirs.tro.entity.domain.entity.linkmanage.SceneAndBusinessLink;
import com.pamirs.tro.entity.domain.entity.linkmanage.SceneLinkRelate;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessFlowTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TSceneLinkRelateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SceneLinkRelate record);

    int batchInsert(@Param("list") List<SceneLinkRelate> records);

    int insertSelective(SceneLinkRelate record);

    SceneLinkRelate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SceneLinkRelate record);

    int updateByPrimaryKey(SceneLinkRelate record);

    //根据场景名删除
    int deleteBySceneId(@Param("sceneId") String sceneName);

    List<SceneAndBusinessLink> selectSceneIdByTechLinkId(@Param("techLinkId") String techLinkId);

    List<SceneLinkRelate> selectBySceneId(@Param("sceneId") Long sceneId);

    int updateEntranceNameBySystemProcessId(@Param("linkId") String linkId, @Param("newEntrance") String newEntrance);

    List<String> selectBusinessIdByParentBusinessId(@Param("parentBusinessId") List<String> parentBusinessId);

    List<BusinessFlowTree> findAllRecursion(@Param("sceneId") String sceneId);

    int countBySceneId(@Param("sceneId") Long sceneId);

    int countByTechLinkIds(@Param("list") List<String> techLinkIds);

    long countByBusinessLinkId(@Param("businessLinkId") Long businessLinkId);
}
