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

package io.shulie.amdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.mapper.AppInstanceMapper;
import io.shulie.amdb.request.query.TAmdbAppInstanceBatchAppQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceErrorInfoByQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceQueryRequest;
import io.shulie.amdb.response.instance.AmdbAppInstanceResponse;
import io.shulie.amdb.response.instance.InstanceErrorInfoResponse;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.utils.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppInstanceServiceImpl implements AppInstanceService {
    @Resource
    AppInstanceMapper appInstanceMapper;

    @Override
    public Response insert(TAmdbAppInstanceDO record) {
        try {
            appInstanceMapper.insertSelective(record);
            return Response.success(record.getId());
        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException|| e instanceof DuplicateKeyException) {
                //唯一键冲突，新增转更新
                TAmdbAppInstanceDO t = appInstanceMapper.selectOneByParam(record);
                record.setId(t.getId());
                appInstanceMapper.updateByPrimaryKeySelective(record);
                return Response.success(t.getId());
            } else {
                log.error("新增失败",e);
                return Response.fail(e.getMessage());
            }
        }
    }

    @Override
    public Response insertBatch(List<TAmdbAppInstanceDO> tAmdbApps) {
        try {
            if (!CollectionUtils.isEmpty(tAmdbApps)) {
                tAmdbApps.forEach(record -> {
                    try {
                        appInstanceMapper.insertSelective(record);
                    } catch (Exception e) {
                        if (e instanceof MySQLIntegrityConstraintViolationException) {
                            //唯一键冲突，新增转更新
                            TAmdbAppInstanceDO t = appInstanceMapper.selectOneByParam(record);
                            appInstanceMapper.updateByExampleSelective(t, record);
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }catch (Exception e){
            return Response.fail(e.getMessage());
        }
        return Response.emptySuccess();
    }

    @Override
    public TAmdbAppInstanceDO selectOneByParam(TAmdbAppInstanceDO instance) {
        return appInstanceMapper.selectOneByParam(instance);
    }

    @Override
    public int update(TAmdbAppInstanceDO record) {
        return appInstanceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateBatch(List<TAmdbAppInstanceDO> tAmdbApps) {
        if (!CollectionUtils.isEmpty(tAmdbApps)) {
            tAmdbApps.forEach(record -> {
                appInstanceMapper.updateByPrimaryKey(record);
            });
            return 1;
        }
        return 0;
    }

    @Override
    public PageInfo<AmdbAppInstanceResponse> selectByParams(TAmdbAppInstanceQueryRequest param) {
        int page = param.getCurrentPage();
        int pageSize = param.getPageSize();
        String filter = "app_name='" + param.getAppName() + "' ";
        if (param.getCustomerId() != null && param.getCustomerId().trim().length() > 0) {
            filter += "and customer_id ='" + param.getCustomerId() + "'";
        }
        PageHelper.startPage(page, pageSize);
        List<TAmdbAppInstanceDO> amdbApps = appInstanceMapper.selectByFilter(filter);
        List<AmdbAppInstanceResponse> amdbAppResponseParams = amdbApps.stream().map(amdbApp -> new AmdbAppInstanceResponse(amdbApp)).collect(Collectors.toList());
        return PagingUtils.result(amdbApps, amdbAppResponseParams);
    }

    @Override
    public Integer getAllInstanceCount(Long appId) {
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appId", appId);
        return appInstanceMapper.selectCountByExample(example);
    }

    /**
     * 获取在线实例列表
     *
     * @param appId
     * @return
     */
    @Override
    public Integer getOnlineInstanceCount(Long appId) {
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appId", appId);
        criteria.andCondition("(flag&1)=1");
        return appInstanceMapper.selectCountByExample(example);
    }

    /**
     * 获取异常实例列表
     *
     * @param appId
     * @return
     */
    @Override
    public Integer getExceptionInstanceCount(Long appId) {
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appId", appId);
        criteria.andCondition("(flag&2)!=2");
        criteria.andCondition("(flag&1)=1");
        return appInstanceMapper.selectCountByExample(example);
    }

    @Override
    public PageInfo<AmdbAppInstanceResponse> selectByBatchAppParams(TAmdbAppInstanceBatchAppQueryRequest param) {
        int page = param.getCurrentPage();
        int pageSize = param.getPageSize();
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(param.getAppIds())) {
            criteria.andIn("appId", param.getAppIds());
        }
        if (!CollectionUtils.isEmpty(param.getAppNames())) {
            criteria.andIn("appName", param.getAppNames());
        }
        if (!CollectionUtils.isEmpty(param.getAgentIds())) {
            criteria.andIn("agentId", param.getAgentIds());
        }
        if (!CollectionUtils.isEmpty(param.getIpAddress())) {
            criteria.andIn("ip", param.getIpAddress());
        }
        if (StringUtils.isNotBlank(param.getTenantKey())) {
            criteria.andEqualTo("customer_id", param.getTenantKey());
        }
        criteria.andCondition("(flag&1)=1");
        PageHelper.startPage(page, pageSize);
        List<TAmdbAppInstanceDO> amdbApps = appInstanceMapper.selectByExample(example);
        List<AmdbAppInstanceResponse> amdbAppResponseParams = amdbApps.stream().map(amdbApp -> new AmdbAppInstanceResponse(amdbApp)).collect(Collectors.toList());
        return PagingUtils.result(amdbApps, amdbAppResponseParams);
    }

    @Override
    public List<InstanceErrorInfoResponse> selectErrorInfoByParams(TAmdbAppInstanceErrorInfoByQueryRequest param) {
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(param.getAppId())) {
            criteria.andEqualTo("appId", param.getAppId());
        }
        if (StringUtils.isNotBlank(param.getAppName())) {
            criteria.andEqualTo("appName", param.getAppName());
        }
        criteria.andCondition("(flag&1)=1");
        List<TAmdbAppInstanceDO> amdbAppInstances = appInstanceMapper.selectByExample(example);
        Map<String, InstanceErrorInfoResponse> errorMaps = new HashMap<>();
        if (CollectionUtils.isEmpty(amdbAppInstances)) {
            return new ArrayList<>();
        }
        amdbAppInstances.forEach(amdbAppInstance -> {
            String ext = amdbAppInstance.getExt();
            JSONObject jsonObject = JSON.parseObject(ext);
            JSONObject errorMsgInfos = jsonObject.getJSONObject("errorMsgInfos");
            if (CollectionUtils.isEmpty(errorMsgInfos)) {
                return;
            }
            Set<String> keySet = errorMsgInfos.keySet();
            keySet.forEach(key -> {
                JSONObject errorMsgInfoDetail = (JSONObject) errorMsgInfos.get(key);
                if (errorMaps.get(key) == null) {
                    errorMaps.put(key, new InstanceErrorInfoResponse());
                }
                InstanceErrorInfoResponse responseParam = errorMaps.get(key);
                responseParam.setId(key);
                responseParam.getAgentIds().add(amdbAppInstance.getAgentId());
                responseParam.setTime(DateFormatUtils.format((Long) errorMsgInfoDetail.get("time"), "yyyy-MM-dd HH:mm:ss"));
                responseParam.setDescription((String) errorMsgInfoDetail.get("msg"));
            });
        });
        return CollectionUtils.arrayToList(errorMaps.values().toArray());
    }

    @Override
    public void initOnlineStatus() {
        appInstanceMapper.initOnlineStatus();
    }

    @Override
    public void deleteByParams(TAmdbAppInstanceQueryRequest param) {
        Example example = new Example(TAmdbAppInstanceDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("appName", Arrays.asList(param.getAppName().split(",").clone()));
        if (StringUtils.isNotBlank(param.getIp())) {
            criteria.andEqualTo("ip", param.getIp());
        }
        if (StringUtils.isNotBlank(param.getAgentId())) {
            criteria.andEqualTo("agentId", param.getAgentId());
        }
        appInstanceMapper.deleteByExample(example);
    }
}
