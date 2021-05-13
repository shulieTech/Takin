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

package com.pamirs.tro.entity.dao.preventcheat;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TApplicationMntConfig;
import com.pamirs.tro.entity.domain.vo.TApplicationMntConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 应用配置
 *
 * @author 298403
 */
@Mapper
public interface TApplicationMntConfigDao {

    /**
     * 根据主键删除
     *
     * @param tamcId
     * @return
     */
    int deleteByPrimaryKey(Long tamcId);

    /**
     * 插入
     *
     * @param record
     * @return
     */
    int insert(TApplicationMntConfig record);

    /**
     * 根据字段不为null插入
     *
     * @param record
     * @return
     */
    int insertSelective(TApplicationMntConfig record);

    /**
     * 通过主键id 搜索
     *
     * @param tamcId
     * @return
     */
    TApplicationMntConfig selectByPrimaryKey(Long tamcId);

    /**
     * 通过主键id更新 不为null的
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TApplicationMntConfig record);

    /**
     * 根据主键id 更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(TApplicationMntConfig record);

    /**
     * 分页 查寻应用配置
     *
     * @param paramMap
     * @return
     */
    List<TApplicationMntConfigVo> queryApplicationConfigPage(Map<String, Object> paramMap);

    /**
     * 通过应用id查寻
     *
     * @param applicationId
     * @return
     */
    TApplicationMntConfig queryByApplicationId(@Param("applicationId") String applicationId);

    /**
     * 通过applicationName 查询
     *
     * @param applicationName
     * @return
     */
    TApplicationMntConfig queryByApplicationName(@Param("applicationName") String applicationName);

    TApplicationMntConfig queryByApplicationNameAndUserId(@Param("applicationName") String applicationName,
        @Param("userId") Long userId);

    /**
     * 通过应用id 批量查询
     *
     * @param applicationIdList
     * @return
     */
    List<TApplicationMntConfig> queryByApplicationIdList(@Param("applicationIdList") List<String> applicationIdList);

    /**
     * 批量更新 作弊检查
     *
     * @param applicationIdList
     * @return
     */
    int updateCheatRuleByApplicationIdList(@Param("applicationIdList") List<String> applicationIdList,
        @Param("cheatCheck") int cheatCheck);

    /**
     * 批量插入
     *
     * @param recordList
     * @return
     */
    int insertList(@Param("recordList") List<TApplicationMntConfig> recordList);
}
