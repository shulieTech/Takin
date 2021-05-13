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

package io.shulie.tro.web.app.service.simplify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.pamirs.tro.common.constant.JobEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.simplify.TShadowJobConfigMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.ShadowJobConfigQuery;
import com.pamirs.tro.entity.domain.vo.ShadowJobConfigVo;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import io.shulie.tro.web.app.service.user.TroWebUserService;
import io.shulie.tro.web.app.utils.Estimate;
import io.shulie.tro.web.app.utils.XmlUtil;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationShadowJobDAO;
import io.shulie.tro.web.data.param.application.ShadowJobCreateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 影子JOB配置
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.web.api.service.simplify
 * @Date 2020-03-17 15:44
 */
@Slf4j
@Service
public class ShadowJobConfigService {

    @Autowired
    TroAuthService troAuthService;
    @Autowired
    TroWebUserService troWebUserService;
    @Resource
    private TShadowJobConfigMapper tShadowJobConfigMapper;
    @Resource
    private TApplicationMntDao tApplicationMntDao;
    @Resource
    private TUserMapper TUserMapper;
    @Autowired
    private ConfigSyncService configSyncService;
    @Autowired
    private ApplicationShadowJobDAO applicationShadowJobDAO;
    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;
    @Autowired
    private ApplicationDAO applicationDAO;

    public static void main(String[] args) throws DocumentException {
        String text = "<xml>\n" +
            "    <className>com.pradar.xx.TestJob</className>\n" +
            "    <cron>0 * * * * *</cron>\n" +
            "    <jobType>elastic-job</jobType>\n" +
            "    <jobDataType>simple</jobDataType>\n" +
            "    <listener>com.pradar.listener.TestListener</listener>\n" +
            "</xml>";
        Map<String, String> stringStringMap = XmlUtil.readStringXml(text);
        System.out.println(stringStringMap);
    }

    public Response insert(TShadowJobConfig tShadowJobConfig) throws DocumentException {
        Map<String, String> xmlMap = XmlUtil.readStringXml(tShadowJobConfig.getConfigCode());
        String className = xmlMap.get("className");
        String jobType = xmlMap.get("jobType");
        Estimate.notBlank(className, "className不能为空");
        Estimate.notBlank(jobType, "job类型不能为空");

        tShadowJobConfig.setName(className);
        tShadowJobConfig.setType(JobEnum.getJobByText(jobType).ordinal());
        if (null != tShadowJobConfig.getId()) {
            return Response.fail("ID必须为空");
        }
        //        tShadowJobConfigMapper.insert(tShadowJobConfig);

        ShadowJobCreateParam shadowJobCreateParam = new ShadowJobCreateParam();
        BeanUtils.copyProperties(tShadowJobConfig, shadowJobCreateParam);
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(tShadowJobConfig.getApplicationId());
        shadowJobCreateParam.setCustomerId(applicationDetailResult.getCustomerId());
        shadowJobCreateParam.setUserId(applicationDetailResult.getUserId());
        applicationShadowJobDAO.insert(shadowJobCreateParam);
        User user = RestContext.getUser();
        configSyncService.syncShadowJob(user.getKey(), tShadowJobConfig.getApplicationId(), null);
        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(
            tShadowJobConfig.getApplicationId());
        if (null == tApplicationMnt) {
            return Response.fail("未查询到相关应用信息");
        }
        agentConfigCacheManager.evictShadowJobs(tApplicationMnt.getApplicationName());
        return Response.success();
    }

    public Response queryDetail(Long id) {
        TShadowJobConfig shadowJobConfig = tShadowJobConfigMapper.selectOneById(id);
        if (shadowJobConfig == null) {
            return Response.fail("未查询到相关数据");
        }
        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(
            shadowJobConfig.getApplicationId());
        if (null == tApplicationMnt) {
            return Response.fail("未查询到相关应用信息");
        }
        return Response.success(new ShadowJobConfigVo(shadowJobConfig, tApplicationMnt.getApplicationName()));
    }

    public Response update(ShadowJobConfigQuery query) throws DocumentException {
        TShadowJobConfig updateShadowJobConfig = new TShadowJobConfig();
        TShadowJobConfig shadowJobConfig = tShadowJobConfigMapper.selectOneById(query.getId());
        if (null == shadowJobConfig) {
            return Response.success("根据ID未查询到相关数据");
        }
        if (StringUtils.isNotBlank(query.getConfigCode())) {

            if (null != query.getConfigCode() && !query.getConfigCode().equals(shadowJobConfig.getConfigCode())) {
                updateShadowJobConfig.setConfigCode(query.getConfigCode());
                Map<String, String> xmlMap = XmlUtil.readStringXml(query.getConfigCode());
                String className = xmlMap.get("className");
                String jobType = xmlMap.get("jobType");
                JobEnum jobText = JobEnum.getJobByText(jobType);

                if (StringUtils.isNotBlank(className) && !className.equals(shadowJobConfig.getName())) {
                    updateShadowJobConfig.setName(className);
                }

                if (null != jobText && !Integer.valueOf(jobText.ordinal()).equals(shadowJobConfig.getType())) {
                    updateShadowJobConfig.setType(jobText.ordinal());
                }
            }
        }
        updateShadowJobConfig.setId(query.getId());
        if (null != query.getStatus() && !query.getStatus().equals(shadowJobConfig.getStatus())) {
            updateShadowJobConfig.setStatus(query.getStatus());
        }
        if (null != query.getActive() && !query.getActive().equals(shadowJobConfig.getActive())) {
            updateShadowJobConfig.setActive(query.getActive());
        }



        if (StringUtils.isNotBlank(updateShadowJobConfig.getConfigCode()) || null != query.getStatus()
            || null != query.getActive()) {
            tShadowJobConfigMapper.update(updateShadowJobConfig);
        }
        // 仅仅更新备注字段
        if(query.getRemark() != null) {
            TShadowJobConfig updateRemark = new TShadowJobConfig();
            updateRemark.setId(query.getId());
            updateRemark.setRemark(query.getRemark());
            tShadowJobConfigMapper.update(updateRemark);
        }

        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(
            shadowJobConfig.getApplicationId());
        if (null == tApplicationMnt) {
            return Response.fail("未查询到相关应用信息");
        }
        User user = RestContext.getUser();
        configSyncService.syncShadowJob(user.getKey(), shadowJobConfig.getApplicationId(), null);
        agentConfigCacheManager.evictShadowJobs(tApplicationMnt.getApplicationName());
        return Response.success();
    }

    public Response delete(Long id) {
        TShadowJobConfig shadowJobConfig = tShadowJobConfigMapper.selectOneById(id);
        if (null == shadowJobConfig) {
            return Response.fail("根据ID未查询到相关信息");
        }
        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(
            shadowJobConfig.getApplicationId());
        if (null == tApplicationMnt) {
            return Response.fail("未查询到相关应用信息");
        }
        tShadowJobConfigMapper.delete(id);
        User user = RestContext.getUser();
        configSyncService.syncShadowJob(user.getKey(), shadowJobConfig.getApplicationId(), null);
        agentConfigCacheManager.evictShadowJobs(tApplicationMnt.getApplicationName());
        return Response.success();
    }

    public Response queryByPage(ShadowJobConfigQuery query) {
        //PageHelper.startPage(query.getPageNum(), query.getPageSize());
        if (StringUtils.isBlank(query.getOrderBy())) {
            query.setOrderBy("id desc");
        }
        List<TShadowJobConfig> tShadowJobConfigs = tShadowJobConfigMapper.selectList(query);
        PageInfo<TShadowJobConfig> pageInfo = new PageInfo<>(tShadowJobConfigs);
        List<ShadowJobConfigVo> configVos = new ArrayList<>();
        pageInfo.getList().forEach(tShadowJobConfig -> {
            ShadowJobConfigVo vo = new ShadowJobConfigVo();
            vo.setId(String.valueOf(tShadowJobConfig.getId()));
            vo.setApplicationId(String.valueOf(tShadowJobConfig.getApplicationId()));
            vo.setName(tShadowJobConfig.getName());
            vo.setType(tShadowJobConfig.getType());
            vo.setStatus(tShadowJobConfig.getStatus());
            vo.setConfigCode(tShadowJobConfig.getConfigCode());
            vo.setTypeName(JobEnum.getJobByIndex(tShadowJobConfig.getType()).getText());
            vo.setActive(tShadowJobConfig.getActive());
            vo.setActive(tShadowJobConfig.getActive());
            vo.setUpdateTime(tShadowJobConfig.getUpdateTime());
            vo.setRemark(tShadowJobConfig.getRemark());
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                vo.setCanEdit(allowUpdateUserIdList.contains(tShadowJobConfig.getUserId()));
            }
            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                vo.setCanRemove(allowDeleteUserIdList.contains(tShadowJobConfig.getUserId()));
            }
            List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
                vo.setCanEnableDisable(allowEnableDisableUserIdList.contains(tShadowJobConfig.getUserId()));
            }
            configVos.add(vo);
        });
        return Response.success(configVos, pageInfo.getTotal());
    }

    public List<TShadowJobConfig> queryByAppName(String appName) {
        Estimate.notBlank(appName, "应用名称不能为空");
        User user = RestContext.getUser();
        if (user == null) {
            String userAppKey = RestContext.getTenantUserKey();
            user = TUserMapper.selectByKey(userAppKey);
        }
        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationInfoByNameAndTenant(appName,
            user == null ? null : user.getId());
        if (null == tApplicationMnt) {
            throw new RuntimeException("未查询到应用相关数据");
        }
        ShadowJobConfigQuery query = new ShadowJobConfigQuery();
        query.setApplicationId(tApplicationMnt.getApplicationId());
        List<TShadowJobConfig> tShadowJobConfigs = tShadowJobConfigMapper.selectList(query);
        try {
            for (TShadowJobConfig tShadowJobConfig : tShadowJobConfigs) {
                Map<String, String> stringStringMap = XmlUtil.readStringXml(tShadowJobConfig.getConfigCode());
                tShadowJobConfig.setConfigCode(JSONObject.toJSONString(stringStringMap));
            }
        } catch (Exception e) {
            throw new RuntimeException("dom 解析发生异常");
        }
        return tShadowJobConfigs;
    }

    public List<TShadowJobConfig> getAllEnableShadowJobs(long applicationId) {
        return tShadowJobConfigMapper.getAllEnableShadowJobs(applicationId);
    }
}
