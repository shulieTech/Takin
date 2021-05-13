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

package io.shulie.tro.cloud.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.shulie.tro.cloud.common.bean.FlowVO;
import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.common.enums.PressureTypeEnums;
import io.shulie.tro.cloud.common.enums.TimeUnitEnum;

/**
 * @ClassName FlowUtil
 * @Description
 * @Author qianshui
 * @Date 2020/5/13 上午10:40
 */
public class FlowUtil {

    public static BigDecimal calcEstimateFlow(FlowVO wrapperVO) {
        //走之前的计算逻辑
        if (PressureTypeEnums.isConcurrency(wrapperVO.getPressureType())){
            return calcConcurrentFlow(wrapperVO);
        }
        //自定义
        if (PressureTypeEnums.isPersonalization(wrapperVO.getPressureType())){
            return new BigDecimal(1);
        }
        //tps预估流量
        if (PressureTypeEnums.isTps(wrapperVO.getPressureType())){
            Long time = wrapperVO.getPressureTestTime().getTime();
            if (TimeUnitEnum.SECOND.getValue().equals(wrapperVO.getPressureTestTime().getUnit())){
                time = time % 60 == 0 ? time / 60 : time / 60 + 1;
            }
            return new BigDecimal(500).multiply(new BigDecimal(time));
        }
        return new BigDecimal(0);
    }

    public static void main(String[] args) {
        FlowVO wrapperVO = new FlowVO();
        wrapperVO.setConcurrenceNum(100);
        wrapperVO.setPressureTestTime(new TimeBean(5L, "m"));
        wrapperVO.setPressureMode(3);
        wrapperVO.setIncreasingTime(new TimeBean(2L, "m"));
        wrapperVO.setStep(4);
        System.out.println(calcEstimateFlow(wrapperVO));
    }

    public static BigDecimal calcRealityFlow(FlowVO flow) {
        //走之前的计算逻辑
        if (PressureTypeEnums.isConcurrency(flow.getPressureType())){
            return calcConcurrentFlow(flow);
        }

        if (PressureTypeEnums.isTps(flow.getPressureType()) || PressureTypeEnums.isPersonalization(flow.getPressureType())){
            return calcFlowByRealityConcurrent(flow);
        }

        return new BigDecimal(0);
    }

    /**
     * 根据实际的并发来计算
     * @param flowVO
     * @return
     */
    public static BigDecimal calcFlowByRealityConcurrent(FlowVO flowVO){
        Long time = flowVO.getPressureTestTime().getTime();
        if (TimeUnitEnum.SECOND.getValue().equals(flowVO.getPressureTestTime().getUnit())){
            time = time % 60 == 0 ? time / 60 : time / 60 + 1;
        }
        return flowVO.getAvgConcurrent().multiply(new BigDecimal(time));
    }

    /**
     * 计算并发模式流量
     * @return
     */
    public static BigDecimal calcConcurrentFlow(FlowVO wrapperVO){
        Integer threadNum = wrapperVO.getConcurrenceNum();
        Long ptDuration = wrapperVO.getPressureTestTime().getTime();
        String ptUnit = wrapperVO.getPressureTestTime().getUnit();
        int ptMode = wrapperVO.getPressureMode();
        Long incDuration = wrapperVO.getIncreasingTime() != null ? wrapperVO.getIncreasingTime().getTime() : null;
        String incUnit = wrapperVO.getIncreasingTime() != null ? wrapperVO.getIncreasingTime().getUnit() : null;

        if (ptMode == 1) {
            incDuration = 0L;
            incUnit = TimeUnitEnum.MINUTE.getValue();
        }

        if (TimeUnitEnum.SECOND.getValue().equals(ptUnit)) {
            ptDuration = (ptDuration - 1) / 60 + 1;
        }

        if (TimeUnitEnum.SECOND.getValue().equals(incUnit)) {
            incDuration = (incDuration - 1) / 60 + 1;
        }

        /**
         * 如果中间停止，会出现递增时长>压测时长的情况
         */
        if(incDuration > ptDuration) {
            if(ptMode == 3 && wrapperVO.getStep() != null) {
                Long step = ptDuration / (incDuration / wrapperVO.getStep());
                wrapperVO.setStep(step == 0L ? 1 : step.intValue());
            }
            incDuration = ptDuration;
        }

        BigDecimal threadNumDecimal = new BigDecimal(Integer.valueOf(threadNum));
        BigDecimal ptDurationDecimal = new BigDecimal(Long.valueOf(ptDuration));
        BigDecimal incDurationDecimal = new BigDecimal(Long.valueOf(incDuration));

        BigDecimal one = new BigDecimal("1");
        BigDecimal two = new BigDecimal("2");

        if (ptMode == 1 || ptMode == 2) {
            return threadNumDecimal.multiply(incDurationDecimal).divide(two, 10, RoundingMode.HALF_UP)
                    .add(threadNumDecimal.multiply(ptDurationDecimal.subtract(incDurationDecimal)))
                    .setScale(5, RoundingMode.HALF_UP);
        }
        BigDecimal stepDecimal = wrapperVO.getStep() != null ? new BigDecimal(wrapperVO.getStep()) : one;
        return (stepDecimal.subtract(one)).divide(stepDecimal, 10, RoundingMode.HALF_UP)
                .multiply(incDurationDecimal).multiply(threadNumDecimal).divide(two, 10, RoundingMode.HALF_UP)
                .add((ptDurationDecimal.subtract(incDurationDecimal)
                        .add(incDurationDecimal.multiply(one).divide(stepDecimal, 10, RoundingMode.HALF_UP)))
                        .multiply(threadNumDecimal))
                .setScale(5, RoundingMode.HALF_UP);
    }
}
