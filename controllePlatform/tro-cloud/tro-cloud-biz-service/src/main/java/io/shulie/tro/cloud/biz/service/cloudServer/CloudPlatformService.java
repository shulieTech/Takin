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

import com.pamirs.tro.entity.domain.entity.cloudserver.CloudPlatform;
import com.pamirs.tro.entity.domain.query.CloudPlatformQueryParam;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudPlatformVO;
import io.shulie.tro.cloud.common.page.PageInfo;
import io.shulie.tro.common.beans.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: mubai
 * @Date: 2020-05-11 11:00
 * @Description:
 */
public interface CloudPlatformService {

    ResponseResult addCloudPlatform(CloudPlatform platform);

    ResponseResult deleteById(Long id);

    ResponseResult updateById(CloudPlatform platform);

    ResponseResult queryById(Long id);

    ResponseResult queryByExample();

    ResponseResult uploadSDK(MultipartFile file, String fileName);

    PageInfo<CloudPlatformVO> getPageList(CloudPlatformQueryParam param);

    ResponseResult confirmCloudPlatform(CloudPlatform param);

    ResponseResult editSDK(String name, String jarName, Long id);
}
