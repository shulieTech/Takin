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

package io.shulie.tro.web.data.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.shulie.tro.web.data.model.mysql.SceneSchedulerTaskEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: mubai
 * @Date: 2020-11-30 21:25
 * @Description:
 */
public interface SceneSchedulerTaskMapper  extends BaseMapper<SceneSchedulerTaskEntity> {

    @Select("select * from t_scene_scheduler_task where scene_id =#{sceneId} and is_deleted =0 ")
    SceneSchedulerTaskEntity selectBySceneId(@Param("sceneId")Long sceneId) ;

    @Delete(" delete from t_scene_scheduler_task where scene_id =#{sceneId} ")
    void deleteBySceneId(@Param("sceneId") Long sceneId ) ;
}
