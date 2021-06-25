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

package io.shulie.tro.web.auth.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.dao.auth.TAuthorityMapper;
import com.pamirs.tro.entity.dao.auth.TBaseRoleMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.auth.Authority;
import com.pamirs.tro.entity.domain.entity.auth.AuthorityExample;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import io.shulie.tro.web.auth.api.AuthService;
import io.shulie.tro.web.auth.api.ResourceService;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.auth.api.exception.TroLoginException;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroResourceDAO;
import io.shulie.tro.web.data.dao.user.TroRoleResourceAuthDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-01
 */
@Component
@Slf4j
public class AuthServiceImpl implements AuthService {

    public static int maxWait = 120 * 60; //单位秒

    @Autowired
    TAuthorityMapper TAuthorityMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    TBaseRoleMapper TBaseRoleMapper;

    @Autowired
    ResourceService resourceService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TUserMapper TUserMapper;

    @Autowired
    private TroResourceDAO troResourceDAO;

    @Autowired
    private TroRoleResourceAuthDAO troRoleResourceAuthDAO;

    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;

    @Autowired
    private TroDeptUserRelationDAO troDeptUserRelationDAO;

    @Override
    public User login(HttpServletRequest request, UserLoginParam userLoginParam) throws TroLoginException {
        UserQueryParam param = new UserQueryParam();
        String username = userLoginParam.getUsername();
        String password = userLoginParam.getPassword();
        param.setName(username);
        User user = TUserMapper.queryUser(param);

        if (user == null) {
            log.error("用户不存在:{}", username);
            throw new TroLoginException("用户不存在");
        }

        String salt = user.getSalt();
        String pwd = BCrypt.hashpw(password, salt);
        if (!pwd.equals(user.getPassword())) {
            log.error("用户密码错误:{}", username);
            throw new TroLoginException("用户密码错误");
        }

        if (user.getStatus() == 1) {
            log.error("用户已禁用:{}", username);
            throw new TroLoginException("用户已禁用");
        }
        return user;
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("x-token");
        redisTemplate.delete(token);
        return null;
    }

    @Override
    public void redirect(String redirectUrl, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void loginSuccess(User user) {
        String uuid = UUID.randomUUID().toString();
        user.setXToken(uuid);
        redisTemplate.opsForValue().set(uuid, user, maxWait, TimeUnit.SECONDS);
    }

    @Override
    public List<Authority> getUserAuth(Integer type, List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        AuthorityExample example = new AuthorityExample();
        AuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andObjectTypeEqualTo(type);
        criteria.andObjectIdIn(ids);
        return TAuthorityMapper.selectByExample(example);
    }

    @Override
    public boolean hasPermissionUrl(User user, String url) {
        if (Objects.isNull(user)) {
            return false;
        }
        if (user.getUserType() == 0) {
            return true;
        }
        String checkUrl = url.substring(url.indexOf("/api"), url.length());
        if (checkUrl.contains("?")) {
            checkUrl = checkUrl.substring(0, checkUrl.indexOf("?") - 1);
        }
        List<String> permissionUrlList = user.getPermissionUrl();
        if (CollectionUtils.isEmpty(permissionUrlList)) {
            return false;
        }
        if (permissionUrlList.contains(checkUrl)) {
            return true;
        }
        return false;
    }

    @Override
    public User check(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("x-token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        User redisUser = (User) redisTemplate.opsForValue().get(token);
        return redisUser;
    }

    @Override
    public void checkFail(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", "未登陆，请重新登陆");
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            String s = JSON.toJSONString(map);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询可配置权限的（有页面的）菜单列表
     *
     * @return
     */
    @Override
    public List<ResourceMenuResult> selectAuthConfigMenu() {
        return troResourceDAO.selectAuthConfigMenu();
    }

    /**
     * 查询全量菜单列表
     *
     * @return
     */
    @Override
    public List<ResourceMenuResult> selectMenu(ResourceMenuQueryParam queryParam) {
        return troResourceDAO.selectMenu(queryParam);
    }

    /**
     * 查询角色权限
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<ResourceAuthResult> selectList(RoleResourceAuthQueryParam queryParam) {
        return troRoleResourceAuthDAO.selectList(queryParam);
    }

    /**
     * 更新角色权限
     *
     * @param updateParamList
     * @return
     */
    @Override
    public void updateRoleAuth(List<RoleResourceAuthUpdateParam> updateParamList) {
        troRoleResourceAuthDAO.updateRoleAuth(updateParamList);
    }

    /**
     * 删除角色权限
     *
     * @param deleteParamList
     * @return
     */
    @Override
    public void deleteRoleAuth(List<RoleResourceAuthDeleteParam> deleteParamList) {
        troRoleResourceAuthDAO.deleteRoleAuth(deleteParamList);
    }

    /**
     * 查询用户下所有角色列表
     *
     * @param param
     * @return
     */
    @Override
    public List<UserRoleRelationResult> selectUserRoleRelationBatch(UserRoleRelationBatchQueryParam param) {
        return troRoleUserRelationDAO.selectUserRoleRelationBatch(param);
    }

    /**
     * 根据用户id查询用户部门信息
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<UserDeptResult> selectList(UserDeptQueryParam queryParam) {
        return troDeptUserRelationDAO.selectList(queryParam);
    }

    /**
     * 根据部门id查询用户列表
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<UserDeptConditionResult> selectUserByDeptIds(UserDeptConditionQueryParam queryParam) {
        return troDeptUserRelationDAO.selectUserByDeptIds(queryParam);
    }
}
