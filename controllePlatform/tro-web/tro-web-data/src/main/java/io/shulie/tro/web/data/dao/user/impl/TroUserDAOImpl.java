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

package io.shulie.tro.web.data.dao.user.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.mapper.mysql.TroUserDeptRelationMapper;
import io.shulie.tro.web.data.mapper.mysql.TroUserMapper;
import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import io.shulie.tro.web.data.model.mysql.TroUserEntity;
import io.shulie.tro.web.data.model.mysql.UserDeptEntity;
import io.shulie.tro.web.data.param.user.UserCreateParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleQueryParam;
import io.shulie.tro.web.data.param.user.UserUpdateParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import io.shulie.tro.web.data.result.user.RoleQueryResult;
import io.shulie.tro.web.data.result.user.TroUserResult;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;
import io.shulie.tro.web.data.result.user.UserRoleResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 9:58 上午
 * @Description:
 */
@Component
public class TroUserDAOImpl implements TroUserDAO {

    @Autowired
    private TroUserDeptRelationMapper userDeptRelationMapper;

    @Autowired
    private TroDeptUserRelationDAO deptUserRelationDAO;

    @Autowired
    private TroRoleUserRelationDAO roleUserRelationDAO;

    @Autowired
    private TroDeptDAO troDeptDAO;

    @Autowired
    private TroUserMapper troUserMapper;

    private String getTopNodeName(Long id, Map<Long, TroDeptEntity> deptMap) {
        //没有这个id 或者没有一级部门 return
        if (!deptMap.containsKey(id)) {
            return "";
        }

        TroDeptEntity deptEntity = deptMap.get(id);
        if (null == deptEntity.getParentId() || 0 == deptEntity.getParentId()) {
            return deptEntity.getName();
        }
        return getTopNodeName(deptEntity.getParentId(), deptMap);

    }

    /**
     * 获取根节点部门名称
     *
     * @param userDeptResultList
     * @return key:deptId,value:deptName
     */
    private Map<Long, String> getDeptBaseTreeName(List<UserDeptResult> userDeptResultList) {
        List<DeptQueryResult> deptQueryResults = userDeptResultList.stream().flatMap(
            data -> data.getDeptList().stream()).collect(Collectors.toList());
        //递归到部门根节点
        List<TroDeptEntity> deptList = troDeptDAO.recursionDept(
            JSON.parseArray(JSON.toJSONString(deptQueryResults), TroDeptEntity.class));

        //key:ParentId value:Dept对象
        Map<Long, List<DeptQueryResult>> deptMap = userDeptResultList.stream()
            .map(data -> data.getDeptList())
            .flatMap(data -> data.stream())
            .map(data -> {
                if (null == data.getParentId()) {
                    data.setParentId(0L);
                }
                return data;
            }).collect(Collectors.groupingBy(data -> data.getParentId()));

        //根据id分组
        Map<Long, TroDeptEntity> deptEntityMap = deptList.stream().collect(
            Collectors.toMap(data -> data.getId(), data -> data, (k1, k2) -> k1));

        Map<Long, String> resultMap = Maps.newHashMap();
        for (Map.Entry<Long, List<DeptQueryResult>> entry : deptMap.entrySet()) {
            Long parentId = entry.getKey();
            List<DeptQueryResult> depts = entry.getValue();
            //一级部门
            if (0 == parentId) {
                for (DeptQueryResult dept : depts) {
                    Map<Long, String> map = putTopNodeName(dept.getName(), Arrays.asList(dept.getId()));
                    resultMap.putAll(map);
                }
            }

            List<Long> deptIds = depts.stream().map(data -> data.getId()).collect(Collectors.toList());

            //一级节点名称
            String topNodeName = getTopNodeName(parentId, deptEntityMap);
            Map<Long, String> nodeName = putTopNodeName(topNodeName, deptIds);
            resultMap.putAll(nodeName);
        }
        return resultMap;
    }

    private Map<Long, String> putTopNodeName(String topNodeName, List<Long> deptIds) {
        if (StringUtils.isEmpty(topNodeName) || CollectionUtils.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }
        Map<Long, String> resultMap = Maps.newHashMap();
        for (Long deptId : deptIds) {
            resultMap.put(deptId, topNodeName);
        }
        return resultMap;
    }

    /**
     * 查询组织成员
     *
     * @param param
     * @return
     */
    @Override
    public PagingList<UserCommonResult> selectUserByCondition(UserQueryParam param) {
        //先根据条件查询出匹配的用户id列表，再根据用户id列表去拿用户部门信息、角色信息等
        Page<UserDeptEntity> page = new Page<>(param.getCurrent(), param.getPageSize());
        IPage<UserDeptEntity> userDeptRoleEntityIPage = userDeptRelationMapper.selectUserIdByCondition(page, param);
        List<UserDeptEntity> userDeptEntityList = userDeptRoleEntityIPage.getRecords();
        if (CollectionUtils.isEmpty(userDeptEntityList)) {
            return PagingList.empty();
        }
        List<String> userIdList = userDeptEntityList.stream().map(UserDeptEntity::getId).map(String::valueOf).collect(
            Collectors.toList());

        UserDeptQueryParam userDeptQueryParam = new UserDeptQueryParam();
        userDeptQueryParam.setUserIdList(userIdList);
        //用户部门信息
        List<UserDeptResult> userDeptResultList = deptUserRelationDAO.selectList(userDeptQueryParam);
        //key:deptId,value:一级部门名称(4.8.0.4以后账号展示直属部门，不展示一级部门名称)
        //Map<Long, String> baseNameMap = getDeptBaseTreeName(userDeptResultList);

        //部门信息，按用户分组
        Map<Long, List<UserDeptResult>> userDeptMap = userDeptResultList.stream().collect(
            Collectors.groupingBy(UserDeptResult::getUserId));

        UserRoleQueryParam userRoleQueryParam = new UserRoleQueryParam();
        userRoleQueryParam.setUserIdList(userIdList);
        //用户角色信息
        List<UserRoleResult> userRoleResultList = roleUserRelationDAO.selectList(userRoleQueryParam);
        //角色信息，按用户分组
        Map<Long, List<UserRoleResult>> userRoleMap = userRoleResultList.stream().collect(
            Collectors.groupingBy(UserRoleResult::getUserId));

        List<UserCommonResult> userCommonResultList = Lists.newArrayList();
        // 增加admin账号,查全部
        if(CollectionUtils.isEmpty(param.getDeptIds()) && StringUtils.isBlank(param.getName())
             && CollectionUtils.isEmpty(param.getUserIds()) && CollectionUtils.isEmpty(param.getRoleIds())
        ) {
            UserCommonResult result = new UserCommonResult();
            TroUserEntity admin = troUserMapper.selectById(param.getCustomerId());
            result.setUserId(admin.getId());
            result.setUserName(admin.getName());
            // 没有部门
            result.setDeptNameList(Lists.newArrayList());
            result.setUserAppKey(admin.getKey());
            List<RoleQueryResult> roleList =Lists.newArrayList();
            // todo
            //RoleQueryResult
            //result.setRoleList();
        }
        userDeptEntityList.forEach(userDeptEntity -> {
            UserCommonResult userCommonResult = new UserCommonResult();
            userCommonResult.setUserId(userDeptEntity.getId());
            userCommonResult.setUserName(userDeptEntity.getName());
            //填充部门名称
            if (CollectionUtils.isNotEmpty(userDeptResultList) && userDeptMap.containsKey(userDeptEntity.getId())) {
                //只显示一级部门名称List
                List<String> deptNameList = userDeptMap.get(userDeptEntity.getId())
                    .stream()
                    .flatMap(data -> data.getDeptList().stream())
                    .map(DeptQueryResult::getName)
                    .distinct()
                    .collect(Collectors.toList());
                userCommonResult.setDeptNameList(deptNameList);
            }

            //填充角色信息
            if (CollectionUtils.isNotEmpty(userRoleResultList) && userRoleMap.containsKey(userDeptEntity.getId())) {
                List<RoleQueryResult> roleList = userRoleMap.get(userDeptEntity.getId()).stream().flatMap(
                    data -> data.getRoleList().stream()).collect(Collectors.toList());
                userCommonResult.setRoleList(roleList);
            }
            userCommonResultList.add(userCommonResult);
        });
        return PagingList.of(userCommonResultList, page.getTotal());
    }

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    public List<UserCommonResult> selectUserByIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<TroUserEntity> list = troUserMapper.selectBatchIds(userIds);
        return list.stream().map(entity -> {
            UserCommonResult result = new UserCommonResult();
            result.setUserId(entity.getId());
            result.setUserName(entity.getName());
            return result;
        }).collect(Collectors.toList());
    }

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    public Map<Long, UserCommonResult> selectUserMapByIds(List<Long> userIds) {
        List<UserCommonResult> userList = selectUserByIds(userIds);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().collect(Collectors.toMap(UserCommonResult::getUserId, data -> data, (k1, k2) -> k1));
    }

    /**
     * 批量的插入 or  更新的操作
     *
     * @param list
     */
    @Override
    public int saveOrUpdateUserBatch(List<TroUserEntity> list) {
        return troUserMapper.saveOrUpdateUserBatch(list);
    }

    @Override
    public TroUserResult selectById(Long id) {
        TroUserEntity entity = troUserMapper.selectById(id);
        if (entity != null) {
            TroUserResult result = new TroUserResult();
            BeanUtils.copyProperties(entity, result);
            return result;
        }
        return null;
    }

    @Override
    public TroUserResult selectByUserAppKey(String userAppkey) {
        LambdaQueryWrapper<TroUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TroUserEntity::getKey, userAppkey);
        List<TroUserEntity> troUserEntities = troUserMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(troUserEntities)) {
            return null;
        }
        TroUserResult result = new TroUserResult();
        BeanUtils.copyProperties(troUserEntities.get(0), result);
        return result;
    }

    @Override
    public Long insert(UserCreateParam createParam) {
        if (StringUtils.isBlank(createParam.getName())) {
            return null;
        }
        TroUserEntity entity = new TroUserEntity();
        entity.setName(createParam.getName());
        entity.setNick(createParam.getNick());
        entity.setKey(createParam.getKey());
        entity.setSalt(createParam.getSalt());
        entity.setPassword(createParam.getPassword());
        entity.setCustomerId(createParam.getCustomerId());
        entity.setUserType(createParam.getUserType());
        troUserMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public int updateUser(UserUpdateParam userUpdateParam) {
        TroUserEntity troUserEntity = troUserMapper.selectById(userUpdateParam.getUserId());
        if (!Objects.isNull(userUpdateParam.getUserType())
            && !userUpdateParam.getUserType().equals(troUserEntity.getUserType())) {
            troUserEntity.setUserType(userUpdateParam.getUserType());
            return troUserMapper.updateById(troUserEntity);
        }
        return 0;
    }

    @Override
    public UserDetailResult selectUserById(Long id) {
        if (!Objects.isNull(id)) {
            TroUserEntity troUserEntity = troUserMapper.selectById(id);
            if (!Objects.isNull(troUserEntity)) {
                UserDetailResult detailResult = new UserDetailResult();
                detailResult.setId(troUserEntity.getId());
                detailResult.setName(troUserEntity.getName());
                detailResult.setStatus(troUserEntity.getStatus());
                detailResult.setUserType(troUserEntity.getUserType());
                detailResult.setSalt(troUserEntity.getSalt());
                detailResult.setPassword(troUserEntity.getPassword());
                detailResult.setCustomerId(troUserEntity.getCustomerId());
                detailResult.setKey(troUserEntity.getKey());
                return detailResult;
            }
        }
        return null;
    }

    @Override
    public UserDetailResult selectUserByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<TroUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TroUserEntity::getName, name);
            TroUserEntity troUserEntity = troUserMapper.selectOne(queryWrapper);
            if (!Objects.isNull(troUserEntity)) {
                UserDetailResult detailResult = new UserDetailResult();
                detailResult.setId(troUserEntity.getId());
                detailResult.setName(troUserEntity.getName());
                detailResult.setStatus(troUserEntity.getStatus());
                detailResult.setUserType(troUserEntity.getUserType());
                detailResult.setSalt(troUserEntity.getSalt());
                detailResult.setPassword(troUserEntity.getPassword());
                detailResult.setCustomerId(troUserEntity.getCustomerId());
                detailResult.setKey(troUserEntity.getKey());
                return detailResult;
            }
        }
        return null;
    }

    @Override
    public List<UserDetailResult> selectAllUser() {
        LambdaQueryWrapper<TroUserEntity> query = new LambdaQueryWrapper<>();
        query.eq(TroUserEntity::getIsDelete, 0);
        List<TroUserEntity> troUserEntities = troUserMapper.selectList(query);
        if (CollectionUtils.isEmpty(troUserEntities)) {
            return Lists.newArrayList();
        }
        return troUserEntities.stream().map(troUserEntity -> {
            UserDetailResult detailResult = new UserDetailResult();
            detailResult.setId(troUserEntity.getId());
            detailResult.setName(troUserEntity.getName());
            detailResult.setStatus(troUserEntity.getStatus());
            detailResult.setUserType(troUserEntity.getUserType());
            detailResult.setCustomerId(troUserEntity.getCustomerId());
            detailResult.setKey(troUserEntity.getKey());
            return detailResult;
        }).collect(Collectors.toList());
    }

    /**
     * 根据名称模糊搜索
     * @param userName
     * @return
     */
    @Override
    public List<UserCommonResult> selectByName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<TroUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TroUserEntity::getId,
            TroUserEntity::getName,
            TroUserEntity::getUserType
        );
        wrapper.like(TroUserEntity::getName, userName);
        wrapper.eq(TroUserEntity::getIsDelete, false);
        List<TroUserEntity> troUserEntities = troUserMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(troUserEntities)) {
            return Lists.newArrayList();
        }
        List<UserCommonResult> results = new ArrayList<>();
        troUserEntities.stream().forEach(entity -> {
            UserCommonResult commonResult = new UserCommonResult();
            commonResult.setUserId(entity.getId());
            commonResult.setUserName(entity.getName());
            results.add(commonResult);
        });
        return results;
    }

}
