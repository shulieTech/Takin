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

package com.pamirs.tro.entity.dao.pressureready;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.vo.TDataBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 数据构建dao层
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface TDataBuildDao {

    /**
     * 说明: 新增链路时把链路应用数据插入数据构建表
     *
     * @param map 构建表信息参数
     * @author shulie
     */
    void insertDataBuild(Map<String, Object> map);

    /**
     * 说明: 根据条件查询构建信息列表
     *
     * @param map 二级链路名称,基础链路名称,应用名称,负责人工号
     * @return 成功, 则返回构建列表信息, 失败则返回错误编码和错误信息
     */
    List<TDataBuild> queryBuildinfo(Map<String, Object> map);

    /**
     * 说明: 新增脚本执行状态
     *
     * @param map 接收参数
     * @author shulie
     */
    void updateScriptExcuteStatus(Map<String, Object> map);

    /**
     * 说明: 根据脚本类型和应用id查询脚本构建状态
     *
     * @param applicationId 应用id
     * @param scriptType    脚本类型
     * @author shulie
     */
    Map<String, Object> queryScriptExcuteStatus(@Param("applicationId") String applicationId,
        @Param("scriptType") String scriptType);

    /**
     * 说明: 查询缓存预热上次执行成功时间和执行状态
     *
     * @param applicationId 应用id
     * @return 缓存预热上次执行成功时间和执行状态
     * @author shulie
     */
    Map<String, Object> queryCacheStatus(@Param("applicationId") String applicationId);

    /**
     * 说明: 查询影子库整体同步检测结果
     *
     * @param applicationId 应用id
     * @return 影子库整体同步检测结果
     * @author shulie
     * @date 2018/5/28 10:50
     */
    Map<String, Object> queryCheckShadowlib(@Param("applicationId") String applicationId);

    /**
     * 说明: 批量删除构建应用信息
     *
     * @param applicationIdLists 多个应用id
     * @author shulie
     */
    void deleteApplicationToDataBuild(@Param("applicationIdLists") List<String> applicationIdLists);

    /**
     * 说明: 根据应用id列表批量查询构建信息
     *
     * @param applicationIds 应用id集合
     * @return 构建信息
     * @author shulie
     * @date 2018/11/5 10:39
     */
    List<Map<String, Object>> queryDataBuildListByIds(@Param("applicationIds") List<String> applicationIds);

    /**
     * 说明: 数据构建调试开关
     *
     * @param map 应用id集合
     * @return
     * @author shulie
     * @date 2018/7/4 19:35
     */
    void debugSwitchUpdate(Map<String, Object> map);
}
