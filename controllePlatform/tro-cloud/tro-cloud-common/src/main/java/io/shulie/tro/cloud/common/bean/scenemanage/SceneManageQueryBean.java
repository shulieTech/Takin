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

package io.shulie.tro.cloud.common.bean.scenemanage;

import java.io.Serializable;
import java.util.List;

import io.shulie.tro.common.beans.page.PagingDevice;
import lombok.Data;

/**
 * @ClassName SceneManageQueryBean
 * @Description 场景列表查询
 * @Author qianshui
 * @Date 2020/4/17 下午2:18
 */
@Data
public class SceneManageQueryBean extends PagingDevice implements Serializable {

    private Long customId;

    private String customName;

    private Long sceneId;

    private String sceneName;

    private Integer status;

    /**
     * 压测场景类型：0普通场景，1流量调试
     */
    private Integer type;

    private List<Long> customIds;

    /**
     * 场景ids
     */
    private List<Long> sceneIds ;

    private String lastPtStartTime ;

    private String lastPtEndTime ;
}
