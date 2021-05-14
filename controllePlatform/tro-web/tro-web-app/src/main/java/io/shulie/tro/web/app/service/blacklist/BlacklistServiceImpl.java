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

package io.shulie.tro.web.app.service.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants.Vars;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.input.blacklist.BlacklistCreateInput;
import io.shulie.tro.web.app.input.blacklist.BlacklistSearchInput;
import io.shulie.tro.web.app.input.blacklist.BlacklistUpdateInput;
import io.shulie.tro.web.app.output.blacklist.BlacklistOutput;
import io.shulie.tro.web.app.service.linkManage.impl.WhiteListFileService;
import io.shulie.tro.web.common.enums.blacklist.BlacklistTypeEnum;
import io.shulie.tro.web.common.vo.blacklist.BlacklistVO;
import io.shulie.tro.web.data.dao.blacklist.BlackListDAO;
import io.shulie.tro.web.data.param.blacklist.BlacklistCreateNewParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistSearchParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistUpdateParam;
import io.shulie.tro.web.data.result.blacklist.BlacklistResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.blacklist
 * @date 2021/4/6 2:15 下午
 */
@Service
public class BlacklistServiceImpl implements BlacklistService{
    @Autowired
    private BlackListDAO blackListDAO;
    @Autowired
    private ConfigSyncService configSyncService;
    @Resource
    private TUserMapper TUserMapper;
    @Autowired
    private WhiteListFileService whiteListFileService;
    @Resource
    private TApplicationMntDao applicationMntDao;

    @Override
    public void insert(BlacklistCreateInput input) {
        BlacklistSearchParam searchParam = new BlacklistSearchParam();
        searchParam.setApplicationId(input.getApplicationId());
        searchParam.setRedisKey(input.getRedisKey());
        List<BlacklistResult> results = blackListDAO.selectList(searchParam);
        if(CollectionUtils.isNotEmpty(results)) {
            throw new TroWebException(ExceptionCode.BLACKLIST_ADD_ERROR,"不允许重复黑名单");
        }
        BlacklistCreateNewParam param = new BlacklistCreateNewParam();
        BeanUtils.copyProperties(input,param);

        User user = RestContext.getUser();
        if (user != null) {
            param.setCustomerId(RestContext.getCustomerId());
            param.setUserId(user.getId());
        }
        // 目前就redis
        param.setType(BlacklistTypeEnum.REDIS.getType());
        param.setUseYn(1);
        blackListDAO.newInsert(param);
        // 刷新agent数据
        updateAgentData(input.getApplicationId());
    }

    @Override
    public void update(BlacklistUpdateInput input) {
        BlacklistResult result = blackListDAO.selectById(input.getBlistId());
        if(result == null) {
            throw new TroWebException(ExceptionCode.BLACKLIST_UPDATE_ERROR,"未查到该id");
        }
        BlacklistSearchParam searchParam = new BlacklistSearchParam();
        searchParam.setApplicationId(result.getApplicationId());
        searchParam.setRedisKey(input.getRedisKey());
        List<BlacklistResult> results = blackListDAO.selectList(searchParam);
        if(!result.getRedisKey().equals(input.getRedisKey()) && CollectionUtils.isNotEmpty(results)) {
            throw new TroWebException(ExceptionCode.BLACKLIST_UPDATE_ERROR,"不允许重复黑名单");
        }

        BlacklistUpdateParam param = new BlacklistUpdateParam();
        BeanUtils.copyProperties(input,param);
        blackListDAO.update(param);
        updateAgentData(result.getApplicationId());
    }

    @Override
    public void enable(BlacklistUpdateInput input) {
        BlacklistResult result = blackListDAO.selectById(input.getBlistId());
        if(result == null) {
            throw new TroWebException(ExceptionCode.BLACKLIST_UPDATE_ERROR,"未查到该id");
        }
        BlacklistUpdateParam param = new BlacklistUpdateParam();
        BeanUtils.copyProperties(input,param);
        blackListDAO.update(param);
        updateAgentData(result.getApplicationId());
    }

    private void updateAgentData(Long applicationId) {
        // 刷新agent数据
        TApplicationMnt tApplicationMnt = applicationMntDao.queryApplicationinfoById(applicationId);
        User appUser = TUserMapper.selectById(tApplicationMnt.getUserId());
        whiteListFileService.writeWhiteListFile();
        configSyncService.syncAllowList(appUser.getKey(), applicationId, tApplicationMnt.getApplicationName());
    }

    @Override
    public void batchEnable(List<Long> ids, Integer useYn) {
        List<BlacklistResult> results = blackListDAO.selectByIds(ids);
        if(CollectionUtils.isEmpty(results)) {
            throw new TroWebException(ExceptionCode.BLACKLIST_DELETE_ERROR,"批量更新失败");
        }
        OperationLogContextHolder.addVars(Vars.BLACKLIST_VALUE, results.stream()
            .map(BlacklistResult::getRedisKey).collect(Collectors.joining(",")));
        ids.forEach(id -> {
            BlacklistUpdateParam param = new BlacklistUpdateParam();
            param.setBlistId(id);
            param.setUseYn(useYn);
            blackListDAO.update(param);
        });
        updateAgentData(results.get(0).getApplicationId());
    }

    @Override
    public void delete(Long id) {
        BlacklistResult result = blackListDAO.selectById(id);
        if(result == null) {
            throw new TroWebException(ExceptionCode.BLACKLIST_DELETE_ERROR,"未查到该id");
        }
        OperationLogContextHolder.addVars(Vars.BLACKLIST_VALUE, result.getRedisKey());
        OperationLogContextHolder.addVars(Vars.BLACKLIST_TYPE, BlacklistTypeEnum.getDescByType(result.getType()));
        blackListDAO.delete(id);
        updateAgentData(result.getApplicationId());
    }

    @Override
    public void batchDelete(List<Long> ids) {
        List<BlacklistResult> results = blackListDAO.selectByIds(ids);
        if(CollectionUtils.isEmpty(results)) {
            throw new TroWebException(ExceptionCode.BLACKLIST_DELETE_ERROR,"批量删除失败");
        }
        OperationLogContextHolder.addVars(Vars.BLACKLIST_VALUE, results.stream()
            .map(BlacklistResult::getRedisKey).collect(Collectors.joining(",")));
        blackListDAO.batchDelete(ids);
        updateAgentData(results.get(0).getApplicationId());
    }

    @Override
    public PagingList<BlacklistVO> pageList(BlacklistSearchInput input) {
        BlacklistSearchParam param = new BlacklistSearchParam();
        BeanUtils.copyProperties(input,param);
        User user = RestContext.getUser();
        if (user != null) {
            param.setCustomerId(RestContext.getCustomerId());
            param.setUserId(user.getId());
        }
        // 是否是admin账号
        // param.setIsAdmin(user.getRole());
        PagingList<BlacklistVO> pagingList = blackListDAO.pageList(param);
        if(pagingList.isEmpty()) {
            return PagingList.empty();
        }
        for (BlacklistVO vo : pagingList.getList()) {
            List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
            if (CollectionUtils.isEmpty(allowUpdateUserIdList)) {
                //管理员
                vo.setCanEdit(true);
            } else {
                //普通用户
                vo.setCanEdit(allowUpdateUserIdList.contains(vo.getUserId()));
            }

            List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
            if (CollectionUtils.isEmpty(allowDeleteUserIdList)) {
                vo.setCanRemove(true);
            } else {
                vo.setCanRemove(allowDeleteUserIdList.contains(vo.getUserId()));
            }
            List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
            if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
                vo.setCanEnableDisable(allowEnableDisableUserIdList.contains(vo.getUserId()));
            }
        }
        return pagingList;
    }

    @Override
    public List<BlacklistOutput> selectList(BlacklistSearchInput input) {
        BlacklistSearchParam param = new BlacklistSearchParam();
        BeanUtils.copyProperties(input,param);
        User user = RestContext.getUser();
        if (user != null) {
            param.setCustomerId(RestContext.getCustomerId());
            param.setUserId(user.getId());
        }
        List<BlacklistResult> results = blackListDAO.selectList(param);
        if(CollectionUtils.isEmpty(results)) {
            return Lists.newArrayList();
        }
        return results.stream().map(result -> {
            BlacklistOutput output = new BlacklistOutput();
            BeanUtils.copyProperties(result,output);
            return output;
        }).collect(Collectors.toList());
    }

    @Override
    public BlacklistOutput selectById(Long id) {
        BlacklistResult result = blackListDAO.selectById(id);
        if(result == null) {
            return  null;
        }
        BlacklistOutput output = new BlacklistOutput();
        BeanUtils.copyProperties(result,output);
        return output;
    }
}
