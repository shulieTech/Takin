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

package io.shulie.tro.web.app.service.linkManage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.apimanage.TApplicationApiManageMapper;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.EnumResult;
import com.pamirs.tro.entity.domain.entity.ApplicationApiManage;
import com.pamirs.tro.entity.domain.vo.entracemanage.ApiCreateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.ApiUpdateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.EntranceApiVo;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.controller.linkmanage.DictionaryCache;
import io.shulie.tro.web.app.utils.PageUtils;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.common.vo.application.ApplicationApiManageVO;
import io.shulie.tro.web.data.dao.application.ApplicationApiDAO;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.param.application.ApplicationApiCreateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: vernon
 * @Date: 2020/4/2 13:11
 * @Description:
 */
@Component
public class ApplicationApiServiveImpl implements ApplicationApiService {
    private static final String EMPTY = " ";
    private static final String HTTP_METHOD_TYPE = "HTTP_METHOD_TYPE";
    @Resource
    private TApplicationApiManageMapper manageMapper;

    @Autowired
    private ApplicationApiDAO applicationApiDAO;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Override
    public Response registerApi(Map<String, List<String>> register) {
        List<ApplicationApiManage> batch = Lists.newArrayList();
        try {
            for (Map.Entry entry : register.entrySet()) {

                String appName = String.valueOf(entry.getKey());

                List<String> apis = (List<String>)entry.getValue();

                if (StringUtils.isBlank(appName) || CollectionUtils.isEmpty(apis)) {
                    continue;
                }

                ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationByCustomerIdAndName(
                    RestContext.getCustomerId(), appName);
                apis.stream().forEach(api -> {

                    String[] str = api.split("#");
                    String requestMethod = str[1];
                    if ("[]".equals(requestMethod)) {
                        //
                        requestMethod = EMPTY;
                    } else {
                        requestMethod = requestMethod.substring(1, requestMethod.length() - 1);
                    }
                    api = str[0];
                    if (api.contains("||")) {
                        String[] splits = api.split("\\|\\|");
                        for (String split : splits) {
                            ApplicationApiManage manage = new ApplicationApiManage();
                            manage.setApi(split.trim());
                            manage.setApplicationName(appName);
                            manage.setIsDeleted((byte)0);
                            manage.setCreateTime(new Date());
                            manage.setUpdateTime(new Date());
                            manage.setRequestMethod(requestMethod);
                            manage.setApplicationId(applicationDetailResult.getApplicationId());
                            manage.setCustomerId(applicationDetailResult.getCustomerId());
                            manage.setUserId(applicationDetailResult.getUserId());
                            manage.setIsAgentRegiste(1);
                            batch.add(manage);
                        }

                    } else {
                        ApplicationApiManage manage = new ApplicationApiManage();
                        manage.setApi(api.trim());
                        manage.setApplicationName(appName);
                        manage.setIsDeleted((byte)0);
                        manage.setCreateTime(new Date());
                        manage.setRequestMethod(requestMethod);
                        manage.setUpdateTime(new Date());
                        manage.setApplicationId(applicationDetailResult.getApplicationId());
                        manage.setCustomerId(applicationDetailResult.getCustomerId());
                        manage.setUserId(applicationDetailResult.getUserId());
                        manage.setIsAgentRegiste(1);
                        batch.add(manage);
                    }

                });

                // 旧的删除了，新的再添加
                manageMapper.deleteByAppName(appName);

                manageMapper.insertBatch(batch);
            }
        } catch (Exception e) {
            batch.stream().forEach(single -> {
                try {
                    ApplicationApiManage manage = new ApplicationApiManage();
                    manage.setRequestMethod(single.getRequestMethod());
                    manage.setApi(single.getApi());
                    manage.setApplicationName(single.getApplicationName());
                    manage.setIsDeleted(single.getIsDeleted());
                    manage.setUpdateTime(single.getUpdateTime());
                    manage.setCreateTime(single.getCreateTime());
                    manage.setApplicationId(single.getApplicationId());
                    manage.setCustomerId(single.getCustomerId());
                    manage.setUserId(single.getUserId());
                    manage.setIsAgentRegiste(single.getIsAgentRegiste());
                    manageMapper.insertSelective(manage);
                } catch (Exception e1) {
                    //ignore
                }
            });
        }
        return Response.success();
    }

    @Override
    public Response pullApi(String appName) {
        List<ApplicationApiManage> all = manageMapper.querySimple(appName);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(all)) {
            return Response.success(new HashMap<>());
        }
        Map<String, List<String>> res = new HashMap<>();
        for (ApplicationApiManage applicationApiManage : all) {
            res.computeIfAbsent(applicationApiManage.getApplicationName(), k -> new ArrayList<>()).add(
                applicationApiManage.getApi()
                    + "#" + applicationApiManage.getRequestMethod());
        }
        return Response.success(res);
    }

    @Override
    public Response delete(String id) {
        manageMapper.deleteByPrimaryKey(Long.parseLong(id));
        return Response.success();
    }

    @Override
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response query(EntranceApiVo vo) {
        ApplicationApiManage manage = new ApplicationApiManage();
        manage.setApplicationName(vo.getApplicationName());
        manage.setApi(vo.getApi());

        List<ApplicationApiManage> reocords = manageMapper.selectBySelective(manage);

        reocords.sort(Comparator.comparing(ApplicationApiManage::getCreateTime).reversed());
        List<ApplicationApiManage> pageData =
            PageUtils.getPage(true, vo.getCurrentPage(), vo.getPageSize(), reocords);
        List<ApplicationApiManageVO> dtos = new ArrayList<>();
        pageData.stream().forEach(data -> {
            ApplicationApiManageVO dto = new ApplicationApiManageVO();
            BeanUtils.copyProperties(data, dto);
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                dto.setCanEdit(allowUpdateUserIdList.contains(data.getUserId()));
            }
            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                dto.setCanRemove(allowDeleteUserIdList.contains(data.getUserId()));
            }
            dtos.add(dto);
        });
        /*  PageInfo<ApplicationApiManage> pageInfo = new PageInfo<>(pageData);*/
        return Response.success(dtos, reocords.size());

    }

    @Override
    public Response update(ApiUpdateVo vo) {
        if (Objects.isNull(vo.getId())) {
            return Response.fail("0", "主键为空");
        }
        ApplicationApiManage applicationApiManage = manageMapper.selectByPrimaryKey(Long.parseLong(vo.getId()));
        if (null == applicationApiManage) {
            return Response.fail("0", "该规则不存在");
        }
        String applicationName = Optional.ofNullable(vo.getApplicationName()).orElse(
            applicationApiManage.getApplicationName());
        if (StringUtils.isNotBlank(vo.getApi()) && !vo.getApi().equals(applicationApiManage.getApi())) {
            OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION_NAME,
                applicationName + "，入口地址：" + vo.getApi());
        } else {
            OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION_NAME, applicationName);
        }
        ApplicationApiManage manage = new ApplicationApiManage();
        manage.setId(Long.parseLong(vo.getId()));
        manage.setApplicationName(vo.getApplicationName());
        manage.setApi(vo.getApi());
        manage.setRequestMethod(
            DictionaryCache.getObjectByParam(HTTP_METHOD_TYPE, Integer.parseInt(vo.getMethod())).getLabel());
        manage.setUpdateTime(new Date());
        manageMapper.updateByPrimaryKeySelective(manage);
        return Response.success();
    }

    @Override
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
        needAuth = ActionTypeEnum.CREATE
    )
    public Response create(ApiCreateVo vo) {
        ApplicationApiCreateParam createParam = new ApplicationApiCreateParam();
        //前端给的是字典中的枚举数据
        createParam.setRequestMethod(
            DictionaryCache.getObjectByParam(HTTP_METHOD_TYPE, Integer.parseInt(vo.getMethod())).getLabel());
        createParam.setApi(vo.getApi());
        createParam.setApplicationName(vo.getApplicationName());
        createParam.setIsDeleted((byte)0);
        createParam.setUpdateTime(new Date());
        createParam.setCreateTime(new Date());
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationByCustomerIdAndName(
            RestContext.getCustomerId(), vo.getApplicationName());
        //4.8.0.4以后入口规则的所属用户跟着应用走
        createParam.setCustomerId(applicationDetailResult.getCustomerId());
        createParam.setUserId(applicationDetailResult.getUserId());
        applicationApiDAO.insert(createParam);
        return Response.success();
    }

    @Override
    public Response queryDetail(String id) {
        if (Objects.isNull(id)) {
            return Response.fail("0", "id 不能为空");
        }
        ApplicationApiManageVO dto = new ApplicationApiManageVO();
        ApplicationApiManage manage = manageMapper.selectByPrimaryKey(Long.parseLong(id));
        if (Objects.nonNull(manage)) {
            BeanUtils.copyProperties(manage, dto);
        }
        if (manage != null && StringUtils.isNotBlank(manage.getRequestMethod())) {
            EnumResult objectByParam = DictionaryCache.getObjectByParamByLabel(HTTP_METHOD_TYPE,
                manage.getRequestMethod());
            if (objectByParam != null) {
                dto.setRequestMethod(objectByParam.getValue());
            }
        }
        return Response.success(dto);
    }
}
