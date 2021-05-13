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

package io.shulie.tro.web.data.dao.leakverify;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.amdb.api.DataLeakAnalysisClient;
import io.shulie.tro.web.amdb.bean.result.leakverify.LeakVerifyDeployDTO;
import io.shulie.tro.web.amdb.bean.result.leakverify.LeakVerifyDeployDetailDTO;
import io.shulie.tro.web.data.converter.leakverify.LeakVerifyDeployConverter;
import io.shulie.tro.web.data.mapper.mysql.LeakVerifyDeployDetailMapper;
import io.shulie.tro.web.data.mapper.mysql.LeakVerifyDeployMapper;
import io.shulie.tro.web.data.model.mysql.LeakVerifyDeploy;
import io.shulie.tro.web.data.model.mysql.LeakVerifyDeployDetail;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyDeployDetailResult;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyDeployResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
@Deprecated
public class LeakVerifyDeployDAOImpl implements LeakVerifyDeployDAO{

    @Autowired
    private LeakVerifyDeployMapper leakVerifyDeployMapper;

    @Autowired
    private LeakVerifyDeployDetailMapper leakVerifyDeployDetailMapper;

    @Autowired
    private DataLeakAnalysisClient dataLeakAnalysisClient;


    @Override
    public List<LeakVerifyDeployResult> listLeakVerifyDeploy(Long leakVerifyId, boolean isCurrent) {
        if (isCurrent){
            List<LeakVerifyDeployDTO> dataLeakLists = dataLeakAnalysisClient.getDataLeakLists(leakVerifyId);
            return LeakVerifyDeployConverter.INSTANCE.ofListLeakVerifyDeployResultByDTO(dataLeakLists);
        }
        LambdaQueryWrapper<LeakVerifyDeploy> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                LeakVerifyDeploy::getId,
                LeakVerifyDeploy::getApplicationName,
                LeakVerifyDeploy::getBizLeakCount,
                LeakVerifyDeploy::getBizRequestCount,
                LeakVerifyDeploy::getEntryName,
                LeakVerifyDeploy::getGmtCreate,
                LeakVerifyDeploy::getGmtUpdate,
                LeakVerifyDeploy::getIsDeleted,
                LeakVerifyDeploy::getLeakVerifyId,
                LeakVerifyDeploy::getPressureLeakCount,
                LeakVerifyDeploy::getPressureRequestCount);
        wrapper.eq(LeakVerifyDeploy::getLeakVerifyId,leakVerifyId);
        List<LeakVerifyDeploy> leakVerifyDeploys = leakVerifyDeployMapper.selectList(wrapper);
        return LeakVerifyDeployConverter.INSTANCE.ofListLeakVerifyDeployResult(leakVerifyDeploys);
    }

    @Override
    public List<LeakVerifyDeployDetailResult> queryLeakVerifyDeployDetail(Long leakVerifyDeployId, Long leakVerifyId, String applicationName, String entryName) {
        //为空查询实况数据
        if (leakVerifyDeployId == null){
            List<LeakVerifyDeployDetailDTO> dataLeakDetails = dataLeakAnalysisClient.getDataLeakDetails(leakVerifyId, applicationName, entryName);
            return LeakVerifyDeployConverter.INSTANCE.ofListLeakVerifyDeployDetailResultByDTO(dataLeakDetails);
        }
        LambdaQueryWrapper<LeakVerifyDeployDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                LeakVerifyDeployDetail::getId,
                LeakVerifyDeployDetail::getFeature,
                LeakVerifyDeployDetail::getGmtCreate,
                LeakVerifyDeployDetail::getGmtUpdate,
                LeakVerifyDeployDetail::getLeakContent,
                LeakVerifyDeployDetail::getLeakCount,
                LeakVerifyDeployDetail::getLeakType,
                LeakVerifyDeployDetail::getLeakVerifyDeployId);
        wrapper.eq(LeakVerifyDeployDetail::getLeakVerifyDeployId,leakVerifyDeployId);
        List<LeakVerifyDeployDetail> leakVerifyDeployDetails = leakVerifyDeployDetailMapper.selectList(wrapper);
        return LeakVerifyDeployConverter.INSTANCE.ofListLeakVerifyDeployDetailResult(leakVerifyDeployDetails);
    }
}
