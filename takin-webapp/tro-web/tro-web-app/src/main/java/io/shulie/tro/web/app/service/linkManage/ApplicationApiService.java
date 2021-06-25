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

package io.shulie.tro.web.app.service.linkManage;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.vo.entracemanage.ApiCreateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.ApiUpdateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.EntranceApiVo;
import io.shulie.tro.web.app.common.Response;

/**
 * @Auther: vernon
 * @Date: 2020/4/2 13:10
 * @Description:
 */
public interface ApplicationApiService {

    Response registerApi(Map<String, List<String>> register);

    Response pullApi(String appName);

    Response delete(String id);

    Response query(EntranceApiVo vo);

    Response update(ApiUpdateVo vo);

    Response create(ApiCreateVo vo);

    Response queryDetail(String id);
}
