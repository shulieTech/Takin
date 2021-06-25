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

package io.shulie.tro.web.app.request.scenemanage;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-02 19:08
 * @Description:
 */

@Data
public class SceneSchedulerTaskQueryRequest {

    /**
     * 执行时间大于时间
     */
    private String startTime ;

    /**
     * 执行时间小于时间
     */
    private String endTime ;


}
