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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.dao.auth.TDeptMapper;
import com.pamirs.tro.entity.dao.auth.TUserDeptRelationMapper;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.DeptExample;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.auth.TreeDeptModel;
import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelation;
import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelationExample;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.auth.common.YundaBaseInfo;
import io.shulie.tro.web.auth.utils.HttpClientUtils;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.mapper.mysql.TroDeptMapper;
import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;
    @Autowired
    private TroDeptMapper troDeptMapper;
    @Autowired
    private TroDeptDAO troDeptDAO;
    @Autowired
    TUserDeptRelationMapper TUserDeptRelationMapper;

    private String YUNDA_DEPT_TOKEN_KEY = "YUNDA_DEPT_TOKEN_KEY";

    @Resource
    UserServiceImpl userService;

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
//        List<Dept> deptList = getAllDepts(deptName);
//        TreeConvertUtil.convertToTree(deptList, null);
//        List<TreeDeptModel> collect = deptList
//                .stream()
//                .filter(item -> item.getParentId() == null)
//                .map(TreeConvertUtil::convertTreeDeptModel)
//                .collect(Collectors.toList());
//        return collect.stream().sorted(Comparator.comparing(TreeDeptModel::getOrder)).collect(Collectors.toList());
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
//        DeptUser deptUser = userService.getDeptUser(userId);
//        Dept dept = new Dept();
//        dept.setId(deptUser.getDeptId());
//        dept.setName(deptUser.getDeptName());
//        return Arrays.asList(dept);
    }

    private List<Dept> getDepts(String yundaToken, String lowe) {
        List<Dept> depts = new ArrayList<>();
        JSONObject param = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_HCM_ORG;
        param.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        param.put("token", yundaToken);
        param.put("orgId", YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID);
        //不填代表只查当前子部门，填Y代表遍历查询所有子部门
        param.put("lowe", lowe);
        param.put("startTime", "1980-01-01 00:00:00");
        param.put("endTime", DateUtils.dateToString(new Date(), DateUtils.FORMATE_YMDHMS));
        int startPage = 1;
        while (true) {
            param.put("startPage", Integer.toString(startPage));
            param.put("pageSize", "100");
            startPage++;
            JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, param.toString());
            if (orgData == null) {
                break;
            }
            JSONArray data = orgData.getJSONArray("data");
            if (CollectionUtils.isEmpty(data)) {
                break;
            }
            for (Object obj : data) {
                JSONObject o = JSON.parseObject(obj.toString());
                if (o == null) {
                    continue;
                }
                Dept dept = new Dept();
                dept.setId(o.getLong("orgid"));
                dept.setParentId(o.getLong("parentorgid"));
                //认为科技板块为最高节点
                if (YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID.equals(o.getString("orgid"))) {
                    dept.setParentId(null);
                }
                dept.setCode(o.getString("orgcode"));
                dept.setLevel(o.getString("orglevel"));
                dept.setName(o.getString("orgname"));
                depts.add(dept);
            }
            if (data.size() != 100) {
                break;
            }
        }
        return depts;
    }

    public String getYundaToken() {

        Object o = redisTemplate.opsForValue().get(YUNDA_DEPT_TOKEN_KEY);
        if (o != null) {
            return o.toString();
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        param.put("secretKey", YundaBaseInfo.YUNDA_DEPT_SECRET_KEY);
        String s = HttpClientUtils.getInstance().sendHttpPost(
            YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_TOKEN, param);
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String token = s.replaceAll("\"", "");
        redisTemplate.opsForValue().set(YUNDA_DEPT_TOKEN_KEY, token, 60, TimeUnit.SECONDS);
        return token;
    }
}
