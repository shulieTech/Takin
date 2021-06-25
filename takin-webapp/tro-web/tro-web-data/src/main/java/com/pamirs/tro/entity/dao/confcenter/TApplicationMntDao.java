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

package com.pamirs.tro.entity.dao.confcenter;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.annocation.DataAuth;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.vo.TLinkApplicationInterface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 应用管理dao层
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface TApplicationMntDao {

    /**
     * 说明: 校验该应用是否已经存在
     *
     * @param applicationName
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    public int applicationExist(@Param("applicationName") String applicationName);

    int applicationExistByAppName(@Param("applicationName") String applicationName);

    /**
     * 判断同一租户下应用名称是否重复
     *
     * @param customerId
     * @param applicationName
     * @return
     */
    int applicationExistByCustomerIdAndAppName(@Param("customerId") Long customerId, @Param("applicationName") String applicationName);

    /**
     * 说明: 添加应用接口
     *
     * @param tApplicationMnt 应用对象
     * @author shulie
     */
    public void addApplication(TApplicationMnt tApplicationMnt);

    /**
     * 说明: 查询应用信息接口
     *
     * @param linkName        链路名称
     * @param applicationName 应用名称
     * @return 链路应用服务信息列表
     * @author shulie
     */
    public List<TLinkApplicationInterface> queryApplicationinfo(@Param("linkName") String linkName,
        @Param("applicationName") String applicationName);

    /**
     * 说明: 当链路应用服务信息列表查询不到时,仅仅只按照应用名称查询应用信息
     *
     * @param applicationName 应用名称
     * @return 应用列表
     * @author shulie
     */
    @DataAuth
    public List<TApplicationMnt> queryApplicationList(@Param("applicationName") String applicationName,
        @Param("applicationIds") List<String> applicationIds);

    /**
     * 说明: 根据id列表批量查询应用和白名单信息
     *
     * @param applicationIds 应用id集合
     * @return 应用数据
     * @author shulie
     * @date 2018/11/5 10:30
     */
    List<Map<String, Object>> queryApplicationListByIds(@Param("applicationIds") List<String> applicationIds);

    /**
     * 说明 根据ID列表批量查询应用信息
     */
    List<TApplicationMnt> queryApplicationMntListByIds(List<Long> applicationIds);

    /**
     * 说明: 根据应用id查询应用信息接口
     *
     * @param applicationId 应用id
     * @return 应用对象
     * @author shulie
     */
    public TApplicationMnt queryApplicationinfoById(@Param("applicationId") long applicationId);

    public TApplicationMnt queryApplicationinfoByIdAndRole(@Param("applicationId") long applicationId,
        @Param("role") Integer role, @Param("userId") Integer userId);

    /**
     * 说明: 根据应用id查询应用信息接口
     *
     * @param applicationId 应用id
     * @return 应用对象
     * @author JasonYan
     */
    public TApplicationMnt queryApplicationinfoByIdAndTenant(@Param("applicationId") long applicationId,
        @Param("userId") long userId);

    /**
     * 说明: 删除应用信息接口
     *
     * @param applicationIdLists 应用id集合
     * @author shulie
     */
    public void deleteApplicationinfoByIds(@Param("applicationIdLists") List<String> applicationIdLists);

    /**
     * 说明: 查询应用下拉框数据接口
     *
     * @return 应用列表
     * @author shulie
     */
    public List<Map<String, Object>> queryApplicationdata();

    /**
     * 说明: 根据应用id更新应用信息（需要验证权限）
     *
     * @param tApplicationMnt 应用实体类
     * @author shulie
     */
    public void updateApplicationinfo(TApplicationMnt tApplicationMnt);

    /**
     * 说明: 修改应用状态（系统内部使用，不需要验证权限）
     *
     * @param applicationIds 应用id列表
     * @author shulie
     */
    public void updateApplicationStatus(@Param("applicationIds") List<Long> applicationIds,
        @Param("accessStatus") Integer accessStatus);

    /**
     * 说明: 查询缓存失效时间
     *
     * @param applicationId 应用id
     * @return 缓存失效时间
     * @author shulie
     */
    public Map<String, Object> queryCacheExpTime(@Param("applicationId") String applicationId);

    /**
     * 说明: 根据应用id和脚本类型查询脚本路径
     *
     * @param applicationId 应用id
     * @param scriptType    脚本类型
     * @return 脚本路径
     * @author shulie
     */
    String selectScriptPath(@Param("applicationId") String applicationId,
        @Param("scriptType") String scriptType);

    /**
     * 说明: 根据应用id查询应用名称
     *
     * @param applicationId 应用id
     * @return 应用名称
     * @author shulie
     */
    public String selectApplicationName(@Param("applicationId") String applicationId);

    /**
     * 根据二级链路ID找应用
     *
     * @param secondLinkId
     * @return
     */
    List<TApplicationMnt> queryApplicationBySecondLinkId(@Param("secondLinkId") Long secondLinkId);

    List<TApplicationMnt> queryApplicationByTenant(@Param("uid") Long uid);

    /**
     * 说明: 根据应用id查询应用名称
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/6/16 18:07
     */
    List<String> queryApplicationName(@Param("applicationIdLists") List<String> applicationIdLists);

    /**
     * 说明: 根据应用id查询关联的基础链路是否存在
     *
     * @param applicationId 应用id
     * @return 关联的基础链路数量和应用名称
     * @author shulie
     * @date 2018/7/10 12:43
     */
    Map<String, Object> queryApplicationRelationBasicLinkByApplicationId(@Param("applicationId") String applicationId);

    /**
     * 说明: 查询应用信息列表(prada同步表查数据)
     *
     * @return
     * @author shulie
     * @date 2019/3/7 9:35
     */
    List<String> queryAppNameList();

    /**
     * 说明: 根据应用名称查询应用信息接口
     *
     * @param applicationName 应用名称
     * @return 应用对象
     * @author shulie
     */
    public TApplicationMnt queryApplicationinfoByName(@Param("applicationName") String applicationName);

    /**
     * 说明: 根据应用名称查询应用信息接口
     *
     * @param applicationName 应用名称
     * @return 应用对象
     * @author JasonYan
     */
    TApplicationMnt queryApplicationinfoByNameTenant(@Param("applicationName") String applicationName,
        @Param("userId") Long userId);

    TApplicationMnt queryApplicationInfoByNameAndTenant(@Param("applicationName") String applicationName,
        @Param("userId") Long userId);

    /**
     * 更新 agentVersion
     *
     * @param applicationId
     * @param agentVersion
     * @param pradarVersion
     */
    public void updateApplicaionAgentVersion(@Param("applicationId") Long applicationId,
        @Param("agentVersion") String agentVersion,
        @Param("pradarVersion") String pradarVersion);

    /**
     * 说明: 根据applicatioName查询agent version
     *
     * @param applicationName 应用名称
     * @return 应用对象
     * @author shulie
     */
    public String queryAgentVersionByApplicationName(@Param("applicationName") String applicationName);

    /**
     * 根据applicationName查询 id
     *
     * @param applicationName
     * @return
     */
    Long queryIdByApplicationName(@Param("applicationName") String applicationName);

    /**
     * 返回id
     * @param names
     * @param userId
     * @return
     */
    List<String> queryIdsByNameAndTenant(@Param("names") List<String> names, @Param("userId") Long userId);


    List<TApplicationMnt> getAllApplications();

    List<TApplicationMnt> getAllApplicationByStatus(@Param("statusList") List<Integer> statusList);

    List<TApplicationMnt> getApplicationsByTenants(@Param("userIdList") List<Long> userIdList);

    String getIdByName(@Param("applicationName") String applicationName);
}
