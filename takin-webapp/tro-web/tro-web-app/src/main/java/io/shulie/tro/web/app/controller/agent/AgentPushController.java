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

package io.shulie.tro.web.app.controller.agent;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.dto.NodeUploadDataDTO;
import com.pamirs.tro.entity.domain.query.ShadowJobConfigQuery;
import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import com.pamirs.tro.entity.domain.vo.JarVersionVo;
import com.pamirs.tro.entity.domain.vo.TUploadInterfaceVo;
import com.pamirs.tro.entity.domain.vo.TUploadNeedVo;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.AgentUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.ConfCenterService;
import io.shulie.tro.web.app.service.UploadInterfaceService;
import io.shulie.tro.web.app.service.linkManage.ApplicationApiService;
import io.shulie.tro.web.app.service.perfomanceanaly.TraceManageService;
import io.shulie.tro.web.app.service.simplify.ShadowJobConfigService;
import io.shulie.tro.web.app.utils.Estimate;
import io.shulie.tro.web.app.utils.XmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AgentUrls.PREFIX_URL)
@Api(tags = "Agent全局配置")
@Slf4j
public class AgentPushController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationApiService apiService;

    @Autowired
    private ShadowJobConfigService shadowJobConfigService;

    @Autowired
    private UploadInterfaceService uploadInterfaceService;

    @Autowired
    private ConfCenterService confCenterService;

    @Autowired
    private TraceManageService traceManageService;

    @ApiOperation("agent注册api")
    @PostMapping(value = AgentUrls.REGISTER_URL)
    public Response registerApi(@RequestBody Map<String, List<String>> register) {
        try {
            return apiService.registerApi(register);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    /**
     * @param requestJson
     * @return
     */
    @PostMapping(value = AgentUrls.MIDDLE_STAUTS_URL)
    @ApiOperation("agent上传中间件列表状态")
    public Response uploadMiddlewareStatusAndRole(@RequestBody String requestJson,
        @RequestParam("appName") String appName) {
        try {
            Map<String, JarVersionVo> requestMap = JSONObject.parseObject(requestJson,
                new TypeReference<Map<String, JarVersionVo>>() {
                });
            return applicationService.uploadMiddlewareStatus(requestMap, appName);
        } catch (Exception e) {
            return Response.fail("middle status error");
        }
    }

    @PostMapping(value = AgentUrls.APP_INSERT_URL)
    @ApiOperation("上传应用")
    public Response addApplication(@RequestBody ApplicationVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION, vo.getApplicationName());
        return applicationService.addAgentRegisteApplication(vo);
    }

    @PostMapping(value = AgentUrls.UPLOAD_ACCESS_STATUS)
    @ApiOperation("上传应用状态")
    public Response uploadAccessStatus(@RequestBody NodeUploadDataDTO param) {
        return applicationService.uploadAccessStatus(param);
    }

    /**
     * 上传应用信息接口
     *
     * @param TUploadInterfaceVo appName与接口信息
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     */
    @PostMapping(value = AgentUrls.UPLOAD_APP_INFO,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> judgeNeedUpload(@RequestBody TUploadInterfaceVo TUploadInterfaceVo) {
        try {
            return ResponseOk.create(uploadInterfaceService.saveUploadInterfaceData(TUploadInterfaceVo));
        } catch (TROModuleException e) {
            log.error("AgentController.judgeNeedUpload 查询是否需要上传异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            log.error("AgentController.judgeNeedUpload 查询是否需要上传异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_INTERFACE_UPLOAD_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 判断是否需要上传
     *
     * @param uploadNeedVo appName与数量
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     */
    @PostMapping(value = AgentUrls.UPLOAD,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> judgeNeedUpload(@RequestBody TUploadNeedVo uploadNeedVo) {
        try {
            return ResponseOk.create(uploadInterfaceService.executeNeedUploadInterface(uploadNeedVo));
        } catch (TROModuleException e) {
            log.error("AgentController.judgeNeedUpload 查询是否需要上传异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            log.error("AgentController.judgeNeedUpload 查询是否需要上传异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorMessage());
        }
    }

    @ApiOperation(value = "影子JOB配置修改")
    @RequestMapping(value = AgentUrls.TRO_REPORT_ERROR_SHADOW_JOB_URL, method = {RequestMethod.PUT, RequestMethod.POST},
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response update(@RequestBody ShadowJobConfigQuery query) {
        try {
            Estimate.notBlank(query.getId(), "ID不能为空");
            Map<String, String> xmlMap = XmlUtil.readStringXml(query.getConfigCode());
            String className = xmlMap.get("className");
            OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
            OperationLogContextHolder.addVars(BizOpConstants.Vars.TASK, className);
            return shadowJobConfigService.update(query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 更新 应用agentVersion版本
     *
     * @param appName
     * @param agentVersion
     * @return
     */
    @GetMapping(value = AgentUrls.AGENT_VERSION, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> appAgentVersionUpdate(@RequestParam("appName") String appName,
        @RequestParam(value = "agentVersion") String agentVersion,
        @RequestParam(value = "pradarVersion") String pradarVersion) {
        try {
            if (StringUtils.isBlank(agentVersion)
                || StringUtils.isBlank(pradarVersion)
                || "null".equalsIgnoreCase(agentVersion)
                || "null".equalsIgnoreCase(pradarVersion)) {
                return ResponseError.create(1010100102, "更新应用版本异常,参数为空");
            }
            confCenterService.updateAppAgentVersion(appName, agentVersion, pradarVersion);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            log.error("AgentController.appAgentVersionUpdate 更新应用版本异常 {}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            log.error("AgentController.appAgentVersionUpdate 更新应用版本异常 {}", e);
            return ResponseError.create(1010100102, "更新应用版本异常");
        }
    }

    /**
     * trace数据 方法追踪
     * @param commandPacket
     */
    @PostMapping(value = AgentUrls.PERFORMANCE_TRACE_URL)
    @ApiOperation(value = "agent上传trace信息")
    public void uploadTraceInfo(@RequestBody CommandPacket commandPacket) {
        log.info("agent上传trace信息，入参为:{}", JsonHelper.bean2Json(commandPacket));
        traceManageService.uploadTraceInfo(commandPacket);
    }

    @Deprecated
    @RequestMapping(value = AgentUrls.BWLISTMETRIC_URL, method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryBWMetricList(@RequestParam(name = "appName", required = false) String appName) {
        try {
            return ResponseEntity.ok(null);
            //return ResponseOk.create(confCenterService.queryBWMetricList(appName));
        } catch (Exception e) {
            log.error("AgentController.queryBWMetricList  查询Metric黑白名单列表异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_QUERY_BWLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_QUERY_BWLIST_EXCEPTION.getErrorMessage());
        }
    }
}
