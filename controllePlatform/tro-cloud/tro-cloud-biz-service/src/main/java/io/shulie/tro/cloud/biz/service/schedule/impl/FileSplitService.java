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

package io.shulie.tro.cloud.biz.service.schedule.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageStartRecordVO;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRunRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest.DataFile;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.biz.service.schedule.ScheduleService;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.constants.PressureInstanceRedisKey;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.constants.ScheduleEventConstant;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import io.shulie.tro.k8s.service.MicroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author 莫问
 * @Date 2020-08-07
 */

@Service
@Slf4j
public class FileSplitService {

    @Value("${console.url}")
    private String console;

    @Value("${nfs.server}")
    private String nfsServer;

    @Value("${nfs.file.dir}")
    private String nfsDir;

    @Value("${file.split.images}")
    private String fileSplitImages;

    @Value("${file.split.images.name}")
    private String fileSplitImagesName;

    @Autowired
    private ScheduleService localScheduleService;

    @Autowired
    private MicroService microService;

    @Autowired
    private SceneManageService sceneManageService;

    @IntrestFor(event = ScheduleEventConstant.INIT_SCHEDULE_EVENT)
    public void initSchedule(Event event) {
        fileSplit((ScheduleRunRequest)event.getExt());
    }

    /**
     * 分件分割
     */
    public void fileSplit(ScheduleRunRequest request) {
        ScheduleStartRequest startRequest = request.getRequest();
        int totalPod = startRequest.getTotalIp();
        boolean isSplit = preCheck(request);
        if (!isSplit) {
            //执行调度引擎
            localScheduleService.runSchedule(request);
            return;
        }
        // 启动中 ---->开始拆分文件中
        sceneManageService.updateSceneLifeCycle(UpdateStatusBean.build(request.getRequest().getSceneId(),
            request.getRequest().getTaskId(),
            request.getRequest().getCustomerId()).checkEnum(SceneManageStatusEnum.STARTING)
            .updateEnum(SceneManageStatusEnum.FILESPLIT_RUNNING).build());

        //拉取pod分割文件
        final Job job = new JobBuilder()
            .withNewMetadata()
            .withName(ScheduleConstants.getFileSplitScheduleName(startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId()))
            .endMetadata()
            .withNewSpec()
            .withNewTemplate()
            .withNewSpec()
            .addNewContainer()
            .withName(fileSplitImagesName)
            .withImage(fileSplitImages)
            .addNewVolumeMount()
            .withName(ScheduleConstants.ENGINE_SCRIPT_FILE_NAME)
            .withMountPath(ScheduleConstants.ENGINE_SCRIPT_FILE_PATH)
            .endVolumeMount()
            .withImagePullPolicy(ScheduleConstants.IMAGE_PULL_POLICY)
            .withEnv(buildContainerEnv(startRequest.getDataFile(), totalPod, startRequest.getSceneId(),
                startRequest.getTaskId(), startRequest.getCustomerId()))
            .endContainer()
            .addNewVolume()
            .withName(ScheduleConstants.ENGINE_SCRIPT_FILE_NAME)
            .withNewNfs()
            .withServer(nfsServer)
            .withPath(nfsDir)
            .endNfs()
            .endVolume()
            .withNewRestartPolicy(ScheduleConstants.RESTART_POLICY_NEVER)
            .endSpec()
            .endTemplate()
            .endSpec()
            .build();
        String msg = microService.createJob(job, PressureInstanceRedisKey.getEngineInstanceRedisKey(startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId()));
        if (StringUtils.isEmpty(msg)) {
            // 是空的
            log.info("场景{},任务{},顾客{}开始拆分文件，拆分文件job创建成功", startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId());
        } else {
            // 创建失败
            log.info("场景{},任务{},顾客{}开始拆分文件，拆分文件job创建失败", startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId());
            sceneManageService.reportRecord(SceneManageStartRecordVO.build(startRequest.getSceneId(),
                startRequest.getTaskId(), startRequest.getCustomerId()).success(false)
                .errorMsg(String.format("拆分文件失败，失败原因：" + msg)).build());
        }
    }

    /**
     * 前置检查
     *
     * @return
     */
    private boolean preCheck(ScheduleRunRequest request) {
        int totalPod = request.getRequest().getTotalIp();
        List<DataFile> dataFileList = request.getRequest().getDataFile();
        if (totalPod <= 1) {
            log.warn("场景调度ID：{},POD数量少于2,分件不做分割处理", request.getScheduleId());
            return false;
        }
        List<DataFile> list = dataFileList.stream().filter(DataFile::isSplit).collect(
            Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            log.warn("场景调度ID：{},没有可分割用的数据文件", request.getScheduleId());
            return false;
        }
        return true;
    }

    /**
     * 构建环境变量
     * 把脚本文件组装成分割文件的参数
     *
     * @return
     */
    private List<EnvVar> buildContainerEnv(List<DataFile> dataFileList, int totalPod, Long sceneId,
        Long taskId, Long customerId) {
        StringBuffer path = new StringBuffer();
        List<DataFile> list = dataFileList.stream().filter(DataFile::isSplit).collect(
            Collectors.toList());
        list.forEach(dataFile -> {
            path.append(dataFile.getPath()).append(":").append(totalPod).append(" ");
        });
        path.deleteCharAt(path.length() - 1);

        //拆分文件列表
        EnvVar envVar = new EnvVar();
        envVar.setName("fileData");
        envVar.setValue(path.toString());

        //拆完之后回调通知云端结果
        EnvVar callback = new EnvVar();
        callback.setName("callback");
        callback.setValue(getCallbackURL(sceneId, taskId, customerId, totalPod));

        //环境变量
        List<EnvVar> envList = Lists.newLinkedList();
        envList.add(envVar);
        envList.add(callback);
        return envList;
    }

    /**
     * 初始化完成之后回调通知
     *
     * @return
     */
    private String getCallbackURL(Long sceneId, Long taskId, Long customerId, int total) {
        // 这里会回调 通知可以createJob,文件拆分完成
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(console);
        if (!console.endsWith("/")) {stringBuffer.append("/");}
        stringBuffer.append("api/scene/task/initCallback?");
        stringBuffer.append("taskId=").append(taskId).append("&");
        stringBuffer.append("sceneId=").append(sceneId).append("&");
        stringBuffer.append("customerId=").append(customerId).append("&");
        stringBuffer.append("total=").append(total);
        return stringBuffer.toString();
    }
}
