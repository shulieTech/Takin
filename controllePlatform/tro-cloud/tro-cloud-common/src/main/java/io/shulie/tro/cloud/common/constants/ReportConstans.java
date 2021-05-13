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

package io.shulie.tro.cloud.common.constants;

/**
 * @Author 莫问
 * @Date 2020-04-24
 */
public class ReportConstans {

    /*********报表状态**/

    /**
     * 就绪状态
     **/
    public static final int INIT_STATUS = 0;

    /**
     * 生成中
     **/
    public static final int RUN_STATUS = 1;

    /**
     * 完成
     **/
    public static final int FINISH_STATUS = 2;

    /**
     * 锁定
     **/
    public static final int LOCK_STATUS = 9;

    /**压测失败**/
    //public static final int FAIL_STATUS = 3;

    /**
     * 压测信息消息 记录本次压测过程中的异常信息
     */
    public static final String PRESSURE_MSG = "pressure_msg";

    /*********报表结论**/

    /**
     * 通过
     **/
    public static final int PASS = 1;

    /**
     * 不通过
     **/
    public static final int FAIL = 0;

    /**
     * 异常消息
     */
    public static final String FEATURES_ERROR_MSG = "error_msg";

    /**
     * influxDB没5秒汇总的数据
     */
    public static final String ALL_BUSINESS_ACTIVITY = "all";

}
