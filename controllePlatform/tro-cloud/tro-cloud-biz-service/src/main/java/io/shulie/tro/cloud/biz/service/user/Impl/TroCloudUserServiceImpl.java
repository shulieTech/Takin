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

package io.shulie.tro.cloud.biz.service.user.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.settle.AccountBook;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import com.pamirs.tro.entity.domain.vo.user.UserVo;
import io.shulie.tro.cloud.biz.output.user.UserOutput;
import io.shulie.tro.cloud.biz.service.settle.AccountService;
import io.shulie.tro.cloud.biz.service.user.TroCloudUserService;
import io.shulie.tro.cloud.common.page.PageUtils;
import io.shulie.tro.cloud.common.utils.ListHelper;
import io.shulie.tro.cloud.common.utils.ResponseHeaderUtils;
import io.shulie.tro.cloud.common.utils.SettleUtil;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:45
 * @Description:
 */
@Component
@Slf4j
public class TroCloudUserServiceImpl implements TroCloudUserService {

    @Resource
    private TUserMapper tUserMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public User queryUser(UserQueryParam param) {
        return tUserMapper.queryUser(param);
    }

    @Override
    public ResponseResult addUser(UserVo userVo) {
        if (userVo == null || userVo.getModel() == null || userVo.getStatus() == null || userVo.getName() == null
            || userVo.getNick() == null || userVo.getStatus() == null) {
            return ResponseResult.fail("0", "参数错误", "");
        }
        UserQueryParam param = new UserQueryParam();
        param.setName(userVo.getName());
        User userExist = tUserMapper.queryByName(param);
        if (userExist != null) {
            return ResponseResult.fail("0", "用户已存在", "");
        }

        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setKey(UUID.randomUUID().toString());
        String salt = BCrypt.gensalt();
        String pwd = BCrypt.hashpw(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(pwd);
        if (userVo.getModel() == 0) {
            user.setRole(1);
        } else if (userVo.getModel() == 1) {
            user.setRole(2);
        }
        try {
            tUserMapper.insertSelective(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail("0", "创建用户失败", "");
        }
        //初始化用户账户、账本信息
        accountService.initAccount(user.getId());
        return ResponseResult.success();
    }

    @Override
    public ResponseResult updateUser(UserVo userVo) {
        if (userVo.getId() == null) {
            return ResponseResult.fail("0", "用户ID不许为空", "");
        }
        User userOld = tUserMapper.selectByPrimaryKey(userVo.getId());
        if (userOld == null) {
            return ResponseResult.fail("0", "用户不存在", "");
        }

        if (StringUtils.isNotBlank(userVo.getName()) && !userVo.getName().equals(userOld.getName())) {
            UserQueryParam param = new UserQueryParam();
            param.setName(userVo.getName());
            User userExist = tUserMapper.queryByName(param);
            if (userExist != null) {
                return ResponseResult.fail("0", "用户已存在", "");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userVo, user, "key");
        if (userVo.getModel() == 0) {
            user.setRole(1);
        } else if (userVo.getModel() == 1) {
            user.setRole(2);
        }
        if (StringUtils.isNotBlank(userVo.getPassword())) {
            String salt = BCrypt.gensalt();
            String pwd = BCrypt.hashpw(user.getPassword(), salt);
            user.setSalt(salt);
            user.setPassword(pwd);
        }
        tUserMapper.updateByPrimaryKeySelective(user);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult queryUserDetail(Long userId) {
        User user = tUserMapper.selectByPrimaryKey(userId);
        UserVo userVo = new UserVo();
        if (user != null) {
            userVo.setId(user.getId());
            userVo.setName(user.getName());
            userVo.setNick(user.getNick());
            userVo.setKey(user.getKey());
            userVo.setRole(user.getRole());
            userVo.setStatus(user.getStatus());
            userVo.setModel(user.getModel());
            userVo.setGmtCreate(user.getGmtCreate());
            userVo.setGmtUpdate(user.getGmtUpdate());
        }
        return ResponseResult.success(userVo);
    }

    @Override
    public UserOutput selectById(Long userId) {
        User user = tUserMapper.selectById(userId);
        UserOutput userOutput = new UserOutput();
        if (user != null) {
            BeanUtils.copyProperties(user,userOutput);
        }
        return userOutput;
    }

    @Override
    public ResponseResult<List<UserVo>> selectByExample(UserQueryParam param) {
        List<User> queryResult = new ArrayList<>();
        List<UserVo> parseResult = new ArrayList<>();
        queryResult = tUserMapper.selectByExample(param);
        if (CollectionUtils.isEmpty(queryResult)) {
            return ResponseResult.success(new PageInfo(Lists.newArrayList()).getList());
        }
        List<User> pageData = PageUtils.getPage(true, param.getCurrentPage(), param.getPageSize(), queryResult);

        Map<Long, BigDecimal> bookMap = ListHelper.transferToMap(
            accountService.getAccountBookByUserIds(pageData.stream().map(User::getId).collect(Collectors.toList())),
            AccountBook::getUid,
            AccountBook::getBalance);

        pageData.forEach(user -> {
            UserVo userVo = new UserVo();
            userVo.setId(user.getId());
            userVo.setName(user.getName());
            userVo.setNick(user.getNick());
            userVo.setKey(user.getKey());
            userVo.setRole(user.getRole());
            userVo.setStatus(user.getStatus());
            userVo.setModel(user.getModel());
            userVo.setGmtCreate(user.getGmtCreate());
            userVo.setGmtUpdate(user.getGmtUpdate());
            userVo.setFlowAmount(SettleUtil.format(bookMap.get(userVo.getId())));
            parseResult.add(userVo);
        });
        PageInfo<UserVo> data = new PageInfo<>(parseResult);
        data.setTotal(queryResult.size());
        return ResponseResult.success(data.getList());
    }

    @Override
    public User queryUserByKey(String key) {
        return tUserMapper.selectByKey(key);
    }
}
