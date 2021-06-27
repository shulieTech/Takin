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

package io.shulie.tro.web.app.service.scriptmanage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.SceneManageConstant;
import com.pamirs.tro.common.enums.amdb.common.enums.RpcType;
import com.pamirs.tro.common.util.parse.UrlUtil;
import com.pamirs.tro.entity.dao.linkmanage.TBusinessLinkManageTableMapper;
import com.pamirs.tro.entity.dao.linkmanage.TSceneLinkRelateMapper;
import com.pamirs.tro.entity.dao.linkmanage.TSceneMapper;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.scenemanage.ScriptCheckDTO;
import com.pamirs.tro.entity.domain.entity.linkmanage.BusinessLinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.Scene;
import com.pamirs.tro.entity.domain.entity.linkmanage.SceneLinkRelate;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.common.pojo.dto.scenemanage.UploadFileDTO;
import io.shulie.tro.cloud.open.req.engine.EnginePluginDetailsWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneParseReq;
import io.shulie.tro.cloud.open.req.scenemanage.UpdateSceneFileRequest;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginDetailResp;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginSimpleInfoResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageListResp;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.utils.linux.LinuxHelper;
import io.shulie.tro.utils.string.StringUtil;
import io.shulie.tro.utils.xml.XmlHelper;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.convert.performace.TraceManageResponseConvertor;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.filemanage.FileManageCreateRequest;
import io.shulie.tro.web.app.request.filemanage.FileManageUpdateRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployCreateRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployPageQueryRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptManageDeployUpdateRequest;
import io.shulie.tro.web.app.request.scriptmanage.ScriptTagCreateRefRequest;
import io.shulie.tro.web.app.request.scriptmanage.SupportJmeterPluginNameRequest;
import io.shulie.tro.web.app.request.scriptmanage.SupportJmeterPluginVersionRequest;
import io.shulie.tro.web.app.response.filemanage.FileManageResponse;
import io.shulie.tro.web.app.response.scriptmanage.PluginConfigDetailResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageActivityResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployActivityResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployDetailResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageSceneManageResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageXmlContentResponse;
import io.shulie.tro.web.app.response.scriptmanage.SinglePluginRenderResponse;
import io.shulie.tro.web.app.response.scriptmanage.SupportJmeterPluginNameResponse;
import io.shulie.tro.web.app.response.scriptmanage.SupportJmeterPluginVersionResponse;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;
import io.shulie.tro.web.app.service.linkManage.LinkManageService;
import io.shulie.tro.web.app.utils.exception.ScriptManageExceptionUtil;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.common.constant.AppConstants;
import io.shulie.tro.web.common.constant.FeaturesConstants;
import io.shulie.tro.web.common.constant.FileManageConstant;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.constant.ScriptManageConstant;
import io.shulie.tro.web.common.enums.script.FileTypeEnum;
import io.shulie.tro.web.common.enums.script.ScriptManageDeployStatusEnum;
import io.shulie.tro.web.common.pojo.vo.file.FileExtendVO;
import io.shulie.tro.web.common.util.ActivityUtil;
import io.shulie.tro.web.common.util.ActivityUtil.EntranceJoinEntity;
import io.shulie.tro.web.common.util.FileUtil;
import io.shulie.tro.web.data.dao.filemanage.FileManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.BusinessLinkManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.LinkManageDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptFileRefDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptManageDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptTagRefDAO;
import io.shulie.tro.web.data.dao.tagmanage.TagManageDAO;
import io.shulie.tro.web.data.param.filemanage.FileManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageQueryParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployPageQueryParam;
import io.shulie.tro.web.data.param.tagmanage.TagManageParam;
import io.shulie.tro.web.data.result.filemanage.FileManageResult;
import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
import io.shulie.tro.web.data.result.linkmange.LinkManageResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptFileRefResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageDeployResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptTagRefResult;
import io.shulie.tro.web.data.result.tagmanage.TagManageResult;
import io.shulie.tro.web.diff.api.DiffFileApi;
import io.shulie.tro.web.diff.api.scenemanage.SceneManageApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoyong
 */
@Component
@Slf4j
public class ScriptManageServiceImpl implements ScriptManageService {

    @Value("${file.upload.url:''}")
    private String fileUploadUrl;
    @Value("${file.upload.tmp.path:'/tmp/tro/'}")
    private String tmpFilePath;
    @Value("${file.upload.script.path:'/nfs/tro/script/'}")
    private String scriptFilePath;
    @Value("${script.check:true}")
    private Boolean scriptCheck;

    @Value("${file.upload.user.data.dir:/data/tmp}")
    private String fileDir;

    @Autowired
    private DiffFileApi fileApi;
    @Autowired
    private ScriptManageDAO scriptManageDAO;
    @Autowired
    private ScriptFileRefDAO scriptFileRefDAO;
    @Autowired
    private FileManageDAO fileManageDAO;
    @Autowired
    private ScriptTagRefDAO scriptTagRefDAO;
    @Autowired
    private TagManageDAO tagManageDAO;
    //FIXME 这里不要直接用mapper，用dao，而且mapper用新版本，不带T的， 带T的逐渐要删除
    @Autowired
    private TBusinessLinkManageTableMapper tBusinessLinkManageTableMapper;
    //FIXME 这里不要直接用mapper，用dao，而且mapper用新版本，不带T的， 带T的逐渐要删除
    @Autowired
    private TSceneMapper tSceneMapper;
    @Autowired
    private LinkManageService linkManageService;
    @Autowired
    private SceneManageApi sceneManageApi;
    //FIXME 这里不要直接用mapper，用dao，而且mapper用新版本，不带T的， 带T的逐渐要删除
    @Autowired
    private TSceneLinkRelateMapper tSceneLinkRelateMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessLinkManageDAO businessLinkManageDAO;
    @Autowired
    private LinkManageDAO linkManageDAO;

    @Override
    public String getZipFileUrl(Long scriptDeployId) {
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(
            scriptDeployId);
        if (scriptManageDeployResult == null) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DOWNLOAD_VALID_ERROR, "脚本不存在！");
        }
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployId(scriptDeployId);
        if (CollectionUtils.isEmpty(scriptFileRefResults)) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DOWNLOAD_VALID_ERROR, "没有关联的文件！");
        }
        List<Long> fileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(
            Collectors.toList());
        List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);
        List<String> uploadPaths = fileManageResults.stream().map(FileManageResult::getUploadPath).collect(
            Collectors.toList());
        String targetScriptPath = getTargetScriptPath(scriptManageDeployResult);
        String fileName = scriptManageDeployResult.getName() + ".zip";
        Boolean result = fileApi.zipFile(targetScriptPath, uploadPaths, fileName);
        if (!result) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DOWNLOAD_VALID_ERROR, "文件打包下载失败！");
        }
        // todo 临时方案
        String url = null;
        try {
            url = fileUploadUrl + FileManageConstant.CLOUD_FILE_DOWN_LOAD_API + targetScriptPath + URLEncoder.encode(fileName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] cmds = {"curl","-o",fileDir +"/" + fileName,"-OL","-H", "licenseKey:"+RemoteConstant.LICENSE_VALUE, url};
        LinuxHelper.execCurl(cmds);
        return fileDir + "/" + fileName;
    }

    @Override
    public Long createScriptManage(ScriptManageDeployCreateRequest scriptManageDeployCreateRequest) {
        checkCreateScriptManageParam(scriptManageDeployCreateRequest);
        scriptManageDeployCreateRequest.setName(scriptManageDeployCreateRequest.getName().trim());
        List<ScriptManageResult> scriptManageResults = scriptManageDAO.selectScriptManageByName(
            scriptManageDeployCreateRequest.getName());
        if (CollectionUtils.isNotEmpty(scriptManageResults)) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, "脚本名称重复！");
        }
        uploadCreateScriptFile(scriptManageDeployCreateRequest.getFileManageCreateRequests());
        List<FileManageCreateRequest> scriptFile = scriptManageDeployCreateRequest.getFileManageCreateRequests()
            .stream().filter(o -> o.getFileType() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(scriptFile) || scriptFile.size() != 1) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, "脚本文件不唯一！");
        }
        ScriptCheckDTO scriptCheckDTO = checkAndUpdateScript(scriptManageDeployCreateRequest.getRefType(),
            scriptManageDeployCreateRequest.getRefValue(),
            tmpFilePath + scriptFile.get(0).getUploadId() + "/" + scriptFile.get(0).getFileName());
        if (scriptCheckDTO != null && !StringUtil.isBlank(scriptCheckDTO.getErrmsg())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, scriptCheckDTO.getErrmsg());
        }
        if (scriptCheckDTO != null && scriptCheckDTO.getIsHttp() != null && !scriptCheckDTO.getIsHttp()) {
            if (CollectionUtils.isEmpty(scriptManageDeployCreateRequest.getPluginConfigCreateRequests())) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, "存在不是http的业务活动，但没有传插件！");
            }
        }

        ScriptManageDeployCreateParam scriptManageDeployCreateParam = new ScriptManageDeployCreateParam();
        BeanUtils.copyProperties(scriptManageDeployCreateRequest, scriptManageDeployCreateParam);
        scriptManageDeployCreateParam.setStatus(0);
        scriptManageDeployCreateParam.setScriptVersion(1);

        if (CollectionUtils.isNotEmpty(scriptManageDeployCreateRequest.getPluginConfigCreateRequests())) {
            Map<String, Object> features = Maps.newHashMap();
            features.put(FeaturesConstants.PLUGIN_CONFIG,
                scriptManageDeployCreateRequest.getPluginConfigCreateRequests());
            scriptManageDeployCreateParam.setFeature(JSON.toJSONString(features));
        }
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.createScriptManageDeploy(
            scriptManageDeployCreateParam);
        String targetScriptPath = getTargetScriptPath(scriptManageDeployResult);
        List<String> tmpFilePaths = scriptManageDeployCreateRequest.getFileManageCreateRequests().stream().map(
            o -> tmpFilePath + o.getUploadId() + "/" + o.getFileName()).collect(Collectors.toList());
        fileApi.copyFile(targetScriptPath, tmpFilePaths);
        fileApi.deleteFile(tmpFilePaths);
        List<FileManageCreateParam> fileManageCreateParams = getFileManageCreateParams(
            scriptManageDeployCreateRequest.getFileManageCreateRequests(), targetScriptPath);
        List<Long> fileIds = fileManageDAO.createFileManageList(fileManageCreateParams);
        scriptFileRefDAO.createScriptFileRefs(fileIds, scriptManageDeployResult.getId());
        return scriptManageDeployResult.getId();
    }

    /**
     * 如果前端有上传脚本文件内容，说明之前的文件是没有用了，所以如果是临时文件，将文件删除，重新创建临时文件
     *
     * @param fileManageCreateRequests
     */
    private void uploadCreateScriptFile(List<FileManageCreateRequest> fileManageCreateRequests) {
        if (CollectionUtils.isNotEmpty(fileManageCreateRequests)) {
            for (FileManageCreateRequest fileManageCreateRequest : fileManageCreateRequests) {
                if (fileManageCreateRequest.getIsDeleted() == 0 && fileManageCreateRequest.getFileType() == 0
                    && !StringUtil.isBlank(fileManageCreateRequest.getScriptContent())) {
                    if (!StringUtil.isBlank(fileManageCreateRequest.getUploadId())) {
                        String tempFile = tmpFilePath + fileManageCreateRequest.getUploadId() + "/"
                            + fileManageCreateRequest.getFileName();
                        fileApi.deleteFile(Collections.singletonList(tempFile));
                    }
                    UUID uuid = UUID.randomUUID();
                    fileManageCreateRequest.setUploadId(uuid.toString());
                    String tempFile = tmpFilePath + fileManageCreateRequest.getUploadId() + "/"
                        + fileManageCreateRequest.getFileName();
                    fileApi.createFileByPathAndString(tempFile, fileManageCreateRequest.getScriptContent());
                }
            }

        }
    }

    /**
     * 上传的脚本文件更新
     *
     * @param fileManageCreateRequests 文件更新参数
     */
    private void uploadUpdateScriptFile(List<FileManageUpdateRequest> fileManageCreateRequests) {
        if (CollectionUtils.isEmpty(fileManageCreateRequests)) {
            return;
        }

        // 脚本文件遍历删除, 创建
        for (FileManageUpdateRequest fileManageUpdateRequest : fileManageCreateRequests) {
            if (StringUtil.isNotBlank(fileManageUpdateRequest.getScriptContent())) {
                // uploadId 不为空的, 删除
                if (StringUtil.isNotBlank(fileManageUpdateRequest.getUploadId())) {
                    String tempFile = tmpFilePath + fileManageUpdateRequest.getUploadId() + "/"
                        + fileManageUpdateRequest.getFileName();
                    fileApi.deleteFile(Collections.singletonList(tempFile));
                }

                // 新建
                UUID uuid = UUID.randomUUID();
                fileManageUpdateRequest.setUploadId(uuid.toString());
                fileManageUpdateRequest.setId(null);
                String tempFile = tmpFilePath + fileManageUpdateRequest.getUploadId() + "/"
                    + fileManageUpdateRequest.getFileName();
                fileApi.createFileByPathAndString(tempFile, fileManageUpdateRequest.getScriptContent());
            }
        }
    }

    @Override
    public ScriptCheckDTO checkAndUpdateScript(String refType, String refValue, String scriptFileUploadPath) {
        ScriptCheckDTO dto = new ScriptCheckDTO();
        if (scriptCheck == null || !scriptCheck) {
            return dto;
        }

        SceneParseReq sceneParseReq = new SceneParseReq();
        sceneParseReq.setUploadPath(scriptFileUploadPath);
        sceneParseReq.setAbsolutePath(true);
        sceneParseReq.setScriptId(1L);
        ResponseResult<Map<String, Object>> mapResponseResult = sceneManageApi.parseAndUpdateScript(sceneParseReq);
        if (mapResponseResult == null || mapResponseResult.getData() == null) {
            log.error("从控制台拿去数据报错：{}", scriptFileUploadPath);
            dto.setErrmsg("从控制台拿去数据报错：" + scriptFileUploadPath);
            return dto;
        }
        Map<String, Object> dataMap = mapResponseResult.getData();
        List<Map<String, Object>> voList = (List<Map<String, Object>>)dataMap.get("requestUrl");
        if (CollectionUtils.isEmpty(voList)) {
            dto.setErrmsg("脚本中没有获取到请求链接！");
            return dto;
        }
        List<BusinessLinkManageTable> businessActivityList = getBusinessActivity(refType, refValue);
        if (CollectionUtils.isEmpty(businessActivityList)) {
            dto.setErrmsg("找不到关联的业务活动！");
            return dto;
        }
        Set<String> errorSet = new HashSet<>();
        int unbindCount = 0;
        Map<String, Integer> urlMap = new HashMap<>();
        for (BusinessLinkManageTable data : businessActivityList) {
            if (StringUtil.isBlank(data.getEntrace())) {
                continue;
            }
            Set<String> tempErrorSet = new HashSet<>();
            EntranceJoinEntity entranceJoinEntity = ActivityUtil.covertEntrance(data.getEntrace());
            if (!entranceJoinEntity.getRpcType().equals(RpcType.TYPE_WEB_SERVER + "")) {
                dto.setIsHttp(false);
            }
            Map<String, String> map = UrlUtil.convertUrl(entranceJoinEntity);
            for (Map<String, Object> temp : voList) {
                ScriptUrlVO urlVO = JsonHelper.json2Bean(JsonHelper.bean2Json(temp), ScriptUrlVO.class);
                if (UrlUtil.checkEqual(map.get("url"), urlVO.getPath()) && urlVO.getEnable()) {
                    unbindCount = unbindCount + 1;
                    tempErrorSet.clear();
                    if (!urlMap.containsKey(urlVO.getName())) {
                        urlMap.put(urlVO.getName(), 1);
                    } else {
                        urlMap.put(urlVO.getName(), urlMap.get(urlVO.getName()) + 1);
                    }
                    break;
                } else {
                    tempErrorSet.add(data.getLinkName());
                }
            }
            errorSet.addAll(tempErrorSet);
        }
        Set<String> urlErrorSet = new HashSet<>();
        urlMap.forEach((k, v) -> {
            if (v > 1) {
                urlErrorSet.add("脚本中[" + k + "]重复" + v + "次");
            }
        });
        if (urlErrorSet.size() > 0) {
            dto.setMatchActivity(false);
            dto.setErrmsg("脚本文件配置不正确:" + urlErrorSet.toString());
        }
        //存在业务活动都关联不上脚本中的请求连接
        if (businessActivityList.size() > unbindCount) {
            dto.setMatchActivity(false);
            dto.setErrmsg("业务活动与脚本文件不匹配:" + errorSet.toString());
        }

        return dto;
    }

    private List<BusinessLinkManageTable> getBusinessActivity(String refType, String refValue) {
        if (ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE.equals(refType)) {
            return tBusinessLinkManageTableMapper.selectBussinessLinkByIdList(
                Collections.singletonList(Long.valueOf(refValue)));
        }
        if (ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE.equals(refType)) {
            List<SceneLinkRelate> sceneLinkRelates = tSceneLinkRelateMapper.selectBySceneId(Long.valueOf(refValue));
            if (CollectionUtils.isNotEmpty(sceneLinkRelates)) {
                List<Long> businessActivityIds = sceneLinkRelates.stream().map(o -> Long.valueOf(o.getBusinessLinkId()))
                    .collect(Collectors.toList());
                return tBusinessLinkManageTableMapper.selectBussinessLinkByIdList(businessActivityIds);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateScriptManage(ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest) {
        // 参数校验
        this.checkUpdateScriptManageParam(scriptManageDeployUpdateRequest);
        // 名称去除空格
        scriptManageDeployUpdateRequest.setName(scriptManageDeployUpdateRequest.getName().trim());

        // 脚本文件
        List<FileManageUpdateRequest> scriptFile =
            scriptManageDeployUpdateRequest.getFileManageUpdateRequests().stream()
                .filter(o -> o.getIsDeleted() == 0
                    && FileTypeEnum.SCRIPT.getCode().equals(o.getFileType()))
                .collect(Collectors.toList());
        // 验证
        ScriptManageExceptionUtil.isUpdateValidError(CollectionUtils.isEmpty(scriptFile)
                || scriptFile.size() != 1, "脚本文件不唯一!");

        // 更新的脚本文件落盘
        this.uploadUpdateScriptFile(scriptFile);

        // 验证
        ScriptManageExceptionUtil.isUpdateValidError(CollectionUtils.isEmpty(scriptFile) || scriptFile.size() != 1, "脚本文件不唯一!");

        // 脚本文件 url
        String scriptFileUrl;
        if (scriptFile.get(0).getId() == null) {
            scriptFileUrl = tmpFilePath + scriptFile.get(0).getUploadId() + "/" + scriptFile.get(0).getFileName();
        } else {
            List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(
                Collections.singletonList(scriptFile.get(0).getId()));
            if (CollectionUtils.isEmpty(fileManageResults)) {
                log.error("已经存在的文件丢失，fileId:{}", scriptFile.get(0).getId());
                throw ScriptManageExceptionUtil.getUpdateValidError("服务器已经存在的文件丢失");
            }
            scriptFileUrl = fileManageResults.get(0).getUploadPath();
        }

        // cloud 那边, 检查, 更新脚本
        ScriptCheckDTO scriptCheckDTO = this.checkAndUpdateScript(scriptManageDeployUpdateRequest.getRefType(),
            scriptManageDeployUpdateRequest.getRefValue(), scriptFileUrl);

        // 判断错误信息
        if (scriptCheckDTO != null && !StringUtil.isBlank(scriptCheckDTO.getErrmsg())) {
            throw ScriptManageExceptionUtil.getUpdateValidError(scriptCheckDTO.getErrmsg());
        }

        if (scriptCheckDTO != null && scriptCheckDTO.getIsHttp() != null && !scriptCheckDTO.getIsHttp()) {
            ScriptManageExceptionUtil.isCreateValidError(CollectionUtils.isEmpty(scriptManageDeployUpdateRequest.getPluginConfigUpdateRequests()), "存在不是http的业务活动，但没有传插件!");
        }

        // 更新脚本, 创建新的脚本实例
        ScriptManageDeployResult scriptManageDeployResult = this.updateScriptAndCreateScriptDeployAndGet(scriptManageDeployUpdateRequest);
        String targetScriptPath = this.getTargetScriptPath(scriptManageDeployUpdateRequest, scriptManageDeployResult);

        // 这里, 脚本文件, 附件, 都放在一起了
        List<FileManageUpdateRequest> addFileManageUpdateRequests = scriptManageDeployUpdateRequest
            .getFileManageUpdateRequests().stream()
            .filter(o -> o.getIsDeleted() == 0).collect(Collectors.toList());

        // 插入文件记录所需参数
        List<FileManageCreateParam> fileManageCreateParams = getFileManageCreateParamsByUpdateReq(
            addFileManageUpdateRequests, targetScriptPath);

        // 创建文件记录, 获得文件ids
        List<Long> fileIds = fileManageDAO.createFileManageList(fileManageCreateParams);

        // 更新 cloud 上, 该脚本id
        // 脚本实例 id
        Long scriptId = scriptManageDeployUpdateRequest.getId();
        // 注意: 两方的事务, 没有, 不能保证操作的原子性
        this.updateCloudFileByScriptId(scriptId, scriptManageDeployResult.getType(), fileManageCreateParams);

        // 文件ids与脚本实例id做关联
        scriptFileRefDAO.createScriptFileRefs(fileIds, scriptManageDeployResult.getId());
    }

    /**
     * 更新 scriptId 关联的所有场景下的脚本文件名称
     * @param scriptId                 脚本实例id
     * @param scriptType 脚本类型
     * @param files 所有文件
     */
    private void updateCloudFileByScriptId(Long scriptId, Integer scriptType, List<FileManageCreateParam> files) {
        if (scriptId == null || CollectionUtils.isEmpty(files)) {
            return;
        }

        // 参数转换
        UpdateSceneFileRequest request = new UpdateSceneFileRequest();
        request.setScriptId(scriptId);
        request.setScriptType(scriptType);

        List<UploadFileDTO> uploadFiles = files.stream().map(file -> {
            UploadFileDTO uploadFileDTO = new UploadFileDTO();
            BeanUtils.copyProperties(file, uploadFileDTO);
            uploadFileDTO.setIsDeleted(0);
            String fileExtend = file.getFileExtend();
            if (StringUtils.isNotBlank(fileExtend)) {
                FileExtendVO fileExtendVO = JSONUtil.toBean(fileExtend, FileExtendVO.class);
                uploadFileDTO.setIsSplit(fileExtendVO.getIsSplit());
            }
            uploadFileDTO.setUploadedData(0L);
            uploadFileDTO.setUploadTime(DateUtil.format(file.getUploadTime(), AppConstants.DATE_FORMAT_STRING));
            return uploadFileDTO;
        }).collect(Collectors.toList());
        request.setUploadFiles(uploadFiles);

        // cloud 更新
        ResponseResult response = sceneManageApi.updateSceneFileByScriptId(request);
        if (!response.getSuccess()) {
            log.error("脚本更新 --> 对应的 cloud 场景, 脚本文件更新失败, 错误信息: {}", JSONUtil.toJsonStr(response));
            throw ScriptManageExceptionUtil.getUpdateValidError(String.format("对应的 cloud 脚本更新失败, 错误信息: %s", response.getError().getSolution()));
        }
    }

    /**
     * 获得目标脚本路径
     *
     * @param scriptManageDeployUpdateRequest 脚本更新所需参数
     * @param scriptManageDeployResult 脚本实例
     * @return 目标脚本路径
     */
    private String getTargetScriptPath(ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest,
        ScriptManageDeployResult scriptManageDeployResult) {
        List<String> sourcePaths = new ArrayList<>();
        // 脚本路径前缀 目录 + 脚本id + 版本
        String targetScriptPath = this.getTargetScriptPath(scriptManageDeployResult);

        List<String> tmpFilePaths = scriptManageDeployUpdateRequest.getFileManageUpdateRequests().stream().filter(
            o -> o.getIsDeleted() == 0
                && !StringUtil.isBlank(o.getUploadId())).map(
            o -> tmpFilePath + o.getUploadId() + "/" + o.getFileName()).collect(Collectors.toList());
        List<Long> existFileIds = scriptManageDeployUpdateRequest.getFileManageUpdateRequests().stream().filter(
            o -> o.getIsDeleted() == 0
                && StringUtil.isBlank(o.getUploadId()) && o.getId() != null).map(FileManageUpdateRequest::getId)
            .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(existFileIds)) {
            List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(existFileIds);
            List<String> uploadPaths = fileManageResults.stream().map(FileManageResult::getUploadPath).collect(
                Collectors.toList());
            sourcePaths.addAll(uploadPaths);
        }

        if (CollectionUtils.isNotEmpty(tmpFilePaths)) {
            sourcePaths.addAll(tmpFilePaths);
        }
        fileApi.copyFile(targetScriptPath, sourcePaths);
        if (CollectionUtils.isNotEmpty(tmpFilePaths)) {
            fileApi.deleteFile(tmpFilePaths);
        }
        return targetScriptPath;
    }

    /**
     * 更新脚本, 创建新的脚本实例, 获得脚本实例结果
     *
     * @param scriptManageDeployUpdateRequest 创建, 更新, 所需参数
     * @return 获得脚本实例结果
     */
    private ScriptManageDeployResult updateScriptAndCreateScriptDeployAndGet(
        ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest) {
        // 脚本实例id
        Long scriptDeployId = scriptManageDeployUpdateRequest.getId();
        // 通过脚本实例id获得脚本实例
        ScriptManageDeployResult oldScriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(
            scriptDeployId);
        ScriptManageExceptionUtil.isUpdateValidError(oldScriptManageDeployResult == null, "更新的脚本实例不存在!");

        // 脚本id, 不是脚本实例id
        Long scriptId = oldScriptManageDeployResult.getScriptId();
        // 脚本获得
        ScriptManageResult scriptManageResult = scriptManageDAO.selectScriptManageById(scriptId);
        ScriptManageExceptionUtil.isUpdateValidError(scriptManageResult == null, "更新的脚本实例对应的脚本不存在!");

        // 声明脚本创建参数
        ScriptManageDeployCreateParam scriptManageDeployCreateParam = new ScriptManageDeployCreateParam();
        BeanUtils.copyProperties(scriptManageDeployUpdateRequest, scriptManageDeployCreateParam);
        scriptManageDeployCreateParam.setScriptId(scriptId);
        scriptManageDeployCreateParam.setStatus(ScriptManageDeployStatusEnum.NEW.getCode());
        // 版本 + 1
        int scriptNewVersion = scriptManageResult.getScriptVersion() + 1;
        scriptManageDeployCreateParam.setScriptVersion(scriptNewVersion);

        //更新脚本状态，将脚本实例更新为历史状态
        scriptManageDAO.updateScriptVersion(scriptId, scriptNewVersion);

        // 创建新的脚本实例
        Map<String, Object> features = Maps.newHashMap();
        features.put(FeaturesConstants.PLUGIN_CONFIG, scriptManageDeployUpdateRequest.getPluginConfigUpdateRequests());
        scriptManageDeployCreateParam.setFeature(JSON.toJSONString(features));
        return scriptManageDAO.createScriptManageDeploy(scriptManageDeployCreateParam);
    }

    @Override
    public List<ScriptManageSceneManageResponse> getAllScenes(String businessFlowName) {
        List<ScriptManageSceneManageResponse> scriptManageSceneManageResponses = new ArrayList<>();
        try {
            List<BusinessFlowIdAndNameDto> businessFlowIdAndNameDtos = linkManageService.businessFlowIdFuzzSearch(
                businessFlowName);
            if (CollectionUtils.isNotEmpty(businessFlowIdAndNameDtos)) {
                scriptManageSceneManageResponses = businessFlowIdAndNameDtos.stream().map(businessFlowIdAndNameDto -> {
                    ScriptManageSceneManageResponse scriptManageSceneManageResponse
                        = new ScriptManageSceneManageResponse();
                    scriptManageSceneManageResponse.setId(businessFlowIdAndNameDto.getId());
                    scriptManageSceneManageResponse.setSceneName(businessFlowIdAndNameDto.getBusinessFlowName());
                    return scriptManageSceneManageResponse;

                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("查询业务流程异常");
        }
        if (CollectionUtils.isNotEmpty(scriptManageSceneManageResponses)) {
            List<String> systemProcessIds = scriptManageSceneManageResponses.stream().map(
                ScriptManageSceneManageResponse::getId).collect(Collectors.toList());
            List<Integer> status = new ArrayList<>();
            status.add(0);
            status.add(1);
            List<ScriptManageDeployResult> scriptManageDeployResults = scriptManageDAO.selectByRefIdsAndType(
                systemProcessIds, ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE, status);
            if (CollectionUtils.isNotEmpty(scriptManageDeployResults)) {
                Map<String, List<ScriptManageDeployResult>> systemProcessScriptMap = scriptManageDeployResults.stream()
                    .collect(Collectors.groupingBy(ScriptManageDeployResult::getRefValue));
                scriptManageSceneManageResponses.forEach(scriptManageSceneManageResponse -> {
                    List<ScriptManageDeployResult> scriptManageDeploys = systemProcessScriptMap.get(
                        scriptManageSceneManageResponse.getId());
                    scriptManageSceneManageResponse.setScriptManageDeployResponses(
                        getScriptManageDeployResponses(scriptManageDeploys));
                });
            }
        }
        return scriptManageSceneManageResponses;
    }

    private List<ScriptManageDeployActivityResponse> getScriptManageDeployResponses(
        List<ScriptManageDeployResult> scriptManageDeployResults) {
        if (CollectionUtils.isNotEmpty(scriptManageDeployResults)) {
            List<ScriptManageDeployActivityResponse> scriptManageDeployActivityResponses = new ArrayList<>();
            Map<Long, List<ScriptManageDeployResult>> collect = scriptManageDeployResults.stream().collect(
                Collectors.groupingBy(ScriptManageDeployResult::getScriptId));
            collect.forEach((k, v) -> {
                ScriptManageDeployResult scriptManageDeploy = v.stream().max(
                    Comparator.comparing(ScriptManageDeployResult::getScriptVersion)).get();
                ScriptManageDeployActivityResponse scriptManageDeployActivityResponse
                    = new ScriptManageDeployActivityResponse();
                scriptManageDeployActivityResponse.setId(scriptManageDeploy.getId());
                scriptManageDeployActivityResponse.setName(
                    scriptManageDeploy.getName() + " 版本" + scriptManageDeploy.getScriptVersion());
                scriptManageDeployActivityResponses.add(scriptManageDeployActivityResponse);
            });
            return scriptManageDeployActivityResponses;
        }
        return null;
    }

    @Override
    public List<ScriptManageActivityResponse> listAllActivities(String activityName) {
        // 业务活动的id和名字
        // 如果业务活动名称不存在的话, 获得该登录用户下的所有业务链路
        List<BusinessActiveIdAndNameDto> businessActiveIdAndNameList = linkManageService.businessActiveNameFuzzSearch(
            activityName);
        if (CollectionUtils.isEmpty(businessActiveIdAndNameList)) {
            return Collections.emptyList();
        }

        // 转换
        List<ScriptManageActivityResponse> scriptManageActivityResponses = businessActiveIdAndNameList.stream()
            .map(businessActiveIdAndNameDto -> {
                ScriptManageActivityResponse scriptManageActivityResponse = new ScriptManageActivityResponse();
                scriptManageActivityResponse.setId(businessActiveIdAndNameDto.getId());
                scriptManageActivityResponse.setBusinessActiveName(businessActiveIdAndNameDto.getBusinessActiveName());
                return scriptManageActivityResponse;
            }).collect(Collectors.toList());

        // 获得所有业务活动id
        List<String> activityIds = scriptManageActivityResponses.stream()
            .map(ScriptManageActivityResponse::getId)
            .collect(Collectors.toList());
        if (activityIds.isEmpty()) {
            return scriptManageActivityResponses;
        }

        // 脚本实例的状态, 新建, 和 调试通过的
        List<Integer> scriptManageDeployStatusList = Arrays.asList(ScriptManageDeployStatusEnum.NEW.getCode(),
            ScriptManageDeployStatusEnum.PASS.getCode());

        // 查询出业务链路对应的脚本实例列表
        List<ScriptManageDeployResult> scriptManageDeployResults = scriptManageDAO.selectByRefIdsAndType(
            activityIds, ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE, scriptManageDeployStatusList);
        if (CollectionUtils.isEmpty(scriptManageDeployResults)) {
            return scriptManageActivityResponses;
        }

        // 脚本实例, 根据业务活动id, 分组
        Map<String, List<ScriptManageDeployResult>> activityScriptMap = scriptManageDeployResults.stream()
            .filter(scriptManageDeploy -> StringUtils.isNotBlank(scriptManageDeploy.getRefValue()))
            .collect(Collectors.groupingBy(ScriptManageDeployResult::getRefValue));
        // 业务活动赋值脚本实例
        scriptManageActivityResponses.forEach(scriptManageActivityResponse -> {
            List<ScriptManageDeployResult> scriptManageDeploys = activityScriptMap.get(scriptManageActivityResponse.getId());
            scriptManageActivityResponse.setScriptManageDeployResponses(getScriptManageDeployResponses(scriptManageDeploys));
        });

        return scriptManageActivityResponses;
    }

    @Override
    public String explainScriptFile(String scriptFileUploadPath) {
        SceneParseReq sceneParseReq = new SceneParseReq();
        sceneParseReq.setUploadPath(scriptFileUploadPath);
        sceneParseReq.setScriptId(1L);
        sceneParseReq.setAbsolutePath(true);
        ResponseResult<Map<String, Object>> mapResponseResult = sceneManageApi.parseScript(sceneParseReq);
        if (mapResponseResult != null && mapResponseResult.getData() != null) {
            Map<String, Object> data = mapResponseResult.getData();
            if (data.get("xmlContent") != null) {
                return XmlHelper.formatXml(data.get("xmlContent").toString());
            }
        }
        return "";
    }

    @Override
    public String getFileDownLoadUrl(String filePath) {
        if(StringUtils.isBlank(filePath)) {
            return "";
        }
        String[] file = filePath.split("/");
        String fileName = file[file.length -1];
        String url = null;
        try {
            url = fileUploadUrl + FileManageConstant.CLOUD_FILE_DOWN_LOAD_API + URLEncoder.encode(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // todo 临时方案
        String[] cmds = {"curl","-o",fileDir +"/" + fileName,"-OL","-H", "licenseKey:"+RemoteConstant.LICENSE_VALUE,url};
        LinuxHelper.execCurl(cmds);
        return  fileDir + "/" +fileName;
    }

    @Override
    public List<ScriptManageDeployResponse> listScriptDeployByScriptId(Long scriptId) {
        return TraceManageResponseConvertor.INSTANCE.ofListScriptManageDeployResponse(scriptManageDAO.selectScriptManageDeployByScriptId(scriptId));
    }

    @Override
    public String rollbackScriptDeploy(Long scriptDeployId) {
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(scriptDeployId);
        ScriptManageResult scriptManageResult = scriptManageDAO.selectScriptManageById(scriptManageDeployResult.getScriptId());
        if (scriptManageDeployResult.getScriptVersion().equals(scriptManageResult.getScriptVersion())){
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_ROLLBACK_VALID_ERROR, "当前脚本实例已是最新版本！");
        }
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployId(scriptManageDeployResult.getId());
        if (CollectionUtils.isEmpty(scriptFileRefResults)){
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_ROLLBACK_VALID_ERROR, "该脚本实例没有关联文件！");
        }
        List<Long> scriptFileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(Collectors.toList());
        List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(scriptFileIds);
        ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest = getScriptManageDeployUpdateRequest(scriptManageDeployResult,fileManageResults);
        updateScriptManage(scriptManageDeployUpdateRequest);
        return scriptManageResult.getName();
    }

    @Override
    public List<ScriptManageXmlContentResponse> getScriptManageDeployXmlContent(List<Long> scriptManageDeployIds) {
        List<ScriptManageXmlContentResponse> results = new ArrayList<>();
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployIds(scriptManageDeployIds);
        if (CollectionUtils.isNotEmpty(scriptFileRefResults)){
            Map<Long, List<ScriptFileRefResult>> collect = scriptFileRefResults.stream().collect(Collectors.groupingBy(ScriptFileRefResult::getScriptDeployId));
            collect.forEach((k,v) -> {
                ScriptManageXmlContentResponse scriptManageXmlContentResponse = new ScriptManageXmlContentResponse();
                scriptManageXmlContentResponse.setScriptManageDeployId(k);
                List<Long> fileIds = v.stream().map(ScriptFileRefResult::getFileId).collect(Collectors.toList());
                List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);
                List<FileManageResult> scriptFiles = fileManageResults.stream().filter(o -> o.getFileType() == 0).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(scriptFiles)){
                    scriptManageXmlContentResponse.setContent(explainScriptFile(scriptFiles.get(0).getUploadPath()));
                }
                results.add(scriptManageXmlContentResponse);
            });

        }
        return results;
    }

    private ScriptManageDeployUpdateRequest getScriptManageDeployUpdateRequest(ScriptManageDeployResult scriptManageDeployResult,List<FileManageResult> fileManageResults) {
        ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest = new ScriptManageDeployUpdateRequest();
        scriptManageDeployUpdateRequest.setId(scriptManageDeployResult.getId());
        scriptManageDeployUpdateRequest.setName(scriptManageDeployResult.getName());
        scriptManageDeployUpdateRequest.setRefType(scriptManageDeployResult.getRefType());
        scriptManageDeployUpdateRequest.setRefValue(scriptManageDeployResult.getRefValue());
        scriptManageDeployUpdateRequest.setType(scriptManageDeployResult.getType());
        List<FileManageUpdateRequest> fileManageUpdateRequests = new ArrayList<>();
        fileManageResults.forEach(fileManageResult -> {
            FileManageUpdateRequest fileManageUpdateRequest = new FileManageUpdateRequest();
            fileManageUpdateRequest.setId(fileManageResult.getId());
            fileManageUpdateRequest.setFileName(fileManageResult.getFileName());
            fileManageUpdateRequest.setFileSize(fileManageResult.getFileSize());
            fileManageUpdateRequest.setFileType(fileManageResult.getFileType());
            if(fileManageResult.getFileExtend() != null) {
                Map<String, Object> stringObjectMap = JsonHelper.json2Map(fileManageResult.getFileExtend(),
                        String.class, Object.class);
                if (stringObjectMap != null && stringObjectMap.get("dataCount") != null && !StringUtils.isBlank(stringObjectMap.get("dataCount").toString())) {
                    fileManageUpdateRequest.setDataCount(Long.valueOf(stringObjectMap.get("dataCount").toString()));
                }
                if (stringObjectMap != null && stringObjectMap.get("isSplit") != null && !StringUtils.isBlank(stringObjectMap.get("isSplit").toString())) {
                    fileManageUpdateRequest.setIsSplit(Integer.valueOf(stringObjectMap.get("isSplit").toString()));
                }
            }
            fileManageUpdateRequest.setIsDeleted(fileManageResult.getIsDeleted());
            fileManageUpdateRequest.setUploadTime(fileManageResult.getUploadTime());
            fileManageUpdateRequests.add(fileManageUpdateRequest);
        });
        scriptManageDeployUpdateRequest.setFileManageUpdateRequests(fileManageUpdateRequests);
        return scriptManageDeployUpdateRequest;
    }


    @Override
    public List<SupportJmeterPluginNameResponse> getSupportJmeterPluginNameList(
        SupportJmeterPluginNameRequest nameRequest) {
        List<SupportJmeterPluginNameResponse> nameResponseList = Lists.newArrayList();
        String refType = nameRequest.getRelatedType();
        String refValue = nameRequest.getRelatedId();
        //获取所有业务活动
        List<BusinessLinkManageTable> businessActivityList = getBusinessActivity(refType, refValue);
        if (CollectionUtils.isEmpty(businessActivityList)) {
            log.error("未查询到业务活动信息:[refType:{},refValue:{}]", refType, refValue);
            return nameResponseList;
        }
        //获取所有系统流程id
        List<Long> businessLinkIdList = businessActivityList.stream()
            .map(BusinessLinkManageTable::getLinkId)
            .collect(Collectors.toList());
        List<BusinessLinkResult> businessLinkResultList = businessLinkManageDAO.getListByIds(businessLinkIdList);
        List<Long> techLinkIdList = businessLinkResultList.stream()
            .map(BusinessLinkResult::getRelatedTechLink)
            .map(Long::parseLong)
            .collect(Collectors.toList());
        //获取所有系统流程入口类型
        LinkManageQueryParam queryParam = new LinkManageQueryParam();
        queryParam.setLinkIdList(techLinkIdList);
        List<LinkManageResult> linkManageResultList = linkManageDAO.selectList(queryParam);
        if (CollectionUtils.isEmpty(linkManageResultList)) {
            log.error("未查询到服务信息:[techLinkIdList:{}]", JSON.toJSONString(techLinkIdList));
            return nameResponseList;
        }
        List<String> typeList = linkManageResultList.stream()
            .map(LinkManageResult::getFeatures)
            .map(features -> JSON.parseObject(features, Map.class))
            .map(featuresObj -> featuresObj.get(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY))
            .map(String::valueOf)
            .distinct()
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(typeList)) {
            log.error("未查询到类型信息:[techLinkIdList:{}]", JSON.toJSONString(techLinkIdList));
            return nameResponseList;
        }
        EnginePluginFetchWrapperReq fetchWrapperReq = new EnginePluginFetchWrapperReq();
        fetchWrapperReq.setPluginTypes(typeList);
        ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>> responseResult
            = sceneManageApi.listEnginePlugins(fetchWrapperReq);
        Map<String, List<EnginePluginSimpleInfoResp>> dataMap = responseResult.getData();
        if (Objects.isNull(responseResult) || dataMap.isEmpty()) {
            log.error("未查询到插件信息:[typeList:{}]", JSON.toJSONString(typeList));
            return nameResponseList;
        }
        nameResponseList = typeList.stream().map(type -> {
            SupportJmeterPluginNameResponse nameResponse = new SupportJmeterPluginNameResponse();
            nameResponse.setType(type);
            type = type.toLowerCase();
            if (dataMap.containsKey(type)) {
                List<EnginePluginSimpleInfoResp> pluginSimpleInfoResps = dataMap.get(type);
                if (CollectionUtils.isNotEmpty(pluginSimpleInfoResps)) {
                    List<SinglePluginRenderResponse> singlePluginRenderResponseList;
                    singlePluginRenderResponseList = pluginSimpleInfoResps.stream().map(enginePluginSimpleInfoResp -> {
                        SinglePluginRenderResponse renderResponse = new SinglePluginRenderResponse();
                        renderResponse.setLabel(enginePluginSimpleInfoResp.getPluginName());
                        renderResponse.setValue(enginePluginSimpleInfoResp.getPluginId());
                        return renderResponse;
                    }).collect(Collectors.toList());
                    nameResponse.setSinglePluginRenderResponseList(singlePluginRenderResponseList);
                }
            }
            return nameResponse;
        }).collect(Collectors.toList());
        return nameResponseList;
    }

    @Override
    public SupportJmeterPluginVersionResponse getSupportJmeterPluginVersionList(
        SupportJmeterPluginVersionRequest versionRequest) {
        Long pluginId = versionRequest.getPluginId();
        EnginePluginDetailsWrapperReq wrapperReq = new EnginePluginDetailsWrapperReq();
        wrapperReq.setPluginId(pluginId);
        ResponseResult<EnginePluginDetailResp> responseResult = sceneManageApi.getEnginePluginDetails(wrapperReq);
        EnginePluginDetailResp detailResp = responseResult.getData();
        if (!Objects.isNull(detailResp)) {
            SupportJmeterPluginVersionResponse versionResponse = new SupportJmeterPluginVersionResponse();
            versionResponse.setVersionList(detailResp.getSupportedVersions());
            return versionResponse;
        }
        return null;
    }

    private List<FileManageCreateParam> getFileManageCreateParamsByUpdateReq(
        List<FileManageUpdateRequest> fileManageUpdateRequests, String targetScriptPath) {
        return fileManageUpdateRequests.stream().map(fileManageUpdateRequest -> {
            FileManageCreateParam fileManageCreateParam = new FileManageCreateParam();
            fileManageCreateParam.setFileName(fileManageUpdateRequest.getFileName());
            fileManageCreateParam.setFileSize(fileManageUpdateRequest.getFileSize());
            fileManageCreateParam.setFileType(fileManageUpdateRequest.getFileType());
            Map<String, Object> fileExtend = new HashMap<>();
            fileExtend.put("dataCount", fileManageUpdateRequest.getDataCount());
            fileExtend.put("isSplit", fileManageUpdateRequest.getIsSplit());
            fileManageCreateParam.setCustomerId(RestContext.getUser().getCustomerId());
            fileManageCreateParam.setFileExtend(JsonHelper.bean2Json(fileExtend));
            fileManageCreateParam.setUploadPath(targetScriptPath + fileManageUpdateRequest.getFileName());
            fileManageCreateParam.setUploadTime(fileManageUpdateRequest.getUploadTime());
            return fileManageCreateParam;
        }).collect(Collectors.toList());

    }

    private List<FileManageCreateParam> getFileManageCreateParams(
        List<FileManageCreateRequest> fileManageCreateRequests, String targetScriptPath) {
        return fileManageCreateRequests.stream().map(fileManageCreateRequest -> {
            FileManageCreateParam fileManageCreateParam = new FileManageCreateParam();
            fileManageCreateParam.setFileName(fileManageCreateRequest.getFileName());
            fileManageCreateParam.setFileSize(fileManageCreateRequest.getFileSize());
            fileManageCreateParam.setFileType(fileManageCreateRequest.getFileType());
            Map<String, Object> fileExtend = new HashMap<>();
            fileExtend.put("dataCount", fileManageCreateRequest.getDataCount());
            fileExtend.put("isSplit", fileManageCreateRequest.getIsSplit());
            fileManageCreateParam.setFileExtend(JsonHelper.bean2Json(fileExtend));
            fileManageCreateParam.setCustomerId(RestContext.getUser().getCustomerId());
            fileManageCreateParam.setUploadPath(targetScriptPath + fileManageCreateRequest.getFileName());
            fileManageCreateParam.setUploadTime(fileManageCreateRequest.getUploadTime());
            return fileManageCreateParam;
        }).collect(Collectors.toList());

    }

    @Override
    public PagingList<ScriptManageDeployResponse> pageQueryScriptManage(
        ScriptManageDeployPageQueryRequest scriptManageDeployPageQueryRequest) {
        ScriptManageDeployPageQueryParam scriptManageDeployPageQueryParam = new ScriptManageDeployPageQueryParam();
        if (scriptManageDeployPageQueryRequest != null) {
            BeanUtils.copyProperties(scriptManageDeployPageQueryRequest, scriptManageDeployPageQueryParam);
            if (!StringUtil.isBlank(scriptManageDeployPageQueryRequest.getBusinessActivityId())) {
                scriptManageDeployPageQueryParam.setRefType(ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE);
                scriptManageDeployPageQueryParam.setRefValue(
                    scriptManageDeployPageQueryRequest.getBusinessActivityId());
            }
            if (!StringUtil.isBlank(scriptManageDeployPageQueryRequest.getBusinessFlowId())) {
                scriptManageDeployPageQueryParam.setRefType(ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE);
                scriptManageDeployPageQueryParam.setRefValue(scriptManageDeployPageQueryRequest.getBusinessFlowId());
            }
            if (!StringUtil.isBlank(scriptManageDeployPageQueryRequest.getBusinessActivityId()) && !StringUtil
                .isBlank(scriptManageDeployPageQueryRequest.getBusinessFlowId())) {
                return PagingList.empty();
            }
            if (!CollectionUtils.isEmpty(scriptManageDeployPageQueryRequest.getTagIds())) {
                List<ScriptTagRefResult> scriptTagRefResults = scriptTagRefDAO.selectScriptTagRefByTagIds(
                    scriptManageDeployPageQueryRequest.getTagIds());
                if (CollectionUtils.isEmpty(scriptTagRefResults)) {
                    //标签中没有查到关联数据，直接返回空
                    return PagingList.empty();
                }
                Map<Long, List<ScriptTagRefResult>> scriptTagRefMap = scriptTagRefResults.stream().collect(
                    Collectors.groupingBy(ScriptTagRefResult::getScriptId));
                List<Long> scriptIds = new ArrayList<>();
                for (Map.Entry<Long, List<ScriptTagRefResult>> entry : scriptTagRefMap.entrySet()) {
                    if (entry.getValue().size() == scriptManageDeployPageQueryRequest.getTagIds().size()) {
                        scriptIds.add(entry.getKey());
                    }
                }
                if (CollectionUtils.isEmpty(scriptIds)) {
                    return PagingList.empty();
                }
                scriptManageDeployPageQueryParam.setScriptIds(scriptIds);
            }
        }
        scriptManageDeployPageQueryParam.setCurrent(scriptManageDeployPageQueryRequest.getCurrent());
        scriptManageDeployPageQueryParam.setPageSize(scriptManageDeployPageQueryRequest.getPageSize());
        // todo 目前就jmx 需要后续可以通过请求进来
        scriptManageDeployPageQueryParam.setScriptType(0);
        List<Long> userIdList = RestContext.getQueryAllowUserIdList();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            scriptManageDeployPageQueryParam.setUserIdList(userIdList);
        }
        PagingList<ScriptManageDeployResult> scriptManageDeployResults = scriptManageDAO
            .pageQueryRecentScriptManageDeploy(
                scriptManageDeployPageQueryParam);
        if (scriptManageDeployResults.isEmpty()) {
            return PagingList.empty();
        }
        // 获取实例个数
        Map<Long, Long> numMaps = scriptManageDAO.selectScriptDeployNumResult();
        //用户ids
        List<Long> userIds = scriptManageDeployResults.getList().stream().filter(data -> null != data.getUserId()).map(
            ScriptManageDeployResult::getUserId).collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
        List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
        List<Long> allowDownloadUserIdList = RestContext.getDownloadAllowUserIdList();
        List<ScriptManageDeployResponse> scriptManageDeployResponses = scriptManageDeployResults.getList().stream().map(
                scriptManageDeployResult -> {
                    ScriptManageDeployResponse response = new ScriptManageDeployResponse();
                    BeanUtils.copyProperties(scriptManageDeployResult, response);
                    // 判断有几个实例脚本
                    if(numMaps.get(response.getScriptId()) != null && numMaps.get(response.getScriptId()) > 1) {
                        response.setOnlyOne(false);
                    }
                    if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                        response.setCanEdit(allowUpdateUserIdList.contains(scriptManageDeployResult.getUserId()));
                    }
                    if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                        response.setCanRemove(allowDeleteUserIdList.contains(scriptManageDeployResult.getUserId()));
                    }
                    if (CollectionUtils.isNotEmpty(allowDownloadUserIdList)) {
                        response.setCanDownload(allowDownloadUserIdList.contains(scriptManageDeployResult.getUserId()));
                    }
                    //负责人id
                    response.setManagerId(scriptManageDeployResult.getUserId());
                    //负责人名称
                    String userName = Optional.ofNullable(userMap.get(scriptManageDeployResult.getUserId()))
                            .map(u -> u.getName())
                            .orElse("");
                    response.setManagerName(userName);
                    return response;
                }).collect(Collectors.toList());
        setFileList(scriptManageDeployResponses);
        setTagList(scriptManageDeployResponses);
        setRefName(scriptManageDeployResponses, scriptManageDeployResults);
        return PagingList.of(scriptManageDeployResponses, scriptManageDeployResults.getTotal());
    }

    private void setTagList(List<ScriptManageDeployResponse> scriptManageDeployResponses) {
        if (scriptManageDeployResponses == null || CollectionUtils.isEmpty(scriptManageDeployResponses)) {
            return;
        }
        List<Long> scriptIds = scriptManageDeployResponses.stream().map(ScriptManageDeployResponse::getScriptId)
            .collect(Collectors.toList());
        List<ScriptTagRefResult> scriptTagRefResults = scriptTagRefDAO.selectScriptTagRefByScriptIds(scriptIds);
        if (CollectionUtils.isEmpty(scriptTagRefResults)) {
            return;
        }
        List<Long> tagIds = scriptTagRefResults.stream().map(ScriptTagRefResult::getTagId).collect(Collectors.toList());
        List<TagManageResult> tagManageResults = tagManageDAO.selectScriptTagsByIds(tagIds);
        if (CollectionUtils.isEmpty(tagManageResults)) {
            return;
        }
        Map<Long, List<ScriptTagRefResult>> scriptTagRefMap = scriptTagRefResults.stream().collect(
            Collectors.groupingBy(ScriptTagRefResult::getScriptId));
        List<TagManageResponse> tagManageResponses = tagManageResults.stream().map(tagManageResult -> {
            TagManageResponse tagManageResponse = new TagManageResponse();
            tagManageResponse.setId(tagManageResult.getId());
            tagManageResponse.setTagName(tagManageResult.getTagName());
            return tagManageResponse;
        }).collect(Collectors.toList());
        Map<Long, TagManageResponse> tagManageResponseMap = tagManageResponses.stream().collect(
            Collectors.toMap(TagManageResponse::getId, a -> a, (k1, k2) -> k1));
        scriptManageDeployResponses.forEach(scriptManageDeployResponse -> {
            List<ScriptTagRefResult> scriptTagRefList = scriptTagRefMap.get(scriptManageDeployResponse.getScriptId());
            if (CollectionUtils.isEmpty(scriptTagRefList)) {
                return;
            }
            List<TagManageResponse> resultTagManageResponses = scriptTagRefList.stream().map(
                scriptTagRefResult -> tagManageResponseMap.get(scriptTagRefResult.getTagId())).collect(
                Collectors.toList());
            if (CollectionUtils.isEmpty(resultTagManageResponses)) {
                return;
            }
            scriptManageDeployResponse.setTagManageResponses(resultTagManageResponses);
        });
    }

    private void setFileList(List<ScriptManageDeployResponse> scriptManageDeployResponses) {
        if (scriptManageDeployResponses == null || CollectionUtils.isEmpty(scriptManageDeployResponses)) {
            return;
        }
        List<Long> scriptDeployIds = scriptManageDeployResponses.stream().map(ScriptManageDeployResponse::getId)
            .collect(Collectors.toList());
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployIds(
            scriptDeployIds);
        if (CollectionUtils.isEmpty(scriptFileRefResults)) {
            return;
        }
        List<Long> fileIdList = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(
            Collectors.toList());
        List<FileManageResponse> fileManageResponses = getFileManageResponseByFileIds(fileIdList);
        if (CollectionUtils.isEmpty(fileManageResponses)) {
            return;
        }
        Map<Long, List<ScriptFileRefResult>> scriptFileRefMap = scriptFileRefResults.stream().collect(
            Collectors.groupingBy(ScriptFileRefResult::getScriptDeployId));
        Map<Long, FileManageResponse> fileManageResultMap = fileManageResponses.stream().collect(
            Collectors.toMap(FileManageResponse::getId, a -> a, (k1, k2) -> k1));
        scriptManageDeployResponses.forEach(scriptManageDeployResponse -> {
            List<ScriptFileRefResult> scriptFileRefList = scriptFileRefMap.get(scriptManageDeployResponse.getId());
            if (CollectionUtils.isEmpty(scriptFileRefList)) {
                return;
            }
            List<FileManageResponse> resultFileManageResponses = scriptFileRefList.stream().map(
                scriptFileRefResult -> fileManageResultMap.get(scriptFileRefResult.getFileId())).collect(
                Collectors.toList());
            if (CollectionUtils.isEmpty(resultFileManageResponses)) {
                return;
            }
            scriptManageDeployResponse.setFileManageResponseList(resultFileManageResponses);
        });
    }

    @Override
    public void deleteScriptManage(Long scriptDeployId) {
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(
            scriptDeployId);
        if (scriptManageDeployResult == null) {
            return;
        }
        ResponseResult<List<SceneManageListResp>> sceneManageList = sceneManageApi.getSceneManageList();
        if (sceneManageList == null || !sceneManageList.getSuccess()) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DELETE_VALID_ERROR,
                "查询场景列表失败！" + sceneManageList.getError().getMsg());
        }

        List<ScriptManageDeployResult> existScriptManageDeployResults = scriptManageDAO
            .selectScriptManageDeployByScriptId(scriptManageDeployResult.getScriptId());
        Map<Long, ScriptManageDeployResult> existScriptManageDeployResultMap = existScriptManageDeployResults.stream()
            .collect(Collectors.toMap(ScriptManageDeployResult::getId, o -> o, (k1, k2) -> k1));
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(sceneManageList.getData())) {
            for (SceneManageListResp sceneManageListResp : sceneManageList.getData()) {
                if (!StringUtil.isBlank(sceneManageListResp.getFeatures())) {
                    Map<String, Object> featuresMap = JsonHelper.json2Map(sceneManageListResp.getFeatures(),
                        String.class, Object.class);
                    if (featuresMap.get(SceneManageConstant.FEATURES_SCRIPT_ID) != null) {
                        if (existScriptManageDeployResultMap.get(
                            Long.valueOf(featuresMap.get(SceneManageConstant.FEATURES_SCRIPT_ID).toString())) != null) {
                            sb.append(sceneManageListResp.getSceneName()).append("、");
                        }
                    }
                }
            }
        }
        if (!StringUtil.isBlank(sb.toString())) {
            sb.deleteCharAt(sb.lastIndexOf("、"));
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DELETE_VALID_ERROR,
                "该脚本被以下场景引用，请取消引用后再删除:" + sb.toString());
        }
        scriptManageDAO.deleteScriptManageAndDeploy(scriptManageDeployResult.getScriptId());
        List<Long> existScriptManageDeployIds = existScriptManageDeployResults.stream().map(
            ScriptManageDeployResult::getId).collect(Collectors.toList());
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployIds(
            existScriptManageDeployIds);
        if (CollectionUtils.isNotEmpty(scriptFileRefResults)) {
            List<Long> scriptFileRefIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getId).collect(
                Collectors.toList());
            scriptFileRefDAO.deleteByIds(scriptFileRefIds);
            List<Long> fileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(
                Collectors.toList());
            List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);
            List<String> filePaths = fileManageResults.stream().map(FileManageResult::getUploadPath).collect(
                Collectors.toList());
            fileApi.deleteFile(filePaths);
            fileManageDAO.deleteByIds(fileIds);
        }
        scriptTagRefDAO.deleteByScriptId(scriptManageDeployResult.getScriptId());

    }

    @Override
    public void createScriptTagRef(ScriptTagCreateRefRequest scriptTagCreateRefRequest) {
        if (scriptTagCreateRefRequest == null || scriptTagCreateRefRequest.getScriptDeployId() == null) {
            return;
        }
        if (scriptTagCreateRefRequest.getTagNames().size() > 10) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_TAG_ADD_VALID_ERROR, "每个脚本关联标签数不能超过10");
        }
        List<String> collect = scriptTagCreateRefRequest.getTagNames().stream().filter(o -> o.length() > 10).collect(
            Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_TAG_ADD_VALID_ERROR, "存在脚本名称长度超过10");
        }
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(
            scriptTagCreateRefRequest.getScriptDeployId());
        if (scriptManageDeployResult != null) {
            scriptTagRefDAO.deleteByScriptId(scriptManageDeployResult.getScriptId());
            if (CollectionUtils.isNotEmpty(scriptTagCreateRefRequest.getTagNames())) {
                List<TagManageParam> tagManageParams = scriptTagCreateRefRequest.getTagNames().stream().distinct().map(
                    tagName -> {
                        TagManageParam tagManageParam = new TagManageParam();
                        tagManageParam.setTagName(tagName);
                        //默认为可用状态
                        tagManageParam.setTagStatus(0);
                        //默认为脚本类型
                        tagManageParam.setTagType(0);
                        return tagManageParam;
                    }).collect(Collectors.toList());
                List<Long> tagIds = tagManageDAO.addScriptTags(tagManageParams,0);
                scriptTagRefDAO.addScriptTagRef(tagIds, scriptManageDeployResult.getScriptId());
            }
        }

    }

    @Override
    public List<TagManageResponse> queryScriptTagList() {
        List<TagManageResult> tagManageResults = tagManageDAO.selectAllScript();
        if (CollectionUtils.isNotEmpty(tagManageResults)) {
            return tagManageResults.stream().map(tagManageResult -> {
                TagManageResponse tagManageResponse = new TagManageResponse();
                tagManageResponse.setId(tagManageResult.getId());
                tagManageResponse.setTagName(tagManageResult.getTagName());
                return tagManageResponse;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public ScriptManageDeployDetailResponse getScriptManageDeployDetail(Long scriptDeployId) {
        ScriptManageDeployDetailResponse result = new ScriptManageDeployDetailResponse();
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.selectScriptManageDeployById(
            scriptDeployId);
        if (scriptManageDeployResult == null) {
            return null;
        }
        BeanUtils.copyProperties(scriptManageDeployResult, result);
        setRefName(result, scriptManageDeployResult);
        setFileList(result);
        setFeatures(result, scriptManageDeployResult.getFeature());
        return result;
    }

    /**
     * 批量设置关联名称
     *
     * @param scriptManageDeployResponses
     * @param scriptManageDeployResults
     */
    private void setRefName(List<ScriptManageDeployResponse> scriptManageDeployResponses,
        PagingList<ScriptManageDeployResult> scriptManageDeployResults) {
        if (scriptManageDeployResults == null || CollectionUtils.isEmpty(scriptManageDeployResults.getList())
            || CollectionUtils.isEmpty(scriptManageDeployResponses)) {
            return;
        }
        Map<String, List<ScriptManageDeployResult>> refTypeMap = scriptManageDeployResults.getList()
            .stream()
            .filter(item -> item.getRefType() != null)
            .collect(
                Collectors.groupingBy(ScriptManageDeployResult::getRefType));
        if (refTypeMap == null || refTypeMap.size() <= 0) {
            return;
        }
        Map<Long, ScriptManageDeployResult> longScriptManageDeployResultMap = scriptManageDeployResults.getList()
            .stream().collect(Collectors.toMap(ScriptManageDeployResult::getId, a -> a, (k1, k2) -> k1));
        Map<Long, Scene> businessFlowMap = new HashMap<>();
        Map<Long, BusinessLinkManageTable> businessActivityMap = new HashMap<>();
        for (Map.Entry<String, List<ScriptManageDeployResult>> entry : refTypeMap.entrySet()) {
            //关联了业务流程
            if (ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE.equals(entry.getKey())) {
                List<Long> businessFlowIds = entry.getValue().stream().map(
                    scriptManageDeployResult -> Long.parseLong(scriptManageDeployResult.getRefValue())).collect(
                    Collectors.toList());
                List<Scene> scenes = tSceneMapper.selectBusinessFlowNameByIds(businessFlowIds);
                if (CollectionUtils.isNotEmpty(scenes)) {
                    businessFlowMap = scenes.stream().collect(Collectors.toMap(Scene::getId, a -> a, (k1, k2) -> k1));
                }
            }
            //关联了业务活动
            if (ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE.equals(entry.getKey())) {
                List<Long> businessActivityIds = entry.getValue().stream().map(
                    scriptManageDeployResult -> Long.parseLong(scriptManageDeployResult.getRefValue())).collect(
                    Collectors.toList());
                List<BusinessLinkManageTable> businessLinkManageTables = tBusinessLinkManageTableMapper
                    .selectBussinessLinkByIdList(businessActivityIds);
                if (CollectionUtils.isNotEmpty(businessLinkManageTables)) {
                    businessActivityMap = businessLinkManageTables.stream().collect(
                        Collectors.toMap(BusinessLinkManageTable::getLinkId, a -> a, (k1, k2) -> k1));
                }
            }
        }
        Map<Long, Scene> finalBusinessFlowMap = businessFlowMap;
        Map<Long, BusinessLinkManageTable> finalBusinessActivityMap = businessActivityMap;
        scriptManageDeployResponses.forEach(scriptManageDeployResponse -> {
            ScriptManageDeployResult scriptManageDeployResult = longScriptManageDeployResultMap.get(
                scriptManageDeployResponse.getId());
            if (ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE.equals(scriptManageDeployResponse.getRefType())) {
                Scene scene = finalBusinessFlowMap.get(Long.parseLong(scriptManageDeployResult.getRefValue()));
                if (scene != null) {
                    scriptManageDeployResponse.setRefName(scene.getSceneName());
                }
            }
            if (ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE.equals(scriptManageDeployResponse.getRefType())) {
                BusinessLinkManageTable businessLinkManageTable = finalBusinessActivityMap.get(
                    Long.parseLong(scriptManageDeployResult.getRefValue()));
                if (businessLinkManageTable != null) {
                    scriptManageDeployResponse.setRefName(businessLinkManageTable.getLinkName());
                }

            }
        });
    }

    private void setFileList(ScriptManageDeployDetailResponse result) {
        if (result == null || result.getId() == null) {
            return;
        }
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployId(result.getId());
        if (CollectionUtils.isEmpty(scriptFileRefResults)) {
            log.info("不存在关联的文件id");
            return;
        }
        List<Long> fileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(
            Collectors.toList());
        List<FileManageResponse> totalFileList = getFileManageResponseByFileIds(fileIds);
        if (CollectionUtils.isNotEmpty(totalFileList)) {
            List<FileManageResponse> fileManageResponseList = totalFileList.stream().filter(
                f -> f.getFileType().equals(FileTypeEnum.SCRIPT.getCode()) || f.getFileType()
                    .equals(FileTypeEnum.DATA.getCode())).collect(
                Collectors.toList());
            List<FileManageResponse> attachmentManageResponseList = totalFileList.stream().filter(
                f -> f.getFileType().equals(FileTypeEnum.ATTACHMENT.getCode())).collect(
                Collectors.toList());
            result.setFileManageResponseList(fileManageResponseList);
            result.setAttachmentManageResponseList(attachmentManageResponseList);
        }
    }

    private void setFeatures(ScriptManageDeployDetailResponse result, String feature) {
        if (StringUtils.isNotBlank(feature)) {
            Map<String, Object> featureMap = JSON.parseObject(feature, Map.class);
            if (featureMap.containsKey(FeaturesConstants.PLUGIN_CONFIG)) {
                String pluginConfigContent = JSON.toJSONString(featureMap.get(FeaturesConstants.PLUGIN_CONFIG));
                List<PluginConfigDetailResponse> pluginConfigDetailResponseList
                    = JSON.parseArray(pluginConfigContent, PluginConfigDetailResponse.class);
                result.setPluginConfigDetailResponseList(pluginConfigDetailResponseList);
            }
        }
    }

    private List<FileManageResponse> getFileManageResponseByFileIds(List<Long> fileIds) {
        List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);
        List<FileManageResponse> fileManageResponses = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fileManageResults)) {
            for (FileManageResult fileManageResult : fileManageResults) {
                FileManageResponse fileManageResponse = new FileManageResponse();
                BeanUtils.copyProperties(fileManageResult, fileManageResponse);
                if (StringUtils.isNotEmpty(fileManageResult.getFileExtend())) {
                    Map<String, Object> stringObjectMap = JsonHelper.json2Map(fileManageResult.getFileExtend(),
                        String.class, Object.class);
                    if (stringObjectMap != null && stringObjectMap.get("dataCount") != null && !StringUtil.isBlank(
                        stringObjectMap.get("dataCount").toString())) {
                        fileManageResponse.setDataCount(Long.valueOf(stringObjectMap.get("dataCount").toString()));
                    }
                    if (stringObjectMap != null && stringObjectMap.get("isSplit") != null && !StringUtil.isBlank(
                        stringObjectMap.get("isSplit").toString())) {
                        fileManageResponse.setIsSplit(Integer.valueOf(stringObjectMap.get("isSplit").toString()));
                    }
                }
                String uploadUrl = fileManageResult.getUploadPath();
                fileManageResponse.setUploadPath(uploadUrl);
                fileManageResponses.add(fileManageResponse);
            }
            return fileManageResponses;
        }
        return null;
    }

    /**
     * 设置关联的业务活动或业务流程
     *
     * @param result
     * @param scriptManageDeployResult
     */
    private void setRefName(ScriptManageDeployDetailResponse result,
        ScriptManageDeployResult scriptManageDeployResult) {
        if (StringUtils.isEmpty(scriptManageDeployResult.getRefValue())) {
            return;
        }
        //关联了系统流程
        if (ScriptManageConstant.BUSINESS_PROCESS_REF_TYPE.equals(scriptManageDeployResult.getRefType())) {
            long businessId = Long.parseLong(scriptManageDeployResult.getRefValue());
            List<Scene> scenes = tSceneMapper.selectBusinessFlowNameByIds(Collections.singletonList(businessId));
            if (CollectionUtils.isNotEmpty(scenes)) {
                result.setRefName(scenes.get(0).getSceneName());
            }
        }
        //关联了业务活动
        if (ScriptManageConstant.BUSINESS_ACTIVITY_REF_TYPE.equals(scriptManageDeployResult.getRefType())) {
            long businessId = Long.parseLong(scriptManageDeployResult.getRefValue());
            List<BusinessLinkManageTable> businessLinkManageTables = tBusinessLinkManageTableMapper
                .selectBussinessLinkByIdList(Collections.singletonList(businessId));
            if (CollectionUtils.isNotEmpty(businessLinkManageTables)) {
                result.setRefName(businessLinkManageTables.get(0).getLinkName());
            }
        }
    }

    /**
     * 创建脚本参数校验
     *
     * @param scriptManageDeployCreateRequest
     */
    private void checkCreateScriptManageParam(ScriptManageDeployCreateRequest scriptManageDeployCreateRequest) {
        if (scriptManageDeployCreateRequest == null) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "入参为空！");
        }
        if (StringUtil.isBlank(scriptManageDeployCreateRequest.getName())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "脚本名称为空！");
        }
        if (StringUtil.isBlank(scriptManageDeployCreateRequest.getRefType())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "脚本关联类型为空！");
        }
        if (StringUtil.isBlank(scriptManageDeployCreateRequest.getRefValue())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "脚本关联值为空！");
        }
        if (scriptManageDeployCreateRequest.getType() == null) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "脚本类型为空！");
        }
        if (CollectionUtils.isEmpty(scriptManageDeployCreateRequest.getFileManageCreateRequests())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "文件列表为空！");
        }
        boolean existJmx = false;
        for (FileManageCreateRequest fileManageCreateRequest : scriptManageDeployCreateRequest
            .getFileManageCreateRequests()) {
            if (StringUtil.isBlank(fileManageCreateRequest.getFileName())) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "文件列表中存在文件名为空！");
            }
            if (fileManageCreateRequest.getFileName().length() > 64) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "文件列表中存在文件名长度大于64！");
            }
            if (fileManageCreateRequest.getFileType() == null) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR,
                    "文件列表中存在文件类型为空！fileName=" + fileManageCreateRequest.getFileName());
            }
            if (fileManageCreateRequest.getFileName().contains(" ")) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "文件名包含空格！");
            }
            if (fileManageCreateRequest.getFileType() == 0 && fileManageCreateRequest.getIsDeleted() == 0) {
                existJmx = true;
            }
            fileManageCreateRequest.setFileName(FileUtil.replaceFileName(fileManageCreateRequest.getFileName()));
        }
        if (!existJmx) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_PARAM_VALID_ERROR, "文件列表中不存在脚本文件！");
        }
    }

    /**
     * 更新入参判断
     *
     * @param scriptManageDeployUpdateRequest 更新入参
     */
    private void checkUpdateScriptManageParam(ScriptManageDeployUpdateRequest scriptManageDeployUpdateRequest) {
        ScriptManageExceptionUtil.isUpdateValidError(scriptManageDeployUpdateRequest == null, "入参为空!");
        ScriptManageExceptionUtil.isUpdateValidError(scriptManageDeployUpdateRequest.getId() == null, "脚本id为空!");
        ScriptManageExceptionUtil.isUpdateValidError(StringUtil.isBlank(scriptManageDeployUpdateRequest.getName()), "脚本名称为空!");
        ScriptManageExceptionUtil.isUpdateValidError(StringUtil.isBlank(scriptManageDeployUpdateRequest.getRefType()), "脚本关联类型为空!");
        ScriptManageExceptionUtil.isUpdateValidError(StringUtil.isBlank(scriptManageDeployUpdateRequest.getRefValue()), "脚本关联值为空!");
        ScriptManageExceptionUtil.isUpdateValidError(scriptManageDeployUpdateRequest.getType() == null, "脚本类型为空!");
        ScriptManageExceptionUtil.isUpdateValidError(CollectionUtils.isEmpty(scriptManageDeployUpdateRequest.getFileManageUpdateRequests()), "文件列表为空!");

        boolean existJmx = false;
        for (FileManageUpdateRequest fileManageUpdateRequest : scriptManageDeployUpdateRequest
            .getFileManageUpdateRequests()) {

            ScriptManageExceptionUtil.isUpdateValidError(StringUtil.isBlank(fileManageUpdateRequest.getFileName()), "文件列表中存在文件名为空!");
            ScriptManageExceptionUtil.isUpdateValidError(fileManageUpdateRequest.getFileName().length() > 64, "文件列表中存在文件名长度大于64!");
            ScriptManageExceptionUtil.isUpdateValidError(fileManageUpdateRequest.getFileName().contains(" "), "文件名包含空格!");
            ScriptManageExceptionUtil.isUpdateValidError(fileManageUpdateRequest.getFileType() == null, "文件列表中存在文件类型为空！fileName=" + fileManageUpdateRequest.getFileName());

            if (fileManageUpdateRequest.getFileType() == 0 && fileManageUpdateRequest.getIsDeleted() == 0) {
                existJmx = true;
            }
            fileManageUpdateRequest.setFileName(FileUtil.replaceFileName(fileManageUpdateRequest.getFileName()));
        }

        ScriptManageExceptionUtil.isUpdateValidError(!existJmx, "文件列表中不存在脚本文件!");
    }

    /**
     * 脚本路径前缀 目录 + 脚本id + 版本
     *
     * @param scriptManageDeployResult 脚本实例
     * @return 路径前缀
     */
    private String getTargetScriptPath(ScriptManageDeployResult scriptManageDeployResult) {
        return scriptFilePath + "/"+ scriptManageDeployResult.getScriptId() + "/"
            + scriptManageDeployResult.getScriptVersion() + "/";
    }

}
