package io.shulie.tro.definition.config.http;

import io.shulie.tro.definition.annotaion.ParamTypes;
import io.shulie.tro.definition.annotaion.ReturnType;
import io.shulie.tro.definition.config.http.output.PressureSwitchStatusOutput;
import io.shulie.tro.definition.config.http.input.TraceUploadInput;
import io.shulie.tro.definition.config.http.output.WhiteListStatusOutput;
import io.shulie.tro.definition.config.http.input.ApplicationInsertInput;

import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.AGENT_VERSION_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.APP_INSERT_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.APP_PRESSURE_SWITCH_STATUS_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.APP_WHITE_LIST_SWITCH_STATUS_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.GUARD_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.MIDDLE_STAUTS_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.PERFORMANCE_BASE_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.PERFORMANCE_TRACE_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.PREFIX_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.REGISTER_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.ROCKETMQ_ISO_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.SHADOW_DB_TABLE_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.TRO_REPORT_ERROR_SHADOW_JOB_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.TRO_SHADOW_JOB_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.UPLOAD_ACCESS_STATUS_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.UPLOAD_APP_INFO_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.UPLOAD_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.WHITELIST_FILE_URL;
import static io.shulie.tro.definition.config.http.TroHttpConfigConstants.WHITELIST_URL;

/**
 * @author shiyajian
 * create: 2020-12-09
 */
public enum TroHttpPullConfigUrlEnums {

    /**
     * http 拉取白名单
     */
    ALLOW_LIST(PullMethod.GET, PREFIX_URL + WHITELIST_URL),

    ALLOW_FILE_LIST(PullMethod.POST, PREFIX_URL + WHITELIST_FILE_URL),

    SHADOW_DB_TABLE(PullMethod.GET, PREFIX_URL + SHADOW_DB_TABLE_URL),

    TRO_SHADOW_JOB(PullMethod.GET, PREFIX_URL + TRO_SHADOW_JOB_URL),

    TRO_REPORT_ERROR_SHADOW_JOB(PullMethod.GET, PREFIX_URL + TRO_REPORT_ERROR_SHADOW_JOB_URL),

    UPLOAD_ACCESS_STATUS(PullMethod.POST, PREFIX_URL + UPLOAD_ACCESS_STATUS_URL),

    UPLOAD_APP_INFO(PullMethod.POST, PREFIX_URL + UPLOAD_APP_INFO_URL),

    UPLOAD(PullMethod.POST, PREFIX_URL + UPLOAD_URL),

    @ParamTypes(ApplicationInsertInput.class)
    @ReturnType(String.class)// 成功为空，失败返回错误信息
    APP_INSERT(PullMethod.GET, PREFIX_URL + APP_INSERT_URL),

    AGENT_VERSION(PullMethod.GET, PREFIX_URL + AGENT_VERSION_URL),

    @ParamTypes()
    @ReturnType(PressureSwitchStatusOutput.class)
    APP_PRESSURE_SWITCH_STATUS(PullMethod.GET, PREFIX_URL + APP_PRESSURE_SWITCH_STATUS_URL),

    @ParamTypes()
    @ReturnType(WhiteListStatusOutput.class)
    APP_WHITE_LIST_SWITCH_STATUS(PullMethod.GET, PREFIX_URL + APP_WHITE_LIST_SWITCH_STATUS_URL),

    GUARD(PullMethod.GET, PREFIX_URL + GUARD_URL),

    @ParamTypes()// Map<String, List<String>> TODO 不知道后面这个String是啥
    @ReturnType(String.class)// 成功为空，失败返回错误信息
    REGISTER(PullMethod.POST, PREFIX_URL + REGISTER_URL),

    @ParamTypes(String.class)// json，Map<String, JarVersionVo>
    @ReturnType(String.class)// 成功为空，失败返回错误信息
    MIDDLE_STATUS(PullMethod.POST, PREFIX_URL + MIDDLE_STAUTS_URL),

    ROCKETMQ_ISO(PullMethod.GET, PREFIX_URL + ROCKETMQ_ISO_URL),

    PERFORMANCE_BASE(PullMethod.GET, PREFIX_URL + PERFORMANCE_BASE_URL),

    @ParamTypes(TraceUploadInput.class)
    @ReturnType(Void.class)
    PERFORMANCE_TRACE(PullMethod.POST, PREFIX_URL + PERFORMANCE_TRACE_URL);

    private PullMethod method;

    private String url;

    TroHttpPullConfigUrlEnums(PullMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    public PullMethod getMethod() {
        return method;
    }

    public void setMethod(PullMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public enum PullMethod {

        GET,
        POST;
    }
}
