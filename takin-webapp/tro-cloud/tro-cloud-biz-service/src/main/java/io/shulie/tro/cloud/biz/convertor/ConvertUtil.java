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

package io.shulie.tro.cloud.biz.convertor;

import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.common.bean.FlowVO;

/**
 * @ClassName ConvertUtil
 * @Description
 * @Author qianshui
 * @Date 2020/5/26 下午7:49
 */
public class ConvertUtil {

    public static FlowVO convert(SceneManageWrapperInput sceneVO) {
        if (sceneVO == null) {
            return null;
        }
        FlowVO wrapperVO = new FlowVO();
        wrapperVO.setConcurrenceNum(sceneVO.getConcurrenceNum());
        wrapperVO.setPressureTestTime(sceneVO.getPressureTestTime());
        wrapperVO.setPressureMode(sceneVO.getPressureMode());
        wrapperVO.setIncreasingTime(sceneVO.getIncreasingTime());
        wrapperVO.setStep(sceneVO.getStep());
        wrapperVO.setPressureType(sceneVO.getPressureType());
        return wrapperVO;
    }
}
