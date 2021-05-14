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

package io.shulie.tro.cloud.open.constant;

/**
 * @author zhaoyong
 * cloud api接口路径
 */
public class CloudApiConstant {

    public static final String LICENSE_REQUIRED = "licenseRequired";

    public static final String LICENSE_KEY = "licenseKey";

    public static final String FILTER_SQL = "filterSql";

    public static final String FILE_DELETE_URL = "/api/file/deleteFile";
    public static final String FILE_COPY_URL = "/api/file/copyFile";
    public static final String FILE_ZIP_URL = "/api/file/zipFile";
    public static final String FILE_CREATE_BY_STRING = "/api/file/createFileByPathAndString";
    public static final String FILE_CONTENT_BY_PATHS = "/open-api/file/getFileContentByPaths";

    public static final String SCENE_MANAGE_UPDATE_FILE_URL = "/open-api/scenemanage/updateFile";
    public static final String SCENE_MANAGE_URL = "/open-api/scenemanage";
    public static final String SCENE_MANAGE_LIST_URL = "/open-api/scenemanage/list";
    public static final String SCENE_MANAGE_All_LIST_URL = "/open-api/scenemanage/listSceneManage";
    public static final String SCENE_MANAGE_DETAIL_URL = "/api/scenemanage/detail";
    public static final String SCENE_MANAGE_IPNUM_URL = "/open-api/scenemanage/ipnum";
    public static final String SCENE_MANAGE_PARSE_URL = "/api/scenemanage/parse/script";
    public static final String SCENE_MANAGE_FLOWCALC_URL = "/open-api/scenemanage/flow/calc";
    public static final String SCENE_MANAGE_ALLOCATION = "/open-api/scenemanage/allocation";
    public static final String SCENE_MANAGE_PARSE_AND_UPDATE_URL = "/open-api/scenemanage/parseAndUpdate/script";

    //task
    public static final String SCENE_TASK_UPDATE_TPS = "/open-api/scene/task/updateSceneTaskTps";

    public static final String SCENE_TASK_QUERY_ADJUST_TPS = "/open-api/scene/task/queryAdjustTaskTps";

    public static final String SCENE_REPORT_ALLOCATION = "/open-api/report/allocationUser";
    public static final String SCENE_MANAGE_BY_SCENE_IDS = "/open-api/scenemanage/query/ids" ;


    public static final String SCENE_START_TRIAL_RUN_URL = "/open-api/scene/task/startTrialRun";

    /**
     * 压测任务
     */
    public static final String SCENE_TASK_START= "/open-api/scene/task/start";
    public static final String SCENE_TASK_STOP= "/open-api/scene/task/stop";
    public static final String SCENE_TASK_CHECK= "/open-api/scene/task/checkStartStatus";
    public static final String START_FLOW_DEBUG_TASK = "/open-api/scene/task/startFlowDebugTask";

    public static final String REPORT_WARN_URL = "/open-api/report/warn";
    public static final String REPORT_JTL_DOWNLOAD_URL = "/open-api/report/getJtlDownLoadUrl";
    /**
     * 任务报告更新状态，目前漏数用
     */
    public static final String REPORT_UPDATE_STATE_URL = "/open-api/report/updateReportConclusion";
    public static final String REPORT_DETAIL_GET_URL = "/open-api/report/getReportByReportId";
    public static final String REPORT_TEMP_DETAIL_GET_URL = "/open-api/report/tempReportDetail";

    /**
     * 统计相关接口
     */
    public static final String STATISTIC_PRESSUREPIE_URL = "/open-api/statistic/getPressurePieTotal";
    public static final String STATISTIC_REPORT_URL = "/open-api/statistic/getReportTotal";
    public static final String STATISTIC_PRESSURELIST_URL = "/open-api/statistic/getPressureListTotal";
    //engine
    //获取引擎支持的插件信息
    public static final String ENGINE_FETCH_PLUGINS_URI = "/open-api/engine/fetchAvailableEnginePlugins";
    //获取引擎插件详情
    public static final String ENGINE_FETCH_PLUGIN_DETAILS_URI = "/open-api/engine/fetchEnginePluginDetails";


}
