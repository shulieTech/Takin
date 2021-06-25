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

package io.shulie.tro.web.common.constant;

/**
 * @ClassName RemoteConstant
 * @Description tro-cloud 通过SDK通过
 * @Author qianshui
 * @Date 2020/5/11 下午4:10
 */
@Deprecated
public class RemoteConstant {

    public static final String LICENSE_REQUIRED = "licenseRequired";

    public static final String LICENSE_KEY = "licenseKey";
    public static final String FILTER_SQL = "filterSql";
    public static final String PAGE_TOTAL_HEADER = "x-total-count";
    public static final String SCENE_MANAGE_URL = "/api/scenemanage";
    public static final String SCENE_MANAGE_PAGELIST_URL = "/api/scenemanage/list";
    public static final String SCENE_MANAGE_DETAIL_URL = "/api/scenemanage/detail";
    public static final String SCENE_MANAGE_IPNUM_URL = "/api/scenemanage/ipnum";
    public static final String SCENE_MANAGE_PARSE_URL = "/api/scenemanage/parse/script";
    public static final String SCENE_MANAGE_FLOWCALC_URL = "/api/scenemanage/flow/calc";
    public static final String FILE_UPLOAD_URL = "/api/file/upload";
    public static final String FILE_URL = "/api/file";
    public static final String SCENE_TASK_START_URL = "/api/scene/task/start";
    public static final String SCENE_TASK_STOP_URL = "/api/scene/task/stop";
    public static final String SCENE_TASK_CHECKSTATUS_URL = "/api/scene/task/checkStartStatus";
    public static final String REPORT_LIST = "/api/report/listReport";
    public static final String REPORT_TREND = "/api/report/queryReportTrend";
    public static final String REPORT_REALTIME_TREND = "/api/report/queryTempReportTrend";
    public static final String WARN_LIST = "/api/report/listWarn";
    public static final String REPORT_BUSINESSACTIVITY_LIST = "/api/report/queryReportActivityByReportId";
    public static final String SCENE_BUSINESSACTIVITY_LIST = "/api/report/queryReportActivityBySceneId";
    public static final String REPORT_SUMMARY_LIST = "/api/report/businessActivity/summary/list";
    public static final String REPORT_METRICES = "/api/report/metrices";
    public static final String REPORT_COUNT = "/api/report/count";
    public static final String REPORT_RUNNINNG = "/api/report/running";
    public static final String REPORT_RUNNINNG_LIST = "/api/report/running/list";
    public static final String REPORT_LOCK = "/api/report/lock";
    public static final String REPORT_UNLOCK = "/api/report/unlock";
    public static final String REPORT_FINISH = "/api/report/finish";
    public static final String ACCOUNT_BOOK = "/api/settle/accountbook";
    public static final String ACCOUNT_BALANCE_LIST = "/api/settle/balance/list";
    public static final String DICT_LIST = "/api/link/dictionary";
    public static final String ACTIVITY_LIST = "/api/link/bussinessActive";
    public static final String SWITCH_LIST = "/api/application/center/app/switch";
    public static String LICENSE_VALUE = "";
}
