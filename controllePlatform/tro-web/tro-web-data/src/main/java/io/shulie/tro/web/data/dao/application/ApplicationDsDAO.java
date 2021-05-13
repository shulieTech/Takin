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

package io.shulie.tro.web.data.dao.application;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.DsModelWithBLOBs;
import io.shulie.tro.web.data.param.application.ApplicationDsCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationDsDeleteParam;
import io.shulie.tro.web.data.param.application.ApplicationDsEnableParam;
import io.shulie.tro.web.data.param.application.ApplicationDsQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateUserParam;
import io.shulie.tro.web.data.result.application.ApplicationDsResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/9 8:26 下午
 * @Description:
 */
public interface ApplicationDsDAO {
    int insert(ApplicationDsCreateParam createParam);

    int update(ApplicationDsUpdateParam updateParam);

    /**
     * 启动状态
     * @param enableParam
     * @return
     */
    int enable(ApplicationDsEnableParam enableParam);

    int delete(ApplicationDsDeleteParam deleteParam);

    ApplicationDsResult queryByPrimaryKey(Long id);

    List<ApplicationDsResult> queryList(ApplicationDsQueryParam param);

    int allocationUser(ApplicationDsUpdateUserParam param);

    List<DsModelWithBLOBs> selectByAppIdForAgent(Long applicationId);

    List<DsModelWithBLOBs> getAllEnabledDbConfig(Long applicationId);
}
