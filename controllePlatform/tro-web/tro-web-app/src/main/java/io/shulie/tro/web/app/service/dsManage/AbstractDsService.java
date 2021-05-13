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

package io.shulie.tro.web.app.service.dsManage;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.AppConstants;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.data.result.application.ApplicationDsResult;

/**
 * @author HengYu
 * @className AbstractDsService
 * @date 2021/4/12 9:25 下午
 * @description 数据源存储抽象服务
 */
public abstract class AbstractDsService {

    /**
     * 添加数据源
     *
     * @param createRequest 创建对象
     * @return 响应对象
     */
    public abstract Response dsAdd(ApplicationDsCreateRequest createRequest);

    /**
     * 更新数据源
     *
     * @param updateRequest 更新对象
     * @return 响应对象
     */
    public abstract Response dsUpdate(ApplicationDsUpdateRequest updateRequest);

    /**
     * 查询数据源类型
     *
     * @param dsId
     * @param isOldVersion
     * @return
     */
    public abstract Response<ApplicationDsDetailResponse> dsQueryDetail(Long dsId, boolean isOldVersion);

    /**
     * 启用配置
     * @param enableRequest
     * @return
     */
    public abstract Response enableConfig(ApplicationDsEnableRequest enableRequest);

    /**
     * 删除数据源
     * @param dsDeleteRequest
     * @return
     */
    public abstract Response dsDelete(ApplicationDsDeleteRequest dsDeleteRequest);



}
