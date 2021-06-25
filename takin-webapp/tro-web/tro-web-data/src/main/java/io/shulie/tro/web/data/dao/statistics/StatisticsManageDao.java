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

package io.shulie.tro.web.data.dao.statistics;

import java.util.List;

import io.shulie.tro.web.data.result.statistics.PressureListTotalResult;
import io.shulie.tro.web.data.result.statistics.PressurePieTotalResult;
import io.shulie.tro.web.data.result.statistics.ScriptLabelListTotalResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.dao.scenemanage
 * @date 2020/10/26 4:40 下午
 */
@Mapper
public interface StatisticsManageDao {
    /**
     * 统计脚本类型，返回饼状图数据
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>SELECT type,count(1) as count FROM t_script_manage_deploy  where gmt_create &gt;= #{startTime} and gmt_create &lt;= #{endTime} "
            + "and is_deleted =0  and  status != 2  GROUP BY type</script>")
    List<PressurePieTotalResult> getPressureScenePieTotal(@Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取所有的实例id
     * @return
     */
    @Select("<script>SELECT id count FROM t_script_manage_deploy  where type = 0 and is_deleted =0 </script>")
    List<Long> selectScriptManageDeployIds();

    /**
     * 获取脚本标签数据
     * @param ids
     * @return
     */
    @Select("<script>" +
        "SELECT id,name,script_version as scriptVersion,tag_name as tags,gmt_create as gmtCreate,create_user_name as createName \n"
        + "FROM t_script_manage_deploy a left JOIN\n"
        + "(SELECT c.script_id,GROUP_CONCAT(d.tag_name) as tag_name \n"
        + "FROM t_script_tag_ref c left join t_tag_manage d on c.tag_id = d.id GROUP BY c.script_id) b \n"
        + "on a.script_id =b.script_id\n"
        + "WHERE a.is_deleted = 0 "
        + "and a.id in\n" +
        " <foreach collection='ids' open='(' close=')' separator=',' item='id'>#{id}</foreach>\n"
        + " </script>")
    List<PressureListTotalResult> getScriptTag(@Param("ids") List<Long> ids);

    /**
     * 获取场景标签数据
     * @param ids
     * @return
     */
    @Select("<script>" +
        "SELECT scene_id as id,GROUP_CONCAT(tag_name) as tags from t_scene_tag_ref a,t_tag_manage b WHERE a.tag_id = b.id "
        + "and a.scene_id in\n" +
        " <foreach collection='ids' open='(' close=')' separator=',' item='id'>#{id}</foreach>\n"
        + "GROUP BY scene_id\n"
        + " </script>")
    List<PressureListTotalResult> getSceneTag(@Param("ids") List<Long> ids);

    /**
     * 获取标签统计
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>" +
        "SELECT  tag_name as label,count(id) as count FROM t_script_manage_deploy a Left JOIN\n"
        + "(SELECT c.script_id,d.tag_name,d.gmt_create FROM t_script_tag_ref c left join t_tag_manage d on c.tag_id = d.id) b \n"
        + "on a.script_id =b.script_id "
        + "WHERE a.is_deleted = 0 and a.status != 2 "
        + "and b.gmt_create &gt;= #{startTime} and b.gmt_create &lt;= #{endTime} \n"
        + "and tag_name is not null  GROUP BY tag_name  ORDER BY count DESC,b.gmt_create DESC LIMIT 5 \n"
        + " </script>")
    List<ScriptLabelListTotalResult> getScriptLabelListTotal(@Param("startTime") String startTime,
        @Param("endTime") String endTime);


}
