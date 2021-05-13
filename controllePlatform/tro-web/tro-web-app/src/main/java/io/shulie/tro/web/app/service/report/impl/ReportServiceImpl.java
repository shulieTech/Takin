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

package io.shulie.tro.web.app.service.report.impl;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.VerifyResultStatusEnum;
import com.pamirs.tro.entity.domain.dto.report.LeakVerifyResult;
import com.pamirs.tro.entity.domain.vo.report.ReportIdVO;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.report.SceneIdVO;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;
import io.shulie.tro.cloud.open.req.report.ReportDetailByIdReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailBySceneIdReq;
import io.shulie.tro.cloud.open.resp.report.ReportDetailResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.linux.LinuxHelper;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.output.report.ReportDetailOutput;
import io.shulie.tro.web.app.output.report.ReportDetailTempOutput;
import io.shulie.tro.web.app.output.report.ReportJtlDownloadOutput;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskReportQueryRequest;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyTaskResultResponse;
import io.shulie.tro.web.app.service.VerifyTaskReportService;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.constant.FileManageConstant;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebRequest;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.http.HttpWebClient;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.result.user.TroUserResult;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import io.shulie.tro.web.diff.api.report.ReportApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @ClassName ReportServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 下午3:33
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private HttpWebClient httpWebClient;
    @Autowired
    private TroUserDAO troUserDAO;
    @Autowired
    private ReportApi reportApi;
    @Value("${file.upload.url:''}")
    private String fileUploadUrl;

    @Autowired
    private VerifyTaskReportService verifyTaskReportService;

    @Value("${file.upload.user.data.dir:/data/tmp}")
    private String fileDir;

    @Override
    public WebResponse listReport(ReportQueryParam param) {
        param.setRequestUrl(RemoteConstant.REPORT_LIST);
        param.setHttpMethod(HttpMethod.GET);
        if (StringUtils.isNotBlank(param.getManagerName())) {
            List<UserCommonResult> userList = troUserDAO.selectByName(param.getManagerName());
            if (CollectionUtils.isNotEmpty(userList)) {
                List<Long> uids = userList.stream().map(UserCommonResult::getUserId).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(uids)) {
                    param.setUserIdStr(null);
                } else {
                    param.setUserIdStr(StringUtils.join(uids, ","));
                }
            } else {
                return WebResponse.success(Lists.newArrayList());
            }
        }
        WebResponse webResponse = httpWebClient.request(param);
        List<Map> webRespData = (List<Map>)webResponse.getData();
        if (CollectionUtils.isEmpty(webRespData)) {
            return webResponse;
        }
        List<Long> userIds = webRespData.stream().filter(data -> null != data.get("userId"))
            .map(data -> Long.valueOf(data.get("userId").toString()))
            .collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, UserCommonResult> userMap = troUserDAO.selectUserMapByIds(userIds);
        webRespData.forEach(data -> {
                Long userId = data.get("userId") == null ? null : Long.valueOf(data.get("userId").toString());
                //负责人名称
                String userName = Optional.ofNullable(userMap.get(userId))
                    .map(UserCommonResult::getUserName)
                    .orElse("");
                data.put("managerName", userName);
                data.put("managerId", userId);
            });
        return webResponse;
    }

    @Override
    public WebResponse getReportByReportId(Long reportId) {
        ReportDetailByIdReq req = new ReportDetailByIdReq();
        req.setReportId(reportId);
        ResponseResult<ReportDetailResp> result = reportApi.getReportByReportId(req);
        if (result == null) {
            return WebResponse.fail("实况报告不存在!");
        }

        if (result.getSuccess()) {
            ReportDetailOutput output = new ReportDetailOutput();
            BeanUtils.copyProperties(result.getData(), output);
            assembleVerifyResult(output);
            //补充报告执行人
            fillExecuteMan(output);
            return WebResponse.success(output);
        }
        return WebResponse.fail(result.getError().getCode(), result.getError().getMsg());
    }

    private void fillExecuteMan(ReportDetailOutput output) {
        if (output == null || output.getUserId() == null) {
            return;
        }
        TroUserResult troUserResult = troUserDAO.selectById(output.getUserId());
        if(troUserResult != null) {
            output.setOperateName(troUserResult.getName());
            output.setManagerId(troUserResult.getId());
        }
    }

    private void assembleVerifyResult(ReportDetailOutput output) {
        //组装验证任务结果
        LeakVerifyTaskReportQueryRequest queryRequest = new LeakVerifyTaskReportQueryRequest();
        queryRequest.setReportId(output.getId());
        LeakVerifyTaskResultResponse verifyTaskResultResponse = verifyTaskReportService.getVerifyTaskReport(
            queryRequest);
        if (Objects.isNull(verifyTaskResultResponse)) {
            return;
        }
        LeakVerifyResult leakVerifyResult = new LeakVerifyResult();
        leakVerifyResult.setCode(verifyTaskResultResponse.getStatus());
        leakVerifyResult.setLabel(VerifyResultStatusEnum.getLabelByCode(verifyTaskResultResponse.getStatus()));
        output.setLeakVerifyResult(leakVerifyResult);
    }

    @Override
    public WebResponse queryReportTrend(ReportTrendQueryParam param) {
        param.setRequestUrl(RemoteConstant.REPORT_TREND);
        param.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(param);
    }

    @Override
    public WebResponse tempReportDetail(Long sceneId) {
        ReportDetailBySceneIdReq req = new ReportDetailBySceneIdReq();
        req.setSceneId(sceneId);
        ResponseResult<ReportDetailResp> result = reportApi.tempReportDetail(req);
        if (!result.getSuccess()) {
            return WebResponse.fail(result.getError().getCode(), result.getError().getMsg());
        }
        ReportDetailResp resp = result.getData();
        ReportDetailTempOutput output = new ReportDetailTempOutput();
        BeanUtils.copyProperties(resp, output);
        List<Long> allowStartStopUserIdList = RestContext.getStartStopAllowUserIdList();
        if (CollectionUtils.isNotEmpty(allowStartStopUserIdList)) {
            String managerId = "";
            if (output.getManagerId() != null) {
                managerId = String.valueOf(resp.getManagerId());
            }
            if (StringUtils.isBlank(managerId) || !allowStartStopUserIdList.contains(Long.parseLong(managerId))) {
                output.setCanStartStop(Boolean.FALSE);
            } else {
                output.setCanStartStop(Boolean.TRUE);
            }
        } else {
            output.setCanStartStop(Boolean.TRUE);
        }
        fillExecuteMan(output);
        return WebResponse.success(output);

    }

    @Override
    public WebResponse queryTempReportTrend(ReportTrendQueryParam param) {
        param.setRequestUrl(RemoteConstant.REPORT_REALTIME_TREND);
        param.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(param);
    }

    @Override
    public WebResponse listWarn(WarnQueryParam param) {
        param.setRequestUrl(RemoteConstant.WARN_LIST);
        param.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(param);
    }

    @Override
    public WebResponse queryReportActivityByReportId(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_BUSINESSACTIVITY_LIST);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse queryReportActivityBySceneId(Long sceneId) {
        SceneIdVO vo = new SceneIdVO();
        vo.setSceneId(sceneId);
        vo.setRequestUrl(RemoteConstant.SCENE_BUSINESSACTIVITY_LIST);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse querySummaryList(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_SUMMARY_LIST);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse queryMetrices(Long reportId, Long sceneId, Long customId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setSceneId(sceneId);
        vo.setCustomerId(customId);
        vo.setRequestUrl(RemoteConstant.REPORT_METRICES);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse queryReportCount(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_COUNT);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse queryRunningReport() {
        WebRequest request = new WebRequest();
        request.setRequestUrl(RemoteConstant.REPORT_RUNNINNG);
        request.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(request);
    }

    @Override
    public WebResponse queryListRunningReport() {
        WebRequest request = new WebRequest();
        request.setRequestUrl(RemoteConstant.REPORT_RUNNINNG_LIST);
        request.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(request);
    }

    @Override
    public WebResponse lockReport(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_LOCK);
        vo.setHttpMethod(HttpMethod.PUT);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse unLockReport(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_UNLOCK);
        vo.setHttpMethod(HttpMethod.PUT);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse finishReport(Long reportId) {
        ReportIdVO vo = new ReportIdVO();
        vo.setReportId(reportId);
        vo.setRequestUrl(RemoteConstant.REPORT_FINISH);
        vo.setHttpMethod(HttpMethod.PUT);
        return httpWebClient.request(vo);
    }

    @Override
    public ReportJtlDownloadOutput getJtlDownLoadUrl(Long reportId) {
        ResponseResult<String> result = reportApi.getJtlDownLoadUrl(reportId);
        if (result.getSuccess()) {
            String url = null;
            try {
                url = fileUploadUrl + FileManageConstant.CLOUD_FILE_DOWN_LOAD_API +  URLEncoder.encode(result.getData(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // todo 临时方案
            String fileName = reportId  + "_jtl.zip";
            String[] cmds = {"curl","-o",fileDir +"/" + fileName,"-OL","-H", "licenseKey:"+RemoteConstant.LICENSE_VALUE,url};
            LinuxHelper.execCurl(cmds);
            return new ReportJtlDownloadOutput(fileDir + "/" +fileName, true);
        }
        return new ReportJtlDownloadOutput(result.getData(), false);
    }
}
