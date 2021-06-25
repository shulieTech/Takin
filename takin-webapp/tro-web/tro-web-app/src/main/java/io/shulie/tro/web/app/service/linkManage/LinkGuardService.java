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

import com.pamirs.tro.entity.domain.entity.LinkGuardEntity;
import com.pamirs.tro.entity.domain.query.LinkGuardQueryParam;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.web.app.common.Response;

/**
 * @Author: 慕白
 * @Date: 2020-03-05 10:41
 * @Description:
 */
public interface LinkGuardService {

    Response addGuard(LinkGuardVo guardVo);

    Response updateGuard(LinkGuardVo guardVo);

    Response deleteById(Long id);

    Response<List<LinkGuardVo>> selectByExample(LinkGuardQueryParam param);

    List<LinkGuardVo> agentSelect(Long customerId, String appName);

    Response<List<LinkGuardEntity>> selectAll();

    Response getById(Long id);

    /**
     * 挡板开启、关闭开关
     *
     * @param id
     * @param target
     * @return
     */
    Response enableGuard(Long id, Boolean target);

    List<LinkGuardEntity> getAllEnabledGuard(String applicationId);
}
