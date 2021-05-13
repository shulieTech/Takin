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

package io.shulie.tro.cloud.open.req.scenemanage;

import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-16 19:33
 * @Description:
 */

@Data
public class SceneManageQueryByIdsReq  extends HttpCloudRequest implements Serializable {
    private static final long serialVersionUID = -3706985653572707716L;

    private List<Long> sceneIds;
}
