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

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.auth.TDeptMapper;
import com.pamirs.tro.entity.dao.auth.TUserDeptRelationMapper;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.DeptExample;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.auth.TreeDeptModel;
import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelation;
import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelationExample;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.mapper.mysql.TroDeptMapper;
import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 下午3:13
 * @Description:
 */
@Component
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    TDeptMapper TDeptMapper;
    @Autowired
    private TroDeptMapper troDeptMapper;
    @Autowired
    private TroDeptDAO troDeptDAO;

    @Autowired
    TUserDeptRelationMapper TUserDeptRelationMapper;

    @Override
    public List<Dept> getAllDepts(String deptName) {
        DeptExample example = new DeptExample();
        DeptExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(deptName)) {
            criteria.andNameLike(deptName);
        }
        return TDeptMapper.selectByExample(example);
    }


    /**
     * 根据部门名称递归获取部门信息
     *
     * @param deptName
     * @return
     */
    public List<Dept> recursionDeptTreeByName(String deptName) {
        LambdaQueryWrapper<TroDeptEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroDeptEntity::getId,
                TroDeptEntity::getParentId,
                TroDeptEntity::getSequence,
                TroDeptEntity::getName,
                TroDeptEntity::getCode);
        wrapper.like(StringUtils.isNotBlank(deptName), TroDeptEntity::getName, deptName);
        List<TroDeptEntity> deptList = troDeptMapper.selectList(wrapper);
        //递归查询上级信息，直到根节点
        List<TroDeptEntity> deptEntityList = troDeptDAO.recursionDept(deptList);
        return JSON.parseArray(JSON.toJSONString(deptEntityList), Dept.class);
    }



    @Override
    public List<TreeDeptModel> getDeptTree(String deptName) {
        //递归查询部门信息
        List<Dept> deptLists = recursionDeptTreeByName(deptName);
        //转换部门树
        return TreeConvertUtil.convertToTree(deptLists);
    }

    @Override
    public List<Dept> getDeptByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Lists.newArrayList();
        }
        UserDeptRelationExample example = new UserDeptRelationExample();
        UserDeptRelationExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserDeptRelation> userDeptRelationList = TUserDeptRelationMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userDeptRelationList)) {
            return Lists.newArrayList();
        }
        List<Long> deptIds = userDeptRelationList.stream().map(UserDeptRelation::getDeptId).map(Long::parseLong)
                .collect(Collectors.toList());
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria deptCriteria = deptExample.createCriteria();
        deptCriteria.andIdIn(deptIds);
        return TDeptMapper.selectByExample(deptExample);
    }
}
