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

package io.shulie.tro.cloud.biz.service.cloudServer.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import io.shulie.tro.cloud.biz.cloudserver.CloudAccountEntityConvert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.dao.cloudserver.TCloudAccountMapper;
import com.pamirs.tro.entity.dao.cloudserver.TCloudPlatformMapper;
import com.pamirs.tro.entity.domain.entity.cloudserver.CloudAccount;
import com.pamirs.tro.entity.domain.entity.cloudserver.CloudPlatform;
import com.pamirs.tro.entity.domain.query.CloudAccountQueryParam;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudAccountVO;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudAccountService;

/**
 * @Author: mubai
 * @Date: 2020-05-11 10:58
 * @Description:
 */

@Service
@Slf4j
public class CloudAccountServiceImpl implements CloudAccountService {

    public static String false_code = "0";
    @Resource
    TCloudPlatformMapper TCloudPlatformMapper;
    @Resource
    private TCloudAccountMapper TCloudAccountMapper;

    @Override
    public ResponseResult addCloudAccount(CloudAccount cloudAccount) {
        if (cloudAccount == null || cloudAccount.getPlatformId() == null || StringUtils.isBlank(
            cloudAccount.getAccount())) {
            return ResponseResult.fail(false_code, "platformId | account can not be null !", "");
        }
        CloudPlatform platform = TCloudPlatformMapper.selectByPrimaryKey(cloudAccount.getPlatformId());
        if (platform == null) {
            return ResponseResult.fail(false_code, "平台id对应的平台不存在 ", "");
        }
        cloudAccount.setPlatformName(platform.getName());
        try {
            TCloudAccountMapper.insertSelective(cloudAccount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(false_code, "新增云账号失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        if (id == null) {
            return ResponseResult.fail(false_code, "id 不能为空");
        }
        try {
            TCloudAccountMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(false_code, "删除失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult updateById(CloudAccount cloudAccount) {
        if (cloudAccount == null || cloudAccount.getId() == null) {
            return ResponseResult.fail(false_code, "id 不能为空", "");
        }
        try {
            TCloudAccountMapper.updateByPrimaryKeySelective(cloudAccount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(false_code, "更新失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult queryById(Long id) {
        if (id == null) {
            return ResponseResult.fail(false_code, "id 不能为空");
        }
        CloudAccount account = TCloudAccountMapper.selectByPrimaryKey(id);
        if (account != null) {
            return ResponseResult.success(CloudAccountEntityConvert.INSTAMCE.of(account));
        }
        return ResponseResult.fail(false_code, "未查到授权账号", "");
    }

    @Override
    public PageInfo<CloudAccountVO> queryPageInfo(CloudAccountQueryParam param) {
        Page<Object> page = PageHelper.startPage(param.getCurrentPage() + 1, param.getPageSize());
        List<CloudAccount> cloudAccounts = TCloudAccountMapper.selectByExample(param);
        if (cloudAccounts.isEmpty()) {
            return new PageInfo<>(new ArrayList<>());
        }
        PageInfo pageInfo = new PageInfo(CloudAccountEntityConvert.INSTAMCE.ofs(cloudAccounts));
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

}
