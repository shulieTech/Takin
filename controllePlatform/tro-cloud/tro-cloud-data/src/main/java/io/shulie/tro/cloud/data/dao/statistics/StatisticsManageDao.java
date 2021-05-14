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

package io.shulie.tro.cloud.data.dao.statistics;

import java.util.List;

import io.shulie.tro.cloud.data.result.statistics.PressureListTotalResult;
import io.shulie.tro.cloud.data.result.statistics.PressurePieTotalResult;
import io.shulie.tro.cloud.data.result.statistics.ReportTotalResult;
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
     * 统计场景分类，返回饼状图数据
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>select status,count(1) as count from t_scene_manage where create_time &gt;= #{startTime} and create_time &lt;= #{endTime} "
            + "and is_deleted =0  GROUP BY STATUS</script>")
    List<PressurePieTotalResult> getPressureScenePieTotal(@Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 统计报告通过/未通过
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>SELECT sum(CASE conclusion WHEN 1 THEN 1 ELSE 0 END) as success,\n"
        + "sum(CASE conclusion WHEN 0 THEN 1 ELSE 0 END) as fail,count(1) as count\n"
        + "FROM t_report  "
        + "WHERE gmt_create &gt;= #{startTime} and gmt_create &lt;= #{endTime} "
        + "and status = 2 and is_deleted =0</script>")
    ReportTotalResult getReportTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 压测场景次数统计  标签数据需要从web获取
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>SELECT a.id,a.scene_name as name,a.create_time as gmtCreate,a.user_id as createName,\n"
        + "sum(CASE b.conclusion WHEN 1 THEN 1 ELSE 0 END) as success,\n"
        + "sum(CASE b.conclusion WHEN 0 THEN 1 ELSE 0 END) as fail,\n"
        + "count(1) as count\n"
        + "FROM t_scene_manage a,t_report b WHERE a.id = b.scene_id and b.status = 2 and b.is_deleted =0\n"
        + "and b.gmt_create &gt;= #{startTime} and b.gmt_create &lt;= #{endTime}\n"
        + "and a.is_deleted = 0 GROUP BY a.id,a.scene_name,a.create_time,a.create_name ORDER BY count desc,a.create_time desc LIMIT 5</script>")
    List<PressureListTotalResult> getPressureSceneListTotal(@Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 压测脚本次数统计
     * @param startTime
     * @param endTime
     * @param scriptIds
     * @return
     */
    @Select("<script>" +
        "SELECT script_id as id,\n"
        + "sum(CASE conclusion WHEN 1 THEN 1 ELSE 0 END) as success,\n"
        + "sum(CASE conclusion WHEN 0 THEN 1 ELSE 0 END) as fail,\n"
        + "count(1) as count\n"
        + "FROM t_report  WHERE script_id is not null \n"
        + "and gmt_create &gt;= #{startTime} and gmt_create &lt;= #{endTime}\n"
        + "and status = 2 and is_deleted =0\n"
        + "and script_id in\n" +
        " <foreach collection='scriptIds' open='(' close=')' separator=',' item='scriptId'>#{scriptId}</foreach>\n"
        + "GROUP BY script_id ORDER BY count DESC,gmt_create DESC LIMIT 5"
        + " </script>")
    List<PressureListTotalResult> getPressureScriptListTotal(@Param("startTime") String startTime,
        @Param("endTime") String endTime, @Param("scriptIds") List<Long> scriptIds);

}
