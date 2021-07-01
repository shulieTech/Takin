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

package io.shulie.amdb.service;

import com.github.pagehelper.PageInfo;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.request.query.TAmdbAppBatchAppQueryRequest;
import io.shulie.amdb.response.app.AmdbAppResponse;

import java.util.List;

public interface AppService {
    Response insert(AppDO tAmdbApp);

    void insertAsync(AppDO tAmdbApp);

    int insertBatch(List<AppDO> tAmdbApps);

    int update(AppDO tAmdbApp);

    int updateBatch(List<AppDO> tAmdbApps);

    int delete(AppDO tAmdbApp);

    AppDO selectByPrimaryKey(AppDO tAmdbApp);

    AppDO selectOneByParam(AppDO tAmdbApp);

    List<AppDO> selectByFilter(String filter);

    PageInfo<AmdbAppResponse> selectByBatchAppParams(TAmdbAppBatchAppQueryRequest param);

    List<String> selectAllAppName(TAmdbAppBatchAppQueryRequest param);
}
