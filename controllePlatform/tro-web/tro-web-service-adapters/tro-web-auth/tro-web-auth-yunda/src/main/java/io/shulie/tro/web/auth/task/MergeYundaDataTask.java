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

package io.shulie.tro.web.auth.task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.Role;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.auth.common.YundaBaseInfo;
import io.shulie.tro.web.auth.service.DeptServiceImpl;
import io.shulie.tro.web.auth.utils.HttpClientUtils;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.mapper.mysql.TroDeptMapper;
import io.shulie.tro.web.data.mapper.mysql.TroUserMapper;
import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import io.shulie.tro.web.data.model.mysql.TroRoleUserRelationEntity;
import io.shulie.tro.web.data.model.mysql.TroUserDeptRelationEntity;
import io.shulie.tro.web.data.model.mysql.TroUserEntity;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author ZhangXT
 * @Description 合并韵达用户、部门数据
 * @Date 2020/11/13 15:47
 */
@Component
@Slf4j
public class MergeYundaDataTask {


    @Resource
    DeptServiceImpl deptServiceImpl;
    @Autowired
    TroDeptMapper troDeptMapper;
    @Autowired
    DeptService deptService;
    @Autowired
    RoleService roleService;
    @Autowired
    private TroUserMapper troUserMapper;
    @Autowired
    private TroDeptUserRelationDAO troDeptUserRelationDAO;
    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;


    //每页查询条数
    private static final int PAGESIZE = 100;
    //韵达默认角色id
    private static final List<Long> defaultRoleIds = Arrays.asList(1L, 2L);

    /**
     * 批量查询，每批条数（分页太多韵达接口可能会超时）
     */
    private static final Integer MAX_NUMBER = 5;

    /**
     * 同步韵达用户定时任务，每天凌晨执行一次
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void mergeYundaUser() {
        int count = 0;
        log.debug("同步韵达用户定时任务开始！");
        while (true) {
            List<TroUserEntity> userInfo = getUserInfo(count);
            if (CollectionUtils.isEmpty(userInfo)) {
                break;
            }
            //用户批量的插入or更新的操作
            troUserMapper.saveOrUpdateUserBatch(userInfo);
            if (userInfo.size() < PAGESIZE) {
                break;
            }
            count++;
            sleep();
        }
        log.debug("同步韵达用户定时任务结束！");
    }

    /**
     * 给韵达接口的压力不要太大，慢慢查
     */
    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //ignore
        }
    }


    /**
     * 分配默认角色定时任务，每天凌晨执行一次
     */
    @Scheduled(cron = "0 4 0 * * ?")
    public void defaultRole2User() {
        for (Long defaultRoleId : defaultRoleIds) {
            //判断默认角色是否存在
            List<Role> roles = roleService.getRolesByIds(Arrays.asList(defaultRoleId));
            //不存在-新增默认角色-新增角色对应的数据权限
            if (CollectionUtils.isEmpty(roles)) {
                log.error("韵达分配默认角色定时任务，默认角色为空，defaultRoleId：{}", defaultRoleId);
                continue;
            }
            //存在-继续

            //没有默认角色的用户
            List<Long> userIds = troUserMapper.getNoDefaultRoleUserIds(defaultRoleId);
            log.debug("韵达分配默认角色定时任务，未分配默认角色的用户ids：{}", userIds);
            if (CollectionUtils.isEmpty(userIds)) {
                continue;
            }
            List<TroRoleUserRelationEntity> list = Lists.newArrayList();
            //分配默认角色
            for (Long userId : userIds) {
                TroRoleUserRelationEntity entity = new TroRoleUserRelationEntity();
                entity.setRoleId(String.valueOf(defaultRoleId));
                entity.setUserId(String.valueOf(userId));
                list.add(entity);
            }
            int i = troRoleUserRelationDAO.batchInsert(list);
            log.debug("韵达分配默认角色定时任务，成功给【{}】个用户分配默认角色", i);
        }

    }

    /**
     * 同步韵达部门定时任务，每天凌晨执行一次
     */
    @Scheduled(cron = "0 2 0 * * ?")
    public void mergeDept() {
        log.debug("同步韵达部门定时任务开始！");
        int count = 0;
        while (true) {
            List<TroDeptEntity> depts = mergeYundaDept(count);
            if (CollectionUtils.isEmpty(depts)) {
                break;
            }
            //部门批量的插入or更新的操作
            troDeptMapper.saveOrUpdateDeptBatch(depts);
            //没数据，或数据少于分页条数，则跳出
            if (depts.size() < PAGESIZE) {
                break;
            }
            count++;
            sleep();
        }
        log.debug("同步韵达部门定时任务结束！");

    }

    /**
     * 同步韵达用户部门关系定时任务，每天凌晨执行一次
     */
    @Scheduled(cron = "0 3 0 * * ?")
    public void mergeDeptUserRelation() {
        log.debug("同步韵达用户部门关系定时任务开始！");
        //AllDepts
        List<Dept> depts = deptService.getAllDepts("");
        //部门ids
        List<Long> deptIds = depts.stream().map(Dept::getId).collect(Collectors.toList());


        int limit = countStep(deptIds.size());
        //拆分
        List<List<Long>> collect = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> deptIds.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList())).collect(Collectors.toList());

        List<TroUserDeptRelationEntity> deptList = new ArrayList<>();
        for (List<Long> requestList : collect) {
            String orgId = requestList.stream().map(Objects::toString).collect(Collectors.joining(","));
            //根据部门id查询部门下员工
            List<TroUserDeptRelationEntity> dept = getUserListByUserAndDept(orgId);
            deptList.addAll(dept);
            sleep();
        }

        Map<Long, List<TroUserDeptRelationEntity>> listMap = deptList.stream().filter(data -> null != data.getDeptId()).collect(Collectors.groupingBy(data -> Long.valueOf(data.getDeptId())));

        List<TroUserDeptRelationEntity> insertList = Lists.newArrayList();

        for (Map.Entry<Long, List<TroUserDeptRelationEntity>> entry : listMap.entrySet()) {
            UserDeptConditionQueryParam deptParam = new UserDeptConditionQueryParam();
            deptParam.setDeptIds(Arrays.asList(Objects.toString(entry.getKey())));
            //根据部门id查询用户信息
            List<UserDeptConditionResult> resultList = troDeptUserRelationDAO.selectUserByDeptIds(deptParam);
            if (CollectionUtils.isEmpty(resultList)) {
                // 新增
                troDeptUserRelationDAO.batchInsert(entry.getValue());
                continue;
            }

            Map<String, TroUserDeptRelationEntity> dataMap = entry.getValue().stream().collect(Collectors.toMap(data -> data.getUserId(), data -> data, (k1, k2) -> k1));
            Map<String, UserDeptConditionResult> dbDeptMap = resultList.stream().collect(Collectors.toMap(data -> Objects.toString(data.getId()), data -> data, (k1, k2) -> k1));

            //韵达有，数据库中没有，新增 key:用户id
            dataMap.forEach((k, v) -> {
                if (!dbDeptMap.containsKey(k)) {
                    insertList.add(v);
                }
            });

            /** 接口超时会导致数据丢失，暂时先不走删除逻辑*/
//            List<String> deleteUserIds = Lists.newArrayList();
//            //数据库有此用户，韵达没有此用户，删除数据库中用户 key:用户id
//            dbDeptMap.forEach((k, v) -> {
//                if (!dataMap.containsKey(k)) {
//                    deleteUserIds.add(k);
//                }
//            });
//            if (CollectionUtils.isEmpty(deleteUserIds)) {
//                continue;
//            }
//            UserDeptRelationBatchDeleteParam deleteParam = new UserDeptRelationBatchDeleteParam()
//            deleteParam.setDeptId(entry.getKey());
//            deleteParam.setUserIds(deleteUserIds);
//            troDeptUserRelationDAO.deleteByDeptIdAndUserIds(deleteParam);
        }

        // 韵达有，数据库中没有，新增
        if (CollectionUtils.isNotEmpty(insertList)) {
            log.debug("韵达有，数据库中没有，新增数据：{}条！", insertList.size());
            troDeptUserRelationDAO.batchInsert(insertList);
        }
        log.debug("同步韵达用户部门关系定时任务结束！");


    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }


    /**
     * 根据部门id查询部门下员工
     *
     * @param orgId
     * @return
     */
    private List<TroUserDeptRelationEntity> getUserListByUserAndDept(String orgId) {
        String yundaToken = deptServiceImpl.getYundaToken();
        List<TroUserDeptRelationEntity> deptUsers = Lists.newArrayList();
        JSONObject jsonParam = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_USER_INFO_BY_DEPT_USER;
        jsonParam.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        jsonParam.put("token", yundaToken);
        jsonParam.put("orgId", orgId);
        jsonParam.put("empParam", "");
        jsonParam.put("lowe", "N");
        //超时抛异常会导致数据丢失
        log.debug("根据部门id查询部门下员工，url：{}，param：{}", url, jsonParam.toJSONString());
        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, jsonParam.toString());
        log.debug("根据部门id查询部门下员工，result：{}", orgData);

        if (orgData == null) {
            return deptUsers;
        }
        JSONArray data = orgData.getJSONArray("data");
        if (CollectionUtils.isEmpty(data)) {
            return deptUsers;
        }
        for (Object obj : data) {
            JSONObject o = JSON.parseObject(obj.toString());
            TroUserDeptRelationEntity deptUser = new TroUserDeptRelationEntity();
            if (o == null) {
                continue;
            }
            deptUser.setUserId(o.getString("empid"));
            deptUser.setDeptId(o.getString("orgid"));
            deptUsers.add(deptUser);
        }

        return deptUsers;
    }

    /**
     * 查询用户信息
     *
     * @param current
     * @return
     */
    private List<TroUserEntity> getUserInfo(int current) {

        List<TroUserEntity> deptUsers = new ArrayList<>();

        //韵达这边暂不支持多个部门id查询用户信息，根据当前情况，目前只会传一个进来，只同步韵达科技板块组织
        String deptId = YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID;
        String yundaToken = deptServiceImpl.getYundaToken();
        JSONObject jsonParam = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_USER_INFO;
        jsonParam.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        jsonParam.put("token", yundaToken);
        jsonParam.put("orgId", deptId);
        //不填代表只查当前部门人员，填Y代表遍历查询部门下所有人员
        jsonParam.put("lowe", "Y");
        jsonParam.put("startTime", "1980-01-01 00:00:00");
        jsonParam.put("endTime", DateUtils.dateToString(new Date(), DateUtils.FORMATE_YMDHMS));
        jsonParam.put("startPage", current + 1);
        jsonParam.put("pageSize", PAGESIZE);
        log.debug("查询韵达用户信息，url：{}，param：{}", url, jsonParam.toString());
        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, jsonParam.toString());
        log.debug("查询韵达用户信息，result：{}", orgData);
        if (orgData == null) {
            return deptUsers;
        }
        Long maxPage = orgData.getLong("maxPage");
        JSONArray data = orgData.getJSONArray("data");
        if (CollectionUtils.isEmpty(data)) {
            return deptUsers;
        }
        for (Object obj : data) {
            JSONObject o = JSON.parseObject(obj.toString());
            if (o == null) {
                continue;
            }
            TroUserEntity user = new TroUserEntity();
            user.setId(o.getLong("empid"));
            user.setName(o.getString("empname"));
            user.setNick(o.getString("empname"));
            user.setModel(1);
            user.setRole(2);
            user.setUserType(1);
            user.setCustomerId(1L);
            deptUsers.add(user);
        }
        return deptUsers;
    }


    /**
     * @return
     */
    private List<TroDeptEntity> mergeYundaDept(int current) {
        String yundaToken = deptServiceImpl.getYundaToken();
        List<TroDeptEntity> depts = new ArrayList<>();
        JSONObject param = new JSONObject();
        String url = YundaBaseInfo.YUNDA_DEPT_URL + YundaBaseInfo.YUNDA_DEPT_GET_HCM_ORG;
        param.put("appId", YundaBaseInfo.YUNDA_DEPT_APP_ID);
        param.put("token", yundaToken);
        //韵达科技板块组织id
        param.put("orgId", YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID);
        //不填代表只查当前子部门，填Y代表遍历查询所有子部门
        param.put("lowe", "Y");
        param.put("startTime", "1980-01-01 00:00:00");
        param.put("endTime", DateUtils.dateToString(new Date(), DateUtils.FORMATE_YMDHMS));

        param.put("startPage", current + 1);
        param.put("pageSize", PAGESIZE);
        log.debug("查询韵达部门信息，url：{}，param：{}", url, param.toString());
        JSONObject orgData = HttpClientUtils.getInstance().sendHttpPostWithJson(url, param.toString());
        log.debug("查询韵达部门信息，result：{}", orgData);
        if (orgData == null) {
            return depts;
        }
        JSONArray data = orgData.getJSONArray("data");
        if (CollectionUtils.isEmpty(data)) {
            return depts;
        }
        for (Object obj : data) {
            JSONObject o = JSON.parseObject(obj.toString());
            if (o == null) {
                continue;
            }
            TroDeptEntity dept = new TroDeptEntity();
            dept.setId(o.getLong("orgid"));
            dept.setParentId(o.getLong("parentorgid"));
            //认为科技板块为最高节点
            if (YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID.equals(o.getString("orgid"))) {
                dept.setParentId(null);
            }
            dept.setCode(o.getString("orgcode"));
            dept.setLevel(o.getString("orglevel"));
            dept.setName(o.getString("orgname"));
            dept.setRefId(o.getString("orgid"));
            depts.add(dept);
        }

        return depts;
    }

    public JSONObject sendHttpPostWithJson(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");

        JSONObject result = new JSONObject();
        StringEntity stringEntity = new StringEntity(params, Charset.forName("UTF-8"));
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse res = httpClient.execute(httpPost);
        String s = EntityUtils.toString(res.getEntity());
        result = JSONObject.parseObject(s);
        res.close();
        return result;
    }


}
