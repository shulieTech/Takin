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

package com.pamirs.tro.entity.dao.scenemanage;

import com.pamirs.tro.entity.domain.entity.scenemanage.SceneManage;
import io.shulie.tro.cloud.common.annotation.DataApartInterceptAnnotation;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryBean;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TSceneManageMapper {

    int deleteByPrimaryKey(Long id);

    Long insertSelective(SceneManage record);

    SceneManage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SceneManage record);

    int updateStatus(UpdateStatusBean record);

    @DataApartInterceptAnnotation
    List<SceneManage> getPageList(SceneManageQueryBean queryVO);

    /**
     * 查询所有场景信息
     * @return
     */
    List<SceneManage> selectAllSceneManageList();

    int resumeStatus(Long id);

    int updateSceneUserById(@Param("id") Long id, @Param("userId") Long userId);

    List<SceneManage> getByIds(@Param("ids") List<Long> ids) ;


}
