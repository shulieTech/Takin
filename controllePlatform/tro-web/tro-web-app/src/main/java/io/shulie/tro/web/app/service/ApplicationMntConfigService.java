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

package io.shulie.tro.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.domain.entity.TApplicationMntConfig;
import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.AgentApplicationConfigVo;
import com.pamirs.tro.entity.domain.vo.TApplicationMntConfigVo;
import io.shulie.tro.web.app.common.CommonService;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.service.user.TroWebUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMntConfigService extends CommonService {

    @Autowired
    private TroWebUserService troWebUserService;

    /**
     * 分页查应用配置
     *
     * @param paramMap
     * @return
     */
    public PageInfo<TApplicationMntConfigVo> queryApplicationConfigPage(Map<String, Object> paramMap) {
        PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        List<TApplicationMntConfigVo> voList = tApplicationMntConfigDao.queryApplicationConfigPage(paramMap);
        return new PageInfo<>(CollectionUtils.isEmpty(voList) ? Lists.newArrayList() : voList);
    }

    /**
     * agent获取 所有配置
     *
     * @param applicationName
     * @return
     */
    public AgentApplicationConfigVo queryConfig(String applicationName) {
        AgentApplicationConfigVo vo = new AgentApplicationConfigVo();
        vo.setCheatCheck(Constants.N);
        //如果应用名称不为空
        if (StringUtils.isNotEmpty(applicationName)) {
            User user = RestContext.getUser();
            if (user == null) {
                user = troWebUserService.queryUserByKey(RestContext.getTenantUserKey());
            }
            TApplicationMntConfig tApplicationMntConfig = tApplicationMntConfigDao.queryByApplicationNameAndUserId(
                applicationName, user == null ? null : user.getId());
            if (tApplicationMntConfig != null && Constants.INTEGER_USE.equals(tApplicationMntConfig.getCheatCheck())) {
                vo.setCheatCheck(Constants.Y);
            }
        }
        //查询全局配置
        TBaseConfig tBaseConfig = tbaseConfigDao.selectByPrimaryKey(ConfigConstants.SQL_CHECK);
        vo.setSqlCheck(Constants.N);
        //不为空  且值是数字  且 是 1
        if (tBaseConfig != null && NumberUtils.isDigits(tBaseConfig.getConfigValue()) && Constants.INTEGER_USE.equals(
            Integer.parseInt(tBaseConfig.getConfigValue()))) {
            vo.setSqlCheck(Constants.Y);
        }
        return vo;
    }

    /**
     * 批量更新
     *
     * @param configVo
     */
    public void updateConfigBatch(TApplicationMntConfigVo configVo) {
        int cheatCheck = Integer.parseInt(configVo.getCheatCheck());
        List<String> applicationIdList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(
            configVo.getApplicationIds()).stream().distinct().collect(Collectors.toList());
        //先根据应用查出所有的
        List<TApplicationMntConfig> configList = tApplicationMntConfigDao.queryByApplicationIdList(applicationIdList);
        Date now = new Date();
        if (CollectionUtils.isEmpty(configList)) {
            configList = new ArrayList<>();
            for (String appId : applicationIdList) {
                TApplicationMntConfig mntConfig = new TApplicationMntConfig();
                mntConfig.setTamcId(snowflake.next());
                mntConfig.setApplicationId(Long.parseLong(appId));
                mntConfig.setCheatCheck(cheatCheck);
                mntConfig.setCreateTime(now);
                mntConfig.setUpdateTime(now);
                configList.add(mntConfig);
            }
            tApplicationMntConfigDao.insertList(configList);
        } else {
            batchInsertAndUpdate(cheatCheck, applicationIdList, configList, now);
        }
    }

    /**
     * 批量插入 与 更新
     *
     * @param cheatCheck
     * @param applicationIdList
     * @param configList
     * @param now
     */
    private void batchInsertAndUpdate(int cheatCheck, List<String> applicationIdList,
        List<TApplicationMntConfig> configList, Date now) {
        //先批量更新
        tApplicationMntConfigDao.updateCheatRuleByApplicationIdList(applicationIdList, cheatCheck);
        //如果数量不对需要插入
        if (configList.size() != applicationIdList.size()) {
            List<String> configAppIdList = new ArrayList<>();
            for (TApplicationMntConfig config : configList) {
                configAppIdList.add(config.getApplicationId().toString());
            }
            applicationIdList.removeAll(configAppIdList);
            configList = new ArrayList<>();
            for (String appId : applicationIdList) {
                TApplicationMntConfig mntConfig = new TApplicationMntConfig();
                mntConfig.setTamcId(snowflake.next());
                mntConfig.setApplicationId(Long.parseLong(appId));
                mntConfig.setCheatCheck(cheatCheck);
                mntConfig.setCreateTime(now);
                mntConfig.setUpdateTime(now);
                configList.add(mntConfig);
            }
            tApplicationMntConfigDao.insertList(configList);
        }
    }

}
