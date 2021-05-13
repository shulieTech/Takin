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

import com.pamirs.tro.entity.domain.vo.BasicLinkApplicationDetection;
import com.pamirs.tro.entity.domain.vo.TLinkApplicationInterface;
import com.pamirs.tro.entity.domain.vo.TLinkDetection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 链路检测dao层
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface TLinkDetectionDao {

    /**
     * 说明: 插入到链路检测表中
     *
     * @param map 实体类
     * @author shulie
     */
    void insertLinkDetection(Map<String, Object> map);

    /**
     * 说明: 更新链路监控表的链路信息
     *
     * @param linkDetectionInfoLists 链路检测信息集合
     * @author shulie
     */
    void updateLinkDetection(@Param("linkDetectionInfoLists") List<TLinkApplicationInterface> linkDetectionInfoLists);

    /**
     * 说明: 查询压测检测接口(包含异常检测)
     * @author shulie
     * @param applicationId 应用id
     * @return 检测列表信息
     */
    /**
     * 说明: 查询压测检测接口(包含异常检测)
     *
     * @param paramMap 包含链路名称和应用名称
     * @return 检测列表
     * @author shulie
     */
    List<TLinkDetection> queryChecklist(Map<String, Object> paramMap);

    /**
     * 说明: 更新影子库整体同步检测状态
     *
     * @param applicationId         应用id
     * @param shadowLibErrorContent 影子库检测失败内容
     * @param shadowLibResultStatus 影子库检测结果
     * @param applicationId         应用id
     * @author shulie
     */
    void updateShadowLibResult(@Param("shadowLibErrorContent") String shadowLibErrorContent,
        @Param("shadowLibResultStatus") String shadowLibResultStatus,
        @Param("applicationId") String applicationId);

    /**
     * 说明: 白名单检测异常更新异常内容
     *
     * @param wlistErrorContent 白名单检测错误内容
     * @param wlistResultStatus 白名单检测结果
     * @param applicationId     应用id
     * @author shulie
     */
    void updateWlistErrorContent(@Param("wlistErrorContent") String wlistErrorContent,
        @Param("wlistResultStatus") String wlistResultStatus,
        @Param("applicationId") String applicationId
    );

    /**
     * 说明: 更新缓存预热检测结果
     *
     * @param cacheErrorContent 缓存预热检测错误内容
     * @param cacheResultStatus 缓存预热检测结果
     * @param applicationId     应用id
     * @author shulie
     */
    void updateCacheResult(@Param("cacheErrorContent") String cacheErrorContent,
        @Param("cacheResultStatus") String cacheResultStatus,
        @Param("applicationId") String applicationId
    );

    /**
     * 说明: 根据应用id查询服务名称和类型
     *
     * @param applicationId 应用id
     * @return 白名单列
     * @author shulie
     */
    List<Map<String, Object>> queryWLisByApplicationId(@Param("applicationId") String applicationId);

    /**
     * 说明: 批量删除链路检测表信息
     *
     * @param applicationIdLists 多个应用id
     * @author shulie
     */
    void deleteApplicationToLinkDetection(@Param("applicationIdLists") List<String> applicationIdLists);

    /**
     * 说明: 根据应用id列表批量查询数据检测信息
     *
     * @param applicationIds 应用id集合
     * @return 链路检测列表
     * @author shulie
     * @date 2018/11/5 10:44
     */
    List<Map<String, Object>> queryLinkDetectionListByIds(@Param("applicationIds") List<String> applicationIds);

    /**
     * 说明: 校验压测是否可以开启
     *
     * @param applicationId 应用id
     * @return 压测检测结果
     * @author shulie
     */
    Map<String, Object> pressureTestCheck(@Param("applicationId") String applicationId);

    /**
     * 说明: 根据基础链路名称查询压测检测结果
     *
     * @param basicLinkName 基础链路名称
     * @return 基础链路名称查询压测检测结果
     * @author shulie
     * @date 2018/6/18 18:27
     */
    List<BasicLinkApplicationDetection> queryPMCheckByBasicLinkName(@Param("linkId") String linkId);

    /**
     * 说明: 压测检测调试开关
     *
     * @param paramMap 应用id集合和开关状态
     * @author shulie
     * @date 2018/7/4 19:35
     */
    void debugSwitchUpdateByCheck(Map<String, Object> paramMap);
}
