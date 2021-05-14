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

package io.shulie.tro.web.app.init.fix;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.shulie.tro.web.common.enums.blacklist.BlacklistTypeEnum;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.blacklist.BlackListDAO;
import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistCreateNewParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistSearchParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.blacklist.BlacklistResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* @Package io.shulie.tro.web.app.init.fix
* @author 无涯
* @description:
* @date 2021/4/7 12:00 下午
*/
@Component
@Slf4j
public class BlacklistDataFixer {

    @Value("${blacklist.data.fix.enable:false}")
    private Boolean enableBlacklistFix;
    @Autowired
    private BlackListDAO blackListDAO;
    @Autowired
    private ApplicationDAO applicationDAO;

    public void fix() {
        if (enableBlacklistFix) {
            log.info("开始补充每个应用的黑名单");
            BlacklistSearchParam param = new BlacklistSearchParam();
            List<BlacklistResult> results = blackListDAO.selectList(param);
            if(CollectionUtils.isEmpty(results)) {
                return;
            }
            List<BlacklistResult> noAppIdResult = results.stream()
                .filter(result -> result.getApplicationId() == null || result.getApplicationId() == 0L)
                .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(noAppIdResult)) {
                return;
            }
            ApplicationQueryParam queryParam = new ApplicationQueryParam();
            List<ApplicationDetailResult> detailResults = applicationDAO.getApplicationList(queryParam);
            List<BlacklistCreateNewParam> params = Lists.newArrayList();
            detailResults.forEach(detailResult -> {
                noAppIdResult.forEach(result -> {
                    BlacklistCreateNewParam newParam = new BlacklistCreateNewParam();
                    newParam.setApplicationId(detailResult.getApplicationId());
                    newParam.setCustomerId(detailResult.getCustomerId());
                    newParam.setType(BlacklistTypeEnum.REDIS.getType());
                    newParam.setUseYn(result.getUseYn());
                    newParam.setRedisKey(result.getRedisKey());
                    newParam.setUserId(detailResult.getUserId());
                    params.add(newParam);
                });
            });
            blackListDAO.batchInsert(params);
            // 删除软删源数据
            List<Long> ids =noAppIdResult.stream().map(BlacklistResult::getBlistId).collect(Collectors.toList());
            blackListDAO.logicalDelete(ids);
        }
    }
}
