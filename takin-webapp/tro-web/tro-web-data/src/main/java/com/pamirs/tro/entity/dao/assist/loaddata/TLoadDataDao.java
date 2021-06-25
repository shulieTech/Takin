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

package com.pamirs.tro.entity.dao.assist.loaddata;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TAbstractData;
import com.pamirs.tro.entity.domain.query.TConf;
import com.pamirs.tro.entity.domain.query.TDBAbstractData;
import com.pamirs.tro.entity.domain.vo.TDBconfVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明:
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/8/31 15:56
 */
@Mapper
public interface TLoadDataDao {

    /**
     * 说明: 批量查询抽数配置信息
     *
     * @param tdcIdList 抽数id集合
     * @return 抽数配置集合
     * @author shulie
     * @date 2018/8/31 18:37
     */
    List<TConf> queryDataBaseConf(@Param("tadIdList") List<String> tdcIdList);

    /**
     * 说明: 批量更新抽数状态
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/8/31 18:37
     */
    void updateLoadDataStatus(@Param("tadId") String tadId, @Param("loadStatus") String loadStatus);

    /**
     * 批量启动或禁用
     *
     * @param tadIdList
     */
    void updatebatchStartOrStop(@Param("tadIdList") List<String> tadIdList, @Param("startOrStop") Integer startOrStop);

    /**
     * 删除数据库配置，逻辑删除，体现在DB_STATUS的状态，0表示删除，1表示使用。
     *
     * @param tdcId
     */
    void deleteDBConf(String tdcId);

    /**
     * 查询
     *
     * @param tdcId
     * @return
     */
    TDBAbstractData selectDBConfById(@Param("tdcId") String tdcId);

    /**
     * 查询数据库配置列表
     *
     * @param paramMap
     * @return
     */
    List<TDBconfVo> selectDBConfList(Map<String, Object> paramMap);

    /**
     * 说明: 根据抽数id查询场景id
     *
     * @param tdcId 抽数id
     * @return 抽数场景id
     * @author shulie
     * @date 2018/9/10 19:38
     */
    Map<String, Object> selectSecineId(@Param("tdcId") String tdcId);

    /**
     * 更新数据库配置
     *
     * @param tdbAbstractData
     * @return
     */
    int updateDBConf(TDBAbstractData tdbAbstractData);

    /**
     * 该方法未使用
     */
    @Deprecated
    void deleteTAbstractDataByIds(@Param("tadIdsList") List<String> tadIdsList);

    int updateDatabaseConfExist(@Param("tdcId") String tdcId);

    void addTAbstractData(@Param("saveAbstractDataLists") List<TAbstractData> saveAbstractDataLists);

    void updateTAbstractData(TAbstractData tAbstractData);

    int updatedatabaseConfAbstractDataExist(@Param("tdcId") String tdcId, @Param("tadId") String tadId);

    /**
     * 批量启用
     *
     * @param tdcIdList
     */
    void batchStart(@Param("tdcIdList") List<String> tdcIdList);

    /**
     * 批量禁用
     */
    void batchClose(@Param("tdcIdList") List<String> tdcIdList);

    /**
     * 修改单个启用
     *
     * @param tdcId
     */
    void startSingleDBConfById(Long tdcId);

    /**
     * 修改单个禁用
     *
     * @param tdcId
     */
    void closeSingleDBConfById(Long tdcId);

    /**
     * 说明：批量插入数据到关系型数据库
     *
     * @param maps
     * @author shulie
     * @time：2017年12月21日 下午3:27:03
     */
    void dataInsertToRDS(Map<String, Object> maps);

    /**
     * 逻辑删除数据库表配置
     *
     * @param tadIds
     */
    void deleteAbstractData(@Param("tadIds") List<String> tadIds);

    /**
     * 获取数据库表配置
     *
     * @param tdcId 数据库配置id
     * @return
     */
    List<TAbstractData> queryAbstractDataInfoByTdcId(@Param("tdcId") String tdcId);

    /**
     * 获取TDCIDS列表
     *
     * @param tadIds
     * @return
     */
    List<String> queryTdcIds(@Param("tadIds") List<String> tadIds);

    /**
     * 插入
     *
     * @param tDBAbstractData
     * @return
     */
    void insertTDBConf(TDBAbstractData tDBAbstractData);

    /**
     * 插入到t_abstract_data
     *
     * @param tAbstractDataLists
     * @return
     */

    void insertTAbstractData(@Param("tAbstractDataLists") List<TAbstractData> tAbstractDataLists);

    /**
     * 获取基础链路信息
     *
     * @return
     */
    List<Map<String, String>> queryBasicLink();

    /**
     * 说明:
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/9/11 18:49
     */
    void dropTable(@Param("tableName") String tableName);

    /**
     * 根据tdcId获取tadId列表
     *
     * @param tdcId
     * @return
     */
    List<Long> selectTadIdListByTdcIdAndStatus(@Param("tdcId") long tdcId, @Param("dbStatus") String dbStatus);

    /**
     * 查询数据表的抽数状态
     *
     * @param tadIds 数据表id
     * @return
     */
    List<Map<String, String>> queryLoadstatus(@Param("tadIds") List<String> tadIds);
}
