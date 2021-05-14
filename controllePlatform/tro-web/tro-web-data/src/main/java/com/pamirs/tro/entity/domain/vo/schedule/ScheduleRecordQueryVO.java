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

package com.pamirs.tro.entity.domain.vo.schedule;

import java.io.Serializable;

import com.pamirs.tro.entity.domain.PagingDevice;
import lombok.Data;

/**
 * @ClassName ScheduleRecordQueryVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午2:02
 */
@Data
public class ScheduleRecordQueryVO extends PagingDevice implements Serializable {

    private Long sceneId;
}
