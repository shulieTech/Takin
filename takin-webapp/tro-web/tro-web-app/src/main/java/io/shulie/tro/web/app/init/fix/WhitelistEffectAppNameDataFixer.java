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
import java.util.Map;
import java.util.stream.Collectors;

import io.shulie.tro.web.data.dao.application.WhiteListDAO;
import io.shulie.tro.web.data.dao.application.WhitelistEffectiveAppDao;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistUpdatePartAppNameParam;
import io.shulie.tro.web.data.result.whitelist.WhitelistEffectiveAppResult;
import io.shulie.tro.web.data.result.whitelist.WhitelistResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
* @Package io.shulie.tro.web.app.init.fix
* @author 无涯
* @description:
* @date 2021/4/7 12:00 下午
*/
@Component
@Slf4j
public class WhitelistEffectAppNameDataFixer {

    @Autowired
    private WhitelistEffectiveAppDao whitelistEffectiveAppDao;
    @Autowired
    private WhiteListDAO whiteListDAO;

    public void fix() {
        log.info("开始订正白名单生效应用的白名单类型字段");
        WhitelistEffectiveAppSearchParam searchParam = new WhitelistEffectiveAppSearchParam();
        List<WhitelistEffectiveAppResult> appResults = whitelistEffectiveAppDao.getList(searchParam);

        if(appResults.stream().noneMatch(e -> e.getType() == null)) {
            log.info("没有可订正白名单生效应用的白名单类型字段的数据");
            return;
        }
        List<Long> ids = appResults.stream().map(WhitelistEffectiveAppResult::getId).distinct().collect(Collectors.toList());
        WhitelistSearchParam param = new WhitelistSearchParam();
        param.setIds(ids);
        List<WhitelistResult> whitelistResults =  whiteListDAO.getList(param);
        Map<Long,List<WhitelistResult>> whitelistResultsMap = whitelistResults.stream().collect(Collectors.groupingBy(WhitelistResult::getWlistId));
        List<WhitelistUpdatePartAppNameParam> updatePartAppNameParams = appResults.stream().map(result -> {
            WhitelistUpdatePartAppNameParam partAppNameParam = new WhitelistUpdatePartAppNameParam();
            List<WhitelistResult> results = whitelistResultsMap.get(result.getWlistId());
            partAppNameParam.setId(result.getId());
            if(!CollectionUtils.isEmpty(results)) {
                partAppNameParam.setType(results.get(0).getType());
            }
            return partAppNameParam;
        }).collect(Collectors.toList());
        whitelistEffectiveAppDao.updatePartAppName(updatePartAppNameParams);
    }
}
