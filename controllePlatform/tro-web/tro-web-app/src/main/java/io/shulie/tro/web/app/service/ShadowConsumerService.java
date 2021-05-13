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

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.agent.vo.ShadowConsumerVO;
import io.shulie.tro.web.app.request.application.ShadowConsumerCreateRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerQueryRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerUpdateRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerUpdateUserRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumersOperateRequest;
import io.shulie.tro.web.app.response.application.ShadowConsumerResponse;

/**
 * @author shiyajian
 * create: 2021-02-04
 */
public interface ShadowConsumerService {

    List<ShadowConsumerResponse> getShadowConsumersByApplicationId(long applicationId);

    ShadowConsumerResponse getMqConsumerById(Long id);

    PagingList<ShadowConsumerResponse> pageMqConsumers(ShadowConsumerQueryRequest request);

    void createMqConsumers(ShadowConsumerCreateRequest request);

    void updateMqConsumers(ShadowConsumerUpdateRequest request);

    void deleteMqConsumers(List<Long> id);

    void operateMqConsumers(ShadowConsumersOperateRequest request);

    List<ShadowConsumerVO> agentSelect(String namespace);

    int allocationUser(ShadowConsumerUpdateUserRequest request);
}
