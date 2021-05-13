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

import com.pamirs.tro.common.constant.AppAccessTypeEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.linkguard.TLinkGuardMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.LinkGuardEntity;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.LinkGuardQueryParam;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import io.shulie.tro.web.app.utils.PageUtils;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.LinkGuardDAO;
import io.shulie.tro.web.data.param.application.LinkGuardCreateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.linkguard.LinkGuardResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 慕白
 * @Date: 2020-03-05 10:42
 * @Description:
 */

@Service
@Slf4j
public class LinkGuardServiceImpl implements LinkGuardService {

    private static String FALSE_CORE = "0";
    @Resource
    private TLinkGuardMapper tLinkGuardMapper;
    @Autowired
    private ApplicationService applicationService;
    @Resource
    private TUserMapper TUserMapper;
    @Resource
    private TApplicationMntDao applicationMntDao;
    @Autowired
    private TroAuthService troAuthService;
    @Autowired
    private ConfigSyncService configSyncService;
    @Autowired
    private LinkGuardDAO linkGuardDAO;
    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Override
    public Response addGuard(LinkGuardVo vo) {
        Long applicationId;
        if (StringUtils.isBlank(vo.getApplicationId())) {
            LinkGuardEntity linkGuardEntity = tLinkGuardMapper.selectById(vo.getId());
            applicationId = linkGuardEntity.getApplicationId();
        } else {
            applicationId = Long.parseLong(vo.getApplicationId());
        }
        if (StringUtils.isBlank(vo.getApplicationName()) || StringUtils.isBlank(vo.getMethodInfo())) {
            return Response.fail(FALSE_CORE, "applicationName和methodInfo不能为空");
        }
        if (!vo.getMethodInfo().contains("#")) {
            return Response.fail(FALSE_CORE, "类名方法名用'#'分割，如Aa#bb");
        }
        LinkGuardQueryParam param = new LinkGuardQueryParam();
        param.setMethodInfo(vo.getMethodInfo());
        if (vo.getApplicationId() != null && !vo.getApplicationId().isEmpty()) {
            param.setAppId(Long.valueOf(vo.getApplicationId()));
        }
        List<LinkGuardEntity> dbList = tLinkGuardMapper.selectByExample(param);
        if (dbList != null && dbList.size() > 0) {
            return Response.fail(FALSE_CORE, "同一个methodInfo只能设置一个挡板");
        }
        LinkGuardCreateParam createParam = new LinkGuardCreateParam();
        createParam.setIsEnable(vo.getIsEnable());
        createParam.setApplicationName(vo.getApplicationName());
        createParam.setMethodInfo(vo.getMethodInfo());
        createParam.setGroovy(vo.getGroovy());
        createParam.setApplicationId(Long.valueOf(vo.getApplicationId()));
        createParam.setRemark(vo.getRemark());
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(applicationId);
        createParam.setCustomerId(applicationDetailResult.getCustomerId());
        createParam.setUserId(applicationDetailResult.getUserId());
        try {
            linkGuardDAO.insert2(createParam);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(FALSE_CORE, "创建挡板失败");
        }
        applicationService.modifyAccessStatus(vo.getApplicationId(), AppAccessTypeEnum.UNUPLOAD.getValue(), null);
        User user = RestContext.getUser();
        configSyncService.syncGuard(user.getKey(), Long.parseLong(vo.getApplicationId()), vo.getApplicationName());
        agentConfigCacheManager.evictGuards(vo.getApplicationName());
        return Response.success();
    }

    @Override
    public Response updateGuard(LinkGuardVo vo) {
        if (vo.getId() == null) {
            return Response.fail(FALSE_CORE, "更新挡板id不能为null", null);
        }
        String applicationId;
        if (StringUtils.isBlank(vo.getApplicationId())) {
            LinkGuardEntity linkGuardEntity = tLinkGuardMapper.selectById(vo.getId());
            applicationId = String.valueOf(linkGuardEntity.getApplicationId());
        } else {
            applicationId = vo.getApplicationId();
        }
        LinkGuardEntity entity = new LinkGuardEntity();
        entity.setId(vo.getId());
        entity.setApplicationName(vo.getApplicationName());
        entity.setMethodInfo(vo.getMethodInfo());
        entity.setGroovy(vo.getGroovy());
        entity.setIsEnable(vo.getIsEnable());
        entity.setRemark(vo.getRemark());
        try {
            tLinkGuardMapper.update(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(FALSE_CORE, "更新挡板失败", null);
        }
        User user = RestContext.getUser();
        configSyncService.syncGuard(user.getKey(), Long.parseLong(applicationId), vo.getApplicationName());
        agentConfigCacheManager.evictGuards(vo.getApplicationName());
        return Response.success();
    }

    @Override
    public Response deleteById(Long id) {
        try {
            LinkGuardEntity linkGuardEntity = tLinkGuardMapper.selectById(id);
            tLinkGuardMapper.deleteById(id);
            User user = RestContext.getUser();
            configSyncService.syncGuard(user.getKey(), linkGuardEntity.getApplicationId(), null);
            agentConfigCacheManager.evictGuards(linkGuardEntity.getApplicationName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(FALSE_CORE, "删除挡板失败", null);
        }
        return Response.success();
    }

    @Override
    public Response<List<LinkGuardVo>> selectByExample(LinkGuardQueryParam param) {
        List<LinkGuardVo> result = new ArrayList<>();
        List<LinkGuardEntity> page = new ArrayList<>();
        List<LinkGuardEntity> list = null;
        if (param != null && StringUtils.isNotBlank(param.getApplicationId())) {
            param.setAppId(Long.valueOf(param.getApplicationId()));
        }
        try {
            //处理agent携带用户信息的查询
            if (RestContext.getTenantUserKey() != null && !RestContext.getTenantUserKey().isEmpty()) {
                if (param.getApplicationName() != null) {
                    String userAppKey = RestContext.getTenantUserKey();
                    User user = TUserMapper.selectByKey(userAppKey);
                    TApplicationMnt applicationMnt = applicationMntDao.queryApplicationInfoByNameAndTenant(
                        param.getApplicationName(), user == null ? null : user.getId());
                    if (applicationMnt != null) {
                        param.setAppId(applicationMnt.getApplicationId());
                    }
                }
            }
            list = tLinkGuardMapper.selectByExample(param);

            if (null != list && list.size() > 0) {
                if (param.getCurrentPage() == null || param.getPageSize() == null) {
                    page = list;
                } else {
                    page = PageUtils.getPage(true, param.getCurrentPage(), param.getPageSize(), list);
                }
                page.stream().forEach(guardEntity -> {
                    result.add(entityToVo(guardEntity));
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(FALSE_CORE, "查询挡板失败", null);
        }
        return Response.success(result, CollectionUtils.isEmpty(list) ? 0 : list.size());
    }

    @Override
    public List<LinkGuardVo> agentSelect(Long customerId, String appName) {
        List<LinkGuardResult> results = linkGuardDAO.selectByAppNameUnderCurrentUser(appName, customerId);
        return results.stream().map(item -> {
            LinkGuardVo target = new LinkGuardVo();
            BeanUtils.copyProperties(item, target);
            return target;
        }).collect(Collectors.toList());
    }

    @Override
    public Response<List<LinkGuardEntity>> selectAll() {
        List<LinkGuardEntity> list = null;
        try {
            LinkGuardQueryParam param = new LinkGuardQueryParam();
            param.setIsEnable(true);
            list = tLinkGuardMapper.selectByExample(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(FALSE_CORE, "查询挡板失败", null);
        }
        return Response.success(list);
    }

    @Override
    public Response getById(Long id) {
        LinkGuardEntity guardEntity = tLinkGuardMapper.selectById(id);
        LinkGuardVo vo = entityToVo(guardEntity);
        return Response.success(vo);
    }

    @Override
    public Response enableGuard(Long id, Boolean target) {
        if (id == null || target == null) {
            return Response.fail(FALSE_CORE, "挡板开关", null);
        }
        LinkGuardEntity linkGuardEntity = tLinkGuardMapper.selectById(id);
        LinkGuardEntity entity = new LinkGuardEntity();
        entity.setId(id);
        entity.setIsEnable(target);
        tLinkGuardMapper.update(entity);
        User user = RestContext.getUser();
        configSyncService.syncGuard(user.getKey(), linkGuardEntity.getApplicationId(), null);
        agentConfigCacheManager.evictGuards(linkGuardEntity.getApplicationName());
        return Response.success();
    }

    @Override
    public List<LinkGuardEntity> getAllEnabledGuard(String applicationId) {
        return tLinkGuardMapper.getAllEnabledGuard(applicationId);
    }

    public LinkGuardVo entityToVo(LinkGuardEntity guardEntity) {
        if (guardEntity == null) {
            return null;
        }
        LinkGuardVo vo = new LinkGuardVo();
        vo.setId(guardEntity.getId());
        vo.setApplicationName(guardEntity.getApplicationName());
        vo.setMethodInfo(guardEntity.getMethodInfo());
        vo.setGroovy(guardEntity.getGroovy());
        vo.setCreateTime(guardEntity.getCreateTime());
        vo.setUpdateTime(guardEntity.getUpdateTime());
        vo.setRemark(guardEntity.getRemark());
        vo.setIsEnable(guardEntity.getIsEnable());
        List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
            vo.setCanEdit(allowUpdateUserIdList.contains(guardEntity.getUserId()));
        }
        List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
            vo.setCanRemove(allowDeleteUserIdList.contains(guardEntity.getUserId()));
        }
        List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
            vo.setCanEnableDisable(allowEnableDisableUserIdList.contains(guardEntity.getUserId()));
        }
        return vo;
    }
}
