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

package com.pamirs.tro.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 定义的一些静态常量类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
public class Constants {

    //报告状态：正在测试
    public static final String REPORT_STATUS_TESTING = "1";

    // 报告状态：测试完成
    public static final String REPORT_STATUS_DONE = "0";

    //二级链路状态：正在测试
    public static final String SECOND_LINK_STATUS_TESTING = REPORT_STATUS_TESTING;

    //二级链路状态：测试完成
    public static final String SECOND_LINK_STATUS_NOT_TESTING = REPORT_STATUS_DONE;

    //暂停
    public static final String SECOND_LINK_STATUS_SUSPEND = "2";

    //停止
    public static final String SECOND_LINK_STATUS_STOP = "3";

    public static final String BASE_LINK_STOP = "0";
    //基础链路正在压测
    public static final String BASE_LINK_TESTING = "1";

    //是否计算tps：计算
    public static final String IS_TPS_COUNT = "1";

    //是否计算tps：不计算
    public static final String IS_NOT_TPS_COUNT = "0";

    //未开始检查
    public static final String DETECTION_NOT_START = "0";

    //正在检查
    public static final String DETECTION_STARTING = "1";

    //检查成功
    public static final String DETECTION_SUCCESS = "2";

    //检查失败
    public static final String DETECTION_FAIL = "3";

    //正在消费
    public static final String CONSUMING = "1";

    //消费成功
    public static final String CONSUME_SUCCESS = "2";

    //消费失败
    public static final String CONSUME_FAILED = "3";

    // 开启抽取数据
    public static final String LOAD_START = "start";
    public static final int LOAD_START_INT = 1;

    // 停止抽取数据
    public static final String LOAD_STOP = "stop";
    public static final int LOAD_STOP_INT = 2;

    // 数据库禁用标志
    public static final String DISABLE = "disable";

    // 数据库启用标志
    public static final String ENABLE = "enable";

    // 正在生产消息
    public static final String PRODUCING = "1";

    // 生产完成
    public static final String PRODUCE_FINISH = "2";

    //消费失败
    public static final String PRODUCE_FAILED = "3";

    // 阿斯旺id
    public static final String ASWAN_ID = "ASWAN_ID";

    // 基础链路名称
    public static final String BASIC_LINK_NAME = "LINK_NAME";

    /**
     * 取数逻辑的SQL或处理sql的类型，1表示文件附件
     */
    public static final String SQL_TYPE_FILE = "1";

    /**
     * 取数逻辑的SQL或处理sql的类型，0表示存sql语句
     */
    public static final String SQL_TYPE_TEXT = "0";

    /**
     * 数据库状态，0表示删除
     */
    public static final String DB_STATUS_DELETED = "0";

    /**
     * 数据库状态，1表示使用
     */
    public static final String DB_STATUS_ACTIVE = "1";

    /**
     * 上传类型 与 数据库类型
     */
    public static final String UPLOAD_DATA_TYPE_DUBBO = "dubbo";
    public static final Integer UPLOAD_DATA_DBTYPE_DUBBO = 1;
    public static final String UPLOAD_DATA_TYPE_JOB = "job";
    public static final Integer UPLOAD_DATA_DBTYPE_JOB = 4;

    /**
     * 是否使用 1 使用 0 不使用
     */
    public static final String STRING_USE = "1";
    public static final String STRING_NOT_USE = "0";

    public static final Integer INTEGER_USE = 1;
    public static final Integer INTEGER_NOT_USE = 0;

    public static final List<String> SQL_DDL_OPERTYPE = Arrays.asList("UPDATE", "DELETE", "INSERT");

    /**
     * pradar同步数据库使用到的redis锁的key值
     */
    public static final String PRADA_SYNCHRONIZED_TO_MYSQL = "pradaSynchronizedTomysql_lock";

    /**
     * pradar同步redis：所有url数据的key
     */
    public static final String ALL_URL_LIST = "allUrlList";

    /**
     * pradar同步redis的标志key
     */
    public static final String PRADA_SYNCHRONIZED_TO_REDIS = "pradaSynchronizedToRedis";

    /**
     * 成功
     */
    public static final String SUCCESS = "Success";

    /**
     * pradar同步数据库待删除数据的表名
     */
    public static final String TRUNCATE_TABLE = "t_prada_http_data";

    /**
     * 竖线分隔符
     */
    public static final String SPLIT = "|";

    /**
     * Y 与 N 常量
     */
    public static final String Y = "Y";
    public static final String N = "N";

    /**
     * Y 与 N 常量
     */
    public static final Byte B_Y = 'Y';
    public static final Byte B_N = 'N';

    /**
     * 应用上传信息与堆栈 类型 与 string
     */
    public static final String APPLICATION_INFO_TYPE_TRACE_STRING = "trace";
    public static final String APPLICATION_INFO_TYPE_SQL_ERROR_STRING = "sql";
    public static final int APPLICATION_INFO_TYPE_TRACE = 1;
    public static final int APPLICATION_INFO_TYPE_SQL_ERROR = 2;

    /**
     * 应用插件类型
     **/
    //业务监控
    public static final int PLUGIN_TYPE_MONITOR = 1;
    //故障检测
    public static final int PLUGIN_TYPE_DETECT = 2;

    //链路探活运行中
    public static final String LINK_RESEARCH_LIVE_RUNNING = "1";
    //链路探活未开启
    public static final String LINK_RESEARCH_LIVE_STOP = "0";

    /**
     * MQ接口类型
     */
    public static final String MQ = "MQ";
    public static final String HTTP = "HTTP";
    public static final String JOB = "JOB";
    public static final String DUBBO = "DUBBO";

    /**
     * 瓶颈级别 严重 serious 普通 error 正常 normal
     */
    public static final String SERIOUS = "serious";
    public static final String ERROR = "error";
    public static final String NORMAL = "normal";

    /**
     * API异常默认状态码
     */
    public static final int API_ERROR_CODE = 500;

    /**
     * SLA
     */
    public static final String SLA_DESTORY_EXTEND = "SLA_DESTORY";

}
