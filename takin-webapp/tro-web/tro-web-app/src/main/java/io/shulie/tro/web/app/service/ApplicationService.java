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

package io.shulie.tro.web.app.service;

import com.pamirs.tro.entity.domain.dto.ApplicationSwitchStatusDTO;
import com.pamirs.tro.entity.domain.dto.NodeUploadDataDTO;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.query.ApplicationQueryParam;
import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import com.pamirs.tro.entity.domain.vo.JarVersionVo;
import io.shulie.tro.web.app.common.Response;
import org.springframework.web.multipart.MultipartFile;
import io.shulie.tro.web.app.controller.openapi.response.application.ApplicationListResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-03-16 15:23
 * @Description:
 */
public interface ApplicationService {

   List<ApplicationListResponse> getApplicationList(String appNames);


    /**
     * 获取应用列表
     *
     * @param param
     * @return
     */
    Response<List<ApplicationVo>> getApplicationList(ApplicationQueryParam param);

    List<ApplicationVo> getApplicationListVo(ApplicationQueryParam param);

    /**
     * 添加接入状态进行过滤
     *
     * @param param
     * @param accessStatus
     * @return
     */
    Response getApplicationList(ApplicationQueryParam param, Integer accessStatus);

    /**
     * 应用列表（全量应用列表，无鉴权）
     *
     * @return
     */
    Response getApplicationList();

    /**
     * 获取应用信息
     *
     * @param appId
     * @return
     */
    Response getApplicationInfo(String appId);

    Response getApplicationInfoForError(String appId);

    /**
     * 添加应用(控制台手动新增)
     *
     * @param param
     * @return
     */
    Response addApplication(ApplicationVo param);

    /**
     * 添加应用(agent注册)
     *
     * @param param
     * @return
     */
    Response addAgentRegisteApplication(ApplicationVo param);

    /**
     * 添加应用
     *
     * @param param
     * @return
     */
    Response modifyApplication(ApplicationVo param);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    Response deleteApplication(String appId);

    /**
     * 项目压测开关
     *
     * @return
     */
    Response userAppPressureSwitch(Long uid, Boolean enable);

    /**
     * 重新计算
     *
     * @param uid
     * @return
     */
    Response calculateUserSwitch(Long uid);

    Response userAppSwitchInfo();

    ApplicationSwitchStatusDTO agentGetUserSwitchInfo();

    /**
     * 上传接入状态
     *
     * @param param
     * @return
     */
    Response uploadAccessStatus(NodeUploadDataDTO param);

    /**
     * 上传应用中间件信息
     *
     * @param requestMap
     * @return
     */
    Response uploadMiddleware(Map<String, String> requestMap);

    /**
     * 上传应用中间件增强状态
     *
     * @param requestMap
     * @return
     */
    Response uploadMiddlewareStatus(Map<String, JarVersionVo> requestMap, String appName);

    /**
     * 修改应用状态（需鉴权）
     *
     * @param id
     * @param accessStatus
     * @param exceptionInfo
     */
    void modifyAccessStatus(String id, Integer accessStatus, String exceptionInfo);

    /**
     * 修改应用状态（无需鉴权）
     *
     * @param applicationIds
     * @param accessStatus
     */
    void modifyAccessStatusWithoutAuth(List<Long> applicationIds, Integer accessStatus);

    List<TApplicationMnt> getAllApplications();

    List<TApplicationMnt> getApplicationsByUserIdList(List<Long> userIdList);

    String getIdByName(String applicationName);

    String getUserSwitchStatusForVo(Long uid);

    List<String> getApplicationName();

    /**
     * 应用管理相关配置导出
     * 影子库/表, 挡板, job, 白名单, 影子消费知
     *
     * @param response 响应
     * @param applicationId 应用id
     */
    void exportApplicationConfig(HttpServletResponse response, Long applicationId);

    /**
     * 导入应用配置
     *
     * @param file 导入的 excel
     * @param applicationId 应用id
     * @return 错误信息
     */
    Response importApplicationConfig(MultipartFile file, Long applicationId) ;

    Response<String> buildExportDownLoadConfigUrl(String appId, HttpServletRequest request);

    Response<Boolean> appDsConfigIsNewVersion();


}
