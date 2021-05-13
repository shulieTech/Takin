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

package io.shulie.tro.web.app.service.scene;

import java.util.List;

/**
 * @ClassName ApplicationBusinessActivityService
 * @Description
 * @Author qianshui
 * @Date 2020/8/12 上午9:50
 */
public interface ApplicationBusinessActivityService {

    /**
     * 根据业务活动id,查询关联应用名
     *
     * @param businessActivityId
     * @return
     */
    List<String> processAppNameByBusinessActiveId(Long businessActivityId);

}
