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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.auth.TRoleUserRelationMapper;
import com.pamirs.tro.entity.dao.auth.TUserDeptRelationMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.PageUtils;
import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelation;
import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelationExample;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import com.pamirs.tro.entity.domain.entity.user.DeptUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.RoleUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 下午3:16
 * @Description:
 */
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    TRoleUserRelationMapper TRoleUserRelationMapper;

    @Autowired
    TUserDeptRelationMapper TUserDeptRelationMapper;

    @Autowired
    DeptService deptService;

    @Autowired
    TUserMapper TUserMapper;

    @Autowired
    private TroUserDAO troUserDAO;

    /**
     * 查询组织成员
     *
     * @param param
     * @return
     */
    @Override
    public PagingList<UserCommonResult> selectUserByCondition(UserQueryParam param) {
        return troUserDAO.selectUserByCondition(param);
    }

    @Override
    public PageInfo<DeptUser> getSimpleUserList(DeptUserQueryParam param) {
        List<String> deptIdList = param.getDeptIds();
        List<Dept> deptTotalList = deptService.getAllDepts("");
        //获取末端部门节点
        List<String> endDeptIdList = Lists.newArrayList();
        TreeConvertUtil.getEndDeptByDeptIds(deptTotalList, deptIdList, endDeptIdList);
        List<DeptUser> deptUserList = TUserDeptRelationMapper.selectByDept(endDeptIdList, param.getUserName());
        //开始分页
        List<DeptUser> pageData = PageUtils.getPage(true, param.getCurrent(), param.getPageSize(), deptUserList);
        PageInfo<DeptUser> data = new PageInfo<>(pageData);
        data.setTotal(deptUserList.size());
        return data;
    }

    @Override
    public List<DeptUser> getSimpleUserByRoleId(RoleUserQueryParam param) {
        List<DeptUser> deptUserList = TUserDeptRelationMapper.selectByRole(param.getRoleId());
        if (CollectionUtils.isNotEmpty(deptUserList)) {
            return deptUserList.stream().sorted(Comparator.comparing(DeptUser::getCreateTime).reversed()).collect(
                    Collectors.toList());
        }
        return deptUserList;
    }

    @Override
    public List<String> getUserIdListByRoleId(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return Lists.newArrayList();
        }
        List<String> userIdList = Lists.newArrayList();
        RoleUserRelationExample relationExample = new RoleUserRelationExample();
        RoleUserRelationExample.Criteria criteria = relationExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<RoleUserRelation> relations = TRoleUserRelationMapper.selectByExample(relationExample);
        if (CollectionUtils.isNotEmpty(relations)) {
            userIdList = relations.stream().map(RoleUserRelation::getUserId).collect(Collectors.toList());
        }
        return userIdList;
    }

    @Override
    public List<User> getUserById(List<Long> userIds) {
        List<User> userList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(userIds)) {
            return userList;
        }
        userList = TUserMapper.selectByIds(userIds);
        return userList;
    }

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return key:userId  value:user对象
     */
    @Override
    public Map<Long, User> getUserMapByIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<User> userList = this.getUserById(userIds);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().collect(Collectors.toMap(User::getId, data -> data, (k1, k2) -> k1));
    }

    @Override
    public List<UserCommonResult> getByName(String userName) {
        return troUserDAO.selectByName(userName);
    }

    private List<DeptUser> convertUserList(String roleId, List<User> userList) {
        List<DeptUser> deptUserList = Lists.newArrayList();
        List<RoleUserRelation> relations = Lists.newArrayList();
        if (StringUtils.isNotBlank(roleId)) {
            RoleUserRelationExample example = new RoleUserRelationExample();
            RoleUserRelationExample.Criteria criteria = example.createCriteria();
            criteria.andRoleIdEqualTo(roleId);
            relations = TRoleUserRelationMapper.selectByExample(example);
        }
        for (User user : userList) {
            DeptUser deptUser = new DeptUser();
            deptUser.setId(user.getId());
            deptUser.setName(user.getName());
            deptUser.setNick(user.getNick());
            if (StringUtils.isNotBlank(roleId)) {
                for (RoleUserRelation relation : relations) {
                    if (StringUtils.equals(String.valueOf(user.getId()), relation.getUserId())) {
                        deptUser.setCreateTime(relation.getCreateTime());
                    }
                }
            }
            List<Dept> userDeptList = deptService.getDeptByUserId(String.valueOf(user.getId()));
            if (CollectionUtils.isEmpty(userDeptList)) {
                deptUserList.add(deptUser);
            } else {
                for (Dept dept : userDeptList) {
                    deptUser.setDeptId(dept.getId());
                    deptUser.setDeptName(dept.getName());
                    deptUserList.add(deptUser);
                }
            }
        }
        return deptUserList;
    }
}
