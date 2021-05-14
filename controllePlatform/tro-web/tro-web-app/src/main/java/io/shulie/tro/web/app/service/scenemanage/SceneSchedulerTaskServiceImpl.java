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

package io.shulie.tro.web.app.service.scenemanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.domain.vo.report.SceneActionParam;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.controller.openapi.SceneManageOpenApi;
import io.shulie.tro.web.app.controller.openapi.response.scenemanage.SceneManageOpenApiResp;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskCreateRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskQueryRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskUpdateRequest;
import io.shulie.tro.web.app.response.scenemanage.SceneSchedulerTaskResponse;
import io.shulie.tro.web.data.dao.scenemanage.SceneSchedulerTaskDao;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskInsertParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskQueryParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskUpdateParam;
import io.shulie.tro.web.data.result.scenemanage.SceneSchedulerTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-12-01 10:36
 * @Description:
 */
@Slf4j
@Service
public class SceneSchedulerTaskServiceImpl implements SceneSchedulerTaskService {

    @Autowired
    private SceneSchedulerTaskDao sceneSchedulerTaskDao;

    @Autowired
    private SceneTaskService sceneTaskService;

    @Override
    public Long insert(SceneSchedulerTaskCreateRequest request) {
        if (request == null) {
            return null;
        }
        if (request.getSceneId() == null) {
            throw new TroWebException(ExceptionCode.SCENE_SCHEDULER_TASK_SCENE_ID_VALID_ERROR,
                "sceneId can not be null");
        }
        verificationScheduleTime(request.getExecuteTime());
        SceneSchedulerTaskResult result = sceneSchedulerTaskDao.selectBySceneId(request.getSceneId());
        if (result != null) {
            return result.getId();
        }
        SceneSchedulerTaskInsertParam insertParam = new SceneSchedulerTaskInsertParam();
        BeanUtils.copyProperties(request, insertParam);
        return sceneSchedulerTaskDao.create(insertParam);

    }

    /**
     * 定时压测时间必须在当前时间1分钟之后
     *
     * @param time
     */
    public void verificationScheduleTime(Date time) {
        //if (StringUtils.isBlank(time)) {
        //    if (StringUtils.isBlank(time)) {
        //        throw new TroWebException(ExceptionCode.SCENE_SCHEDULER_TASK_EXECUTE_TIME_VALID_ERROR,
        //            "executeTime can not be null");
        //    }
        //}

       // Date date = DateUtils.strToDate(time, DateUtils.FORMATE_YMDHM);
        if (time==null){
            throw new TroWebException(ExceptionCode.SCENE_SCHEDULER_TASK_EXECUTE_TIME_VALID_ERROR,
                            "executeTime can not be null");
        }
        if (time.getTime() - System.currentTimeMillis() < 1000*60) {
            throw new TroWebException(ExceptionCode.SCENE_SCHEDULER_TASK_EXECUTE_TIME_VALID_ERROR, "定时执行时间需要大于当前时间1分钟");
        }
    }

    @Override
    public void delete(Long id) {
        sceneSchedulerTaskDao.delete(id);
    }

    @Override
    public void update(SceneSchedulerTaskUpdateRequest updateRequest,Boolean needVerify) {
        if (needVerify!=null && needVerify==true){
            verificationScheduleTime(updateRequest.getExecuteTime());
        }
        SceneSchedulerTaskUpdateParam updateParam = new SceneSchedulerTaskUpdateParam();
        BeanUtils.copyProperties(updateRequest, updateParam);
        sceneSchedulerTaskDao.update(updateParam);
    }

    @Override
    public SceneSchedulerTaskResponse selectBySceneId(Long sceneId) {
        SceneSchedulerTaskResult result = sceneSchedulerTaskDao.selectBySceneId(sceneId);
        if (result == null) {
            return null;
        }
        SceneSchedulerTaskResponse response = new SceneSchedulerTaskResponse();
        BeanUtils.copyProperties(result, response);
        return response;
    }

    @Override
    public void deleteBySceneId(Long sceneId) {
        if (sceneId == null) {
            return;
        }
        sceneSchedulerTaskDao.deleteBySceneId(sceneId);
    }

    @Override
    public List<SceneSchedulerTaskResponse> selectBySceneIds(List<Long> sceneIds) {
        if (CollectionUtils.isEmpty(sceneIds)) {
            return Lists.newArrayList();
        }
        List<SceneSchedulerTaskResult> resultList = sceneSchedulerTaskDao.selectBySceneIds(sceneIds);
        return result2RespList(resultList);
    }

    @Override
    public List<SceneSchedulerTaskResponse> selectByExample(SceneSchedulerTaskQueryRequest request) {
        if (request == null) {
            return Lists.newArrayList();
        }
        SceneSchedulerTaskQueryParam queryParam = new SceneSchedulerTaskQueryParam();
        BeanUtils.copyProperties(request, queryParam);
        List<SceneSchedulerTaskResult> resultList = sceneSchedulerTaskDao.selectByExample(queryParam);
        return result2RespList(resultList);
    }

    @Override
    public void executeSchedulerPressureTask() {
        SceneSchedulerTaskQueryRequest request = new SceneSchedulerTaskQueryRequest();
        Date previousNSecond = DateUtils.getPreviousNSecond(-67);
        String time = DateUtils.dateToString(previousNSecond, DateUtils.FORMATE_YMDHM);
        request.setEndTime(time);
        List<SceneSchedulerTaskResponse> responseList = this.selectByExample(request);
        if (CollectionUtils.isEmpty(responseList)) {
            return;
        }
        for (SceneSchedulerTaskResponse scheduler : responseList) {
            if (scheduler.getExecuteTime()==null || scheduler.getIsExecuted() == null
                || scheduler.getIsExecuted() != 0) {
                continue;
            }
            Date dbDate = scheduler.getExecuteTime();
            Date now = new Date();
            if (dbDate.before(now)) {
                //执行
                SceneActionParam startParam = new SceneActionParam();
                startParam.setSceneId(scheduler.getSceneId());
                startParam.setUid(scheduler.getUserId());


                new Thread(() -> {
                    try {
                        sceneTaskService.startTask(startParam);
                    } catch (Exception e) {
                        log.error("执行定时压测任务失败...", e);
                    } finally {
                        SceneSchedulerTaskUpdateRequest updateRequest = new SceneSchedulerTaskUpdateRequest();
                        updateRequest.setId(scheduler.getId());
                        updateRequest.setIsExecuted(2);
                        updateRequest.setIsDeleted(true);
                        this.update(updateRequest,false);
                    }
                }).start();
            }
        }

    }

    List<SceneSchedulerTaskResponse> result2RespList(List<SceneSchedulerTaskResult> resultList) {
        if (CollectionUtils.isEmpty(resultList)) {
            Lists.newArrayList();
        }
        List<SceneSchedulerTaskResponse> responseList = new ArrayList<>();
        resultList.stream().forEach(result -> {
            SceneSchedulerTaskResponse response = new SceneSchedulerTaskResponse();
            BeanUtils.copyProperties(result, response);
            responseList.add(response);
        });
        return responseList;
    }

}
