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

package io.shulie.tro.cloud.biz.service.scene.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.vo.engine.EngineNotifyParam;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageStartRecordVO;
import io.shulie.tro.cloud.biz.service.report.ReportService;
import io.shulie.tro.cloud.biz.service.scene.EngineCallbackService;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.common.constants.ReportConstans;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.enums.engine.EngineStatusEnum;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 何仲奇
 * @Package io.shulie.tro.scenemanage.poll.impl
 * @date 2020/9/23 2:59 下午
 */
@Service
@Slf4j
public class EngineCallbackServiceImpl implements EngineCallbackService {

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private ReportService reportService;
    // todo 本地缓存，之后都需要改成redis 解决redis锁问题，以及redis 缓存延迟问题
    private static Map<String,Integer> engine = Maps.newConcurrentMap();

    @Override
    public ResponseResult notifyEngineState(EngineNotifyParam notify) {

        String engineName = ScheduleConstants.getEngineName(notify.getSceneId(), notify.getResultId(),
            notify.getCustomerId());
        String scheduleName = ScheduleConstants.getScheduleName(notify.getSceneId(), notify.getResultId(),
            notify.getCustomerId());
        switch (EngineStatusEnum.getJmeterStatusEnum(notify.getStatus())) {
            case START_FAILED:
                log.info("本次压测{}-{}-{},jmeter 启动失败：{}", notify.getSceneId(), notify.getResultId(),
                    notify.getCustomerId(), notify.getMsg());
                // 记录压测引擎 调jmeter 相关错误  这里给出具体哪个 pod 调用 jmeter 失败 todo 之后可以指定到具体的pod
                String tempFailSign = ScheduleConstants.TEMP_FAIL_SIGN + engineName;
                engine.compute(tempFailSign,(k,v) -> v == null ? 1 : v +1);
                // 记录失败原因，成功则不记录报告中 报告直接完成
                reportService.updateReportFeatures(notify.getResultId(), ReportConstans.FINISH_STATUS,
                    ReportConstans.PRESSURE_MSG, notify.getMsg());

                // 如果 这个失败等于 pod 数量 则 将本次压测至为失败
                int podTotal = Integer.parseInt(
                    redisClientUtils.getString(ScheduleConstants.getPodTotal(notify.getSceneId(), notify.getResultId(),
                        notify.getCustomerId())));
                int tempFailTotal = engine.get(tempFailSign);
                if (podTotal <= tempFailTotal) {
                    sceneManageService.reportRecord(SceneManageStartRecordVO.build(notify.getSceneId(),
                        notify.getResultId(),
                        notify.getCustomerId()).success(false).errorMsg("").build());
                }
                break;
            case INTERRUPT:
                //获取中断状态
                boolean interruptFlag = Boolean.parseBoolean(
                    redisClientUtils.getString(ScheduleConstants.INTERRUPT_POD + scheduleName));
                return ResponseResult.success(interruptFlag);
            case INTERRUPT_SUCCESSED:
                // 中断成功
                log.info("本次压测{}-{}-{} 中断成功", notify.getSceneId(), notify.getResultId(),
                    notify.getCustomerId());
                engine.compute(ScheduleConstants.INTERRUPT_POD + engineName,(k,v) -> v == null ? 1 : v +1);
                if(interruptFinish(engineName)) {
                    engine.remove(ScheduleConstants.INTERRUPT_POD + scheduleName);
                }
                break;
            case INTERRUPT_FAILED:
                // 中断失败
                log.info("本次压测{}-{}-{} 中断失败", notify.getSceneId(), notify.getResultId(), notify.getCustomerId());
                break;
            default: {}
        }
        return ResponseResult.success();
    }

    private boolean interruptFinish(String engineName) {
        if(engine.get(ScheduleConstants.INTERRUPT_POD + engineName).equals(engine.get(engineName))) {
            return true;
        }
        return false;
    }

}
