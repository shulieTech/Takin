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
import java.util.List;

import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.web.app.input.whitelist.WhitelistImportFromExcelInput;
import io.shulie.tro.web.app.request.application.ShadowConsumerCreateRequest;
import io.shulie.tro.web.data.param.application.ApplicationDsCreateParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistCreateNewParam;

/**
 * @Author: mubai
 * @Date: 2021-03-01 11:00
 * @Description:
 */
public interface AppConfigEntityConvertService {

    List<ApplicationDsCreateParam> convertDsSheet(ArrayList<ArrayList<String>> sourceList);

    List<LinkGuardVo> convertGuardSheet(ArrayList<ArrayList<String>> sourceList);

    List<TShadowJobConfig> convertJobSheet(ArrayList<ArrayList<String>> sourceList);

    List<WhitelistImportFromExcelInput> converWhiteList(ArrayList<ArrayList<String>> sourceList);

    /**
     * 转换黑名单
     * @param sourceList
     * @return
     */
    List<BlacklistCreateNewParam> converBlackList(ArrayList<ArrayList<String>> sourceList,Long applicationId);

    List<ShadowConsumerCreateRequest> converComsumerList(ArrayList<ArrayList<String>> sourceList);
}
