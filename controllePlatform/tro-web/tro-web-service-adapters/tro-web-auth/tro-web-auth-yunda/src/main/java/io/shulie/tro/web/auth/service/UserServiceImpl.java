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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.common.util.HttpUtils;
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
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.auth.common.YundaBaseInfo;
import io.shulie.tro.web.auth.utils.HttpClientUtils;
import io.shulie.tro.web.data.dao.user.TroRoleDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
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

    @Resource
    DeptServiceImpl deptService;

    @Autowired
    TUserDeptRelationMapper TUserDeptRelationMapper;

    @Autowired
    private TroUserDAO troUserDAO;

    @Autowired
    private TroRoleDAO troRoleDAO;

    @Autowired
    TUserMapper TUserMapper;

    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;

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


    private PageInfo<DeptUser> getUserList(UserQueryParam param) {
        DeptUserQueryParam deptParam = new DeptUserQueryParam();
        deptParam.setCurrent(param.getCurrent());
        deptParam.setPageSize(param.getPageSize());
        deptParam.setUserName(param.getName());
        deptParam.setDeptIds(param.getDeptIds());
        return getUserListByUserAndDept(deptParam);
    }

    private PageInfo<DeptUser> getUserListByUserAndDept(DeptUserQueryParam param) {
        return getUserListByUserAndDept(new PageInfo<>(), param);
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

    public DeptUser getDeptUser(String userId) {
        String yundaToken = deptService.getYundaToken();
        JSONObject jsonParam = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_USER_IFO_BY_ID;
        jsonParam.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        jsonParam.put("token", yundaToken);
        jsonParam.put("empId", userId);

        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, jsonParam.toString());
        log.info("根据用户id查询用户信息和部门{}", orgData.toString());
        if (orgData == null) {
            return null;
        }
        JSONObject data;
        try {
            data = orgData.getJSONObject("data");
        } catch (Exception e) {
            log.info("解析data数据失败");
            return null;
        }

        if (data == null) {
            return null;
        }
        DeptUser deptUser = new DeptUser();
        deptUser.setId(data.getLong("empid"));
        deptUser.setName(data.getString("empname"));
        deptUser.setNick(data.getString("empname"));
        deptUser.setDeptId(data.getLong("orgid"));
        deptUser.setDeptName(data.getString("orgName"));
        return deptUser;
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
        List<User> userList = this.getUserById(userIds.stream().distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().collect(Collectors.toMap(User::getId, data -> data, (k1, k2) -> k1));
    }

    @Override
    public List<UserCommonResult> getByName(String userName) {
        return troUserDAO.selectByName(userName);
    }

    @Override
    public List<User> getUserById(List<Long> userIds) {
        List<User> userList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(userIds)) {
            return userList;
        }
        userList = TUserMapper.selectByIds(userIds);
        return userList;
//        List<User> users = new ArrayList<>();
//        userIds.forEach(userId -> {
//            User user = getUserById(userId);
//            if (user != null) {
//                users.add(user);
//            }
//        });
//        return users;
    }

    private User getUserById(Long userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId.toString());
        params.put("authId", YundaBaseInfo.authId);
        String result = HttpUtils.sendGet0(YundaBaseInfo.YUNDA_URL + YundaBaseInfo.YUNDA_GET_USER_INFO, params);
        log.info("韵达获取用户信息，{}", result);
        if (result == null) {
            return null;
        }
        JSONObject resultJsonObject = JSON.parseObject(result);
        String retcode = resultJsonObject.getString("retcode");
        if (!YundaBaseInfo.YUNDA_SUCCESS_RETURN.equals(retcode)) {
            return null;
        }
        JSONObject userData = resultJsonObject.getJSONObject("userData");
        if (userData == null) {
            return null;
        }
        User user = new User();
        user.setId(userData.getLong("userId"));
        user.setName(userData.getString("userName"));
        user.setNick(userData.getString("userName"));
        return user;
    }

    //    private String getYundaToken(){
    //
    //        HashMap<String, String> param = new HashMap<>();
    //        param.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
    //        param.put("secretKey",YundaBaseInfo.YUNDA_DEPT_SECRET_KEY);
    //        String s = HttpClientUtils.getInstance().sendHttpPost(YundaBaseInfo.YUNDA_DEPT_URL+YundaBaseInfo
    //        .YUNDA_DEPT_GET_TOKEN,param);
    //        if (StringUtils.isEmpty(s)){
    //            return null;
    //        }
    //        return s.replaceAll("\"","");
    //    }

    private PageInfo<DeptUser> getUserListByUserAndDept(PageInfo<DeptUser> resultPage, DeptUserQueryParam param) {
        String yundaToken = deptService.getYundaToken();
        JSONObject jsonParam = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_USER_INFO_BY_DEPT_USER;
        jsonParam.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        jsonParam.put("token", yundaToken);
        String orgId = "";
        for (String deptId : param.getDeptIds()) {
            orgId = deptId + ",";
        }
        orgId = orgId.substring(0, orgId.length() - 1);
        jsonParam.put("orgId", orgId);
        jsonParam.put("empParam", param.getUserName());
        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, jsonParam.toString());
        if (orgData == null) {
            return resultPage;
        }
        JSONArray data = orgData.getJSONArray("data");
        if (CollectionUtils.isEmpty(data)) {
            return resultPage;
        }
        List<DeptUser> deptUsers = new ArrayList<>();
        if (data.size() > param.getPageSize() * (param.getCurrent() + 1)) {
            for (int i = param.getCurrent() * param.getPageSize(); i < param.getPageSize() * (param.getCurrent() + 1);
                 i++) {
                converterUser(deptUsers, data.get(i));
            }
        } else {
            for (int i = param.getCurrent() * param.getPageSize(); i < data.size(); i++) {
                converterUser(deptUsers, data.get(i));
            }
        }
        resultPage.setList(deptUsers);
        resultPage.setTotal(data.size());
        return resultPage;
    }

    private void converterUser(List<DeptUser> deptUsers, Object obj) {
        JSONObject o = JSON.parseObject(obj.toString());
        if (o == null) {
            return;
        }
        DeptUser deptUser = new DeptUser();
        deptUser.setId(o.getLong("empid"));
        deptUser.setName(o.getString("empname"));
        deptUser.setNick(o.getString("empname"));
        deptUser.setDeptId(o.getLong("orgid"));
        deptUser.setDeptName(o.getString("orgName"));
        deptUsers.add(deptUser);
    }

    /**
     * 根据组织id查询用户信息
     *
     * @param resultPage
     * @param param
     * @return
     */
    private PageInfo<DeptUser> getUserListByDeptIds(PageInfo<DeptUser> resultPage, DeptUserQueryParam param) {
        List<String> deptIds = param.getDeptIds();
        //韵达这边暂不支持多个部门id查询用户信息，根据当前情况，目前只会传一个进来
        String deptId = deptIds.get(0);
        String yundaToken = deptService.getYundaToken();
        JSONObject jsonParam = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_USER_INFO;
        jsonParam.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        jsonParam.put("token", yundaToken);
        jsonParam.put("orgId", deptId);
        //不填代表只查当前子部门，填Y代表遍历查询所有子部门
        jsonParam.put("lowe", "Y");
        jsonParam.put("startTime", "1980-01-01 00:00:00");
        jsonParam.put("endTime", DateUtils.dateToString(new Date(), DateUtils.FORMATE_YMDHMS));
        jsonParam.put("startPage", param.getCurrent() + 1);
        jsonParam.put("pageSize", param.getPageSize());

        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, jsonParam.toString());
        if (orgData == null) {
            return resultPage;
        }
        Long maxPage = orgData.getLong("maxPage");
        JSONArray data = orgData.getJSONArray("data");
        if (CollectionUtils.isEmpty(data)) {
            return resultPage;
        }
        List<DeptUser> deptUsers = new ArrayList<>();
        for (Object obj : data) {
            JSONObject o = JSON.parseObject(obj.toString());
            if (o == null) {
                continue;
            }
            DeptUser deptUser = new DeptUser();
            deptUser.setId(o.getLong("empid"));
            deptUser.setName(o.getString("empname"));
            deptUser.setNick(o.getString("empname"));
            deptUser.setDeptId(o.getLong("orgid"));
            deptUser.setDeptName(o.getString("zkostlName"));
            deptUsers.add(deptUser);
        }

        resultPage.setList(deptUsers);
        if (deptUsers.size() < param.getPageSize()) {
            resultPage.setTotal(param.getCurrent() * param.getPageSize() + deptUsers.size());
        } else {
            resultPage.setTotal(maxPage * param.getPageSize());
        }
        return resultPage;
    }

    //    private PageInfo<DeptUser> getUserList(PageInfo<DeptUser> resultPage,DeptUserQueryParam param){
    //        Map<String,String> pamirs = new HashMap<>();
    //        pamirs.put("authId", YundaBaseInfo.authId);
    //        JSONObject jsonObject = new JSONObject();
    //        //特殊逻辑，韵达的起始页是从1开始的
    //        jsonObject.put("currentPage",param.getCurrent()+1);
    //        jsonObject.put("pageSize",param.getPageSize());
    //        pamirs.put("pageCond",jsonObject.toJSONString());
    //        String result = HttpClientUtils.getInstance().sendHttpPost(YundaBaseInfo.YUNDA_URL + YundaBaseInfo
    //        .YUNDA_GET_USER_LIST, pamirs);
    //        if (result == null){
    //            return resultPage;
    //        }
    //        JSONObject resultJsonObject = JSONObject.parseObject(result);
    //        String retcode = resultJsonObject.getString("retcode");
    //        if (!YundaBaseInfo.YUNDA_SUCCESS_RETURN.equals(retcode)){
    //            return null;
    //        }
    //        JSONArray users = resultJsonObject.getJSONArray("users");
    //        if (CollectionUtils.isEmpty(users)){
    //            return resultPage;
    //        }
    //        resultPage.setPageSize(param.getPageSize());
    //        List<DeptUser> deptUserList = new ArrayList<>();
    //        for (Object userObj : users){
    //            JSONObject userJsonObject = JSONObject.parseObject(userObj.toString());
    //            DeptUser deptUser = new DeptUser();
    //            deptUser.setId(userJsonObject.getLong("userId"));
    //            deptUser.setNick(userJsonObject.getString("userName"));
    //            deptUser.setName(userJsonObject.getString("userName"));
    //            deptUser.setDeptName(userJsonObject.getString("orgName"));
    //            deptUser.setDeptId(userJsonObject.getLong("orgCode"));
    //            deptUserList.add(deptUser);
    //        }
    //        resultPage.setList(deptUserList);
    //        Long totalRecord = resultJsonObject.getLong("totalRecord");
    //        resultPage.setTotal(totalRecord);
    //        return resultPage;
    //    }

}
