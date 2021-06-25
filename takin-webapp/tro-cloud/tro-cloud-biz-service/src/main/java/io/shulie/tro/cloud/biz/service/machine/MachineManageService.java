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

package io.shulie.tro.cloud.biz.service.machine;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.query.MachineTaskLogQueryParm;
import com.pamirs.tro.entity.domain.query.MachineTaskQueryParam;
import com.pamirs.tro.entity.domain.query.PressureMachineQueryParam;
import com.pamirs.tro.entity.domain.vo.machine.MachineDeleteTaskVO;
import com.pamirs.tro.entity.domain.vo.machine.MachineTaskLogVO;
import com.pamirs.tro.entity.domain.vo.machine.MachineTaskVO;
import com.pamirs.tro.entity.domain.vo.machine.PressureMachineVO;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 上午10:46
 * @Description:
 */
public interface MachineManageService {

    /**
     * 开通机器任务
     *
     * @param machineTaskVO
     * @return
     */
    ResponseResult addMachineOpenTask(MachineTaskVO machineTaskVO);

    /**
     * 销毁机器任务
     *
     * @param machineDeleteTaskVO
     * @return
     */
    ResponseResult addMachineDestoryTask(MachineDeleteTaskVO machineDeleteTaskVO);

    /**
     * 任务列表
     *
     * @param param
     * @return
     */
    PageInfo<MachineTaskVO> queryMachineTaskPageInfo(MachineTaskQueryParam param);

    /**
     * 机器列表
     *
     * @param param
     * @return
     */
    PageInfo<PressureMachineVO> queryMachinePageInfo(PressureMachineQueryParam param);

    /**
     * 机器任务日志
     *
     * @param param
     * @return
     */
    PageInfo<MachineTaskLogVO> queryLogByTaskId(MachineTaskLogQueryParm param);
}
