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

package io.shulie.tro.cloud.biz.service.cloudServer;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.entity.cloudserver.CloudAccount;
import com.pamirs.tro.entity.domain.query.CloudAccountQueryParam;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudAccountVO;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @Author: mubai
 * @Date: 2020-05-09 21:28
 * @Description:
 */
public interface CloudAccountService {

    ResponseResult addCloudAccount(CloudAccount cloudAccount);

    ResponseResult deleteById(Long id);

    ResponseResult updateById(CloudAccount cloudAccount);

    ResponseResult queryById(Long id);

    PageInfo<CloudAccountVO> queryPageInfo(CloudAccountQueryParam param);

}
