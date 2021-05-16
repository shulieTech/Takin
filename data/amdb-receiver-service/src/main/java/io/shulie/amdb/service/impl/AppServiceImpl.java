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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.mapper.AppMapper;
import io.shulie.amdb.request.query.TAmdbAppBatchAppQueryRequest;
import io.shulie.amdb.response.app.AmdbAppResponse;
import io.shulie.amdb.response.app.model.InstanceInfo;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import io.shulie.amdb.utils.PagingUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppServiceImpl implements AppService {
    @Resource
    private AppMapper appMapper;

    @Autowired
    private AppInstanceService appInstanceService;

    @Override
    public Response insert(AppDO record) {
        try {
            appMapper.insertSelective(record);
            return Response.success(record.getId());
        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                //唯一键冲突，新增转更新
                AppDO t = appMapper.selectOneByParam(record);
                record.setId(t.getId());
                appMapper.updateByPrimaryKeySelective(record);
                return Response.success(t.getId());
            } else {
                return Response.fail(e.getMessage());
            }
        }
    }

    @Override
    @Async
    public void insertAsync(AppDO record) {
        appMapper.inUpdateSelective(record);
    }

    @Override
    public int insertBatch(List<AppDO> tAmdbApps) {
        appMapper.insertList(tAmdbApps);
        return 1;
    }

    @Override
    public int update(AppDO tAmdbApp) {
        return appMapper.updateByPrimaryKeySelective(tAmdbApp);
    }

    @Override
    public int updateBatch(List<AppDO> tAmdbApps) {
        tAmdbApps.forEach(tAmdbApp -> {
            update(tAmdbApp);
        });
        return 1;
    }

    @Override
    public int delete(AppDO tAmdbApp) {
        return appMapper.deleteByPrimaryKey(tAmdbApp.getId());
    }

    @Override
    public AppDO selectByPrimaryKey(AppDO tAmdbApp) {
        return appMapper.selectByPrimaryKey(tAmdbApp.getId());
    }

    @Override
    public AppDO selectOneByParam(AppDO tAmdbApp) {
        return appMapper.selectOneByParam(tAmdbApp);
    }

    @Override
    public List<AppDO> selectByFilter(String filter) {
        return appMapper.selectByFilter(filter);
    }

    @Override
    public PageInfo<AmdbAppResponse> selectByBatchAppParams(TAmdbAppBatchAppQueryRequest param) {
        Example example = new Example(AppDO.class);
        Example.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(param.getAppIds())) {
            criteria.andIn("id", param.getAppIds());
        }
        if (!CollectionUtils.isEmpty(param.getAppNames())) {
            criteria.andIn("appName", param.getAppNames());
        }
        if (StringUtils.isNotBlank(param.getTenantKey())) {
            criteria.andEqualTo("tenant", param.getTenantKey());
        }
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<AppDO> amdbApps = appMapper.selectByExample(example);
        List<AmdbAppResponse> amdbAppResponseParams = amdbApps.stream().map(amdbApp -> new AmdbAppResponse(param.getFields(), amdbApp)).collect(Collectors.toList());
        amdbAppResponseParams.forEach(amdbAppResponseParam -> {
            boolean appIsException = appInstanceService.getExceptionInstanceCount(amdbAppResponseParam.getAppId()) > 0;
            if (param.getFields().contains("instanceInfo")) {
                InstanceInfo instanceInfo = new InstanceInfo();
                int totalCount = appInstanceService.getAllInstanceCount(amdbAppResponseParam.getAppId());
                int onlineCount = appInstanceService.getOnlineInstanceCount(amdbAppResponseParam.getAppId());
                instanceInfo.setInstanceAmount(totalCount);
                instanceInfo.setInstanceOnlineAmount(onlineCount);
                amdbAppResponseParam.setInstanceInfo(instanceInfo);
                // 如果在线节点数为0，则表示应用异常
                if (onlineCount == 0) {
                    appIsException = true;
                }
            } else {
                // 如果没有查实例信息，且应用是无异常状态，再加个实例在线数判断
                if (!appIsException) {
                    int onlineCount = appInstanceService.getOnlineInstanceCount(amdbAppResponseParam.getAppId());
                    if (onlineCount == 0) {
                        appIsException = true;
                    }
                }
            }
            amdbAppResponseParam.setAppIsException(appIsException);
        });
        return PagingUtils.result(amdbApps, amdbAppResponseParams);
    }

    @Override
    public List<String> selectAllAppName(TAmdbAppBatchAppQueryRequest param){
        String sql = "select app_name as appName from t_amdb_app where app_type in ('APP','virtual')";
        List<LinkedHashMap> result = appMapper.selectBySql(sql);
        return result.stream().map(map -> ObjectUtils.toString(map.get("appName"))).collect(Collectors.toList());
    }
}
