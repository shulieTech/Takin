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

package io.shulie.tro.web.app.exception;

import io.shulie.tro.exception.entity.ExceptionReadable;

/**
 * @author shiyajian
 * create: 2020-09-04
 */
public enum ExceptionCode implements ExceptionReadable {
    SERVLET_ERROR("0000_0000_0000", "请求错误"),
    /**
     * 异常编号
     */
    LICENSE_NOT_VALIDATE("0000_0000_0001", "秘钥校验不合格"),
    TEST_2("0000_0000_0002", "测试对象"),
    GET_CURRENT_USER_ERROR("0000_0000_0003", "获取当前登录用户失败"),
    GET_CUSTOMER_ID_ERROR("0000_0000_0004", "获取租户失败"),
    /**
     * 脚本相关
     */
    SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR("0000_0001_0001", "创建脚本参数校验失败"),
    /**
     * 脚本创建验证错误
     */
    SCRIPT_MANAGE_CREATE_VALID_ERROR("0000_0001_0002", "创建脚本校验失败"),
    SCRIPT_MANAGE_UPDATE_PARAM_VALID_ERROR("0000_0001_0003", ""),

    /**
     * 脚本更新验证错误
     */
    SCRIPT_MANAGE_UPDATE_VALID_ERROR("0000_0001_0004", ""),

    SCRIPT_MANAGE_DOWNLOAD_VALID_ERROR("0000_0001_0005",""),
    SCRIPT_MANAGE_EXPLAIN_SCRIPT_FILE_ERROR("0000_0001_0006",""),
    SCRIPT_MANAGE_DELETE_VALID_ERROR("0000_0001_0007",""),
    SCRIPT_MANAGE_TAG_ADD_VALID_ERROR("0000_0001_0008",""),
    SCRIPT_MANAGE_ROLLBACK_VALID_ERROR("0000_0001_0009",""),
    SCRIPT_MANAGE_VALID_NO_CONFIG("0000_0001_0010","没有样例配置"),
    SCRIPT_MANAGE_EXECUTE_ERROR("0000_0001_0011","脚本执行失败"),
    SCRIPT_MANAGE_ERROR("0000_0001_0012",""),


    /**
     * 登录相关
     */
    LOGIN_ERROR("0000_0002_0001", "登录失败"),

    /**
     * 权限相关
     */
    ACTION_PERMISSION_DENY_ERROR("0000_0003_0001", "操作权限不足"),
    DATA_PERMISSION_DENY_ERROR("0000_0003_0002", "数据权限不足"),
    MENU_PERMISSION_DENY_ERROR("0000_0003_0003", "菜单权限不足"),
    LICENSE_PERMISSION_DENY_ERROR("0000_0003_0004", "license已过期"),
    ALL_PERMISSION_DENY_ERROR("0000_0003_0005", "用户暂未分配权限，请联系管理员"),

    /**
     * 压测场景相关
     */
    SCENE_MANAGE_START_TASK_VALID_ERROR("0000_0004_0001", "压测任务启动校验失败"),
    SCENE_MANAGE_START_TASK_ERROR("0000_0004_0002", "压测任务启动失败"),

    /**
     * 数据源管理相关
     */
    DATASOURCE_MANAGE_TAG_ADD_VALID_ERROR("0000_0005_0001", "标签添加失败"),
    DATASOURCE_ADD_ERROR("0000_0005_0002", "数据源添加失败"),
    DATASOURCE_UPDATE_ERROR("0000_0005_0003", "数据源更新失败"),
    DATASOURCE_DELETE_ERROR("0000_0005_0004", "数据源删除失败"),
    DATASOURCE_TEST_CONNECTION_ERROR("0000_0005_0006", "数据源调试失败"),

    /**
     * 漏数配置相关
     */
    LEAK_SQL_REF_CREATE_ERROR("0000_0006_0001", "漏数配置失败"),
    LEAK_SQL_REF_INVALID_ERROR("0000_0006_0002", "不合法的SQL"),
    LEAK_SQL_REF_RUN_ERROR("0000_0006_0003", "SQL验证失败"),

    /**
     * 验证任务相关
     */
    VERIFY_TASK_START_FAILED("0000_0007_0001", "验证任务启动失败"),
    VERIFY_TASK_RUN_FAILED("0000_0007_0002", "验证任务运行失败"),

    /**
     * 快速调试异常
     */
    FAST_DEBUG_PARAM_VALID_ERROR("0000_0008_0001", "参数检验失败"),
    FAST_DEBUG_PARAM_NAME_ERROR("0000_0008_0002", ""),
    FAST_DEBUG_DELETE_ERROR("0000_0008_0003", ""),
    FAST_DEBUG_ERROR("0000_0008_0004", ""),
    FAST_SEARCH_ERROR("0000_0008_0005", ""),
    FAST_DEBUG_GET_APP_LOG_ERROR("0000_0008_0006", ""),
    FAST_DEBUG_GET_AGENT_LOG_ERROR("0000_0008_0007", ""),
    FAST_DEBUG_GET_AGENT_LOG_NAME_ERROR("0000_0008_0008", ""),
    FAST_DEBUG_GET_CALLSTACK_EXCEPTION_ERROR("0000_0008_0009", ""),
    FAST_DEBUG_CALLSTACK_LOCATE_ERROR("0000_0008_0010", ""),



    /**
     * agent通道异常
     */
    AGENT_REGISTER_ERROR("0000_0009_0001", "agent尚未注册，请检查应用接入状态"),
    AGENT_SEND_ERROR("0000_0009_0002", "上传命令异常"),
    AGENT_RESPONSE_ERROR("0000_0009_0003", "agent响应异常"),

    /**
     * 性能分析 本地方法
     */
    TRACE_MANAGE_PARAM_VALID_ERROR("0000_0010_0001","本地方法参数校验异常"),
    TRACE_MANAGE_VALID_ERROR("0000_0010_0002","本地方法校验异常"),
    TRACE_MANAGE_TIMEOUT("0000_0010_0003","追踪超时"),
    TRACE_MANAGE_ERROR("0000_0010_0004","追踪失败"),
    DUMP_ERROR("0000_0010_0005","dump异常"),

    /**
     * 报告有关
     */
    REPORT_FINISH_ERROR("0000_0011_0001",""),

    /**
     * 压测调度异常
     */
    SCENE_STOP_ERROR("0000_0012_0001",""),
    SCENE_CHECK_ERROR("0000_0012_0002",""),
    /**
     * 场景标签相关
     */
    SCENE_TAG_REF_ADD_VALID_ERROR("0000_0013_0001",""),
    SCENE_SCHEDULER_TASK_SCENE_ID_VALID_ERROR("0000_0013_0002",""),
    SCENE_SCHEDULER_TASK_EXECUTE_TIME_VALID_ERROR("0000_0013_0003",""),
    SCENE_SCHEDULER_TASK_EXISTS_VALID_ERROR("0000_0013_0004",""),

    /**
     * 用户管理相关
     */
    USER_MANAGE_ADD_ERROR("0000_0010_0001", "用户新增失败"),
    USER_MANAGE_UPDATE_ERROR("0000_0010_0002", "用户更新失败"),
    USER_MANAGE_IMPORT_ERROR("0000_0010_0003", "用户导入失败"),
    USER_MANAGE_QUERY_ERROR("0000_0010_0004", "查询用户失败"),
    USER_MANAGE_DOWNLOAD_ERROR("0000_0010_0005", "下载用户数据失败"),

    /**
     * 部门管理相关
     */
    DEPT_MANAGE_ADD_ERROR("0000_0011_0001", "部门新增失败"),
    DEPT_MANAGE_UPDATE_ERROR("0000_0011_0002", "部门更新失败"),
    DEPT_MANAGE_DELETE_ERROR("0000_0011_0003", "部门删除失败"),
    /**
     * 验证码
     */
    VERIFICATION_CODE_ERROR("0000_0012_0001", ""),

    /**
     * 黑名单
     */
    BLACKLIST_ADD_ERROR("0000_0013_0001", ""),
    BLACKLIST_UPDATE_ERROR("0000_0013_0002", ""),
    BLACKLIST_DELETE_ERROR("0000_0013_0003", ""),
    BLACKLIST_ENABLE_ERROR("0000_0013_0004", ""),
    BLACKLIST_SEARCH_ERROR("0000_0013_0005", ""),

    /**
     * 挡板
     */
    GUARD_PARAM_ERROR("0000_0014_0001", ""),

    /**
     * job
     */
    JOB_PARAM_ERROR("0000_0015_0001", ""),

    /**
     * 白名单生效应用添加
     */
    WHITELIST_EFFECTIVE_APP_ERROR("0000_0016_0001", ""),

    /**
     * excel导入导出
     */
    EXCEL_IMPORT_ERROR("0000_0017_0001", ""),

    /**
     * pod num
     */
    POD_NUM_EMPTY("0000_0018_0001",""),
    ;
    private String errorCode;

    private String defaultValue;

    ExceptionCode(String errorCode, String defaultValue) {
        this.errorCode = errorCode;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
