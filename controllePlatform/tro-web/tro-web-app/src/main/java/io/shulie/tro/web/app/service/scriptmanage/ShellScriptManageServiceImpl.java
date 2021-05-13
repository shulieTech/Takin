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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.input.scriptmanage.ShellExecuteInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManageCreateInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManagePageQueryInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManageUpdateInput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ScriptExecuteOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageContentOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageDetailOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageExecuteOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageOutput;
import io.shulie.tro.web.app.output.tagmanage.TagManageOutput;
import io.shulie.tro.web.app.utils.LinuxHelper;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.common.constant.ScriptManageConstant;
import io.shulie.tro.web.data.dao.filemanage.FileManageDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptFileRefDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptManageDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptTagRefDAO;
import io.shulie.tro.web.data.dao.tagmanage.TagManageDAO;
import io.shulie.tro.web.data.param.filemanage.FileManageCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptExecuteResultCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployCreateParam;
import io.shulie.tro.web.data.param.scriptmanage.ScriptManageDeployPageQueryParam;
import io.shulie.tro.web.data.param.scriptmanage.shell.ShellExecuteParam;
import io.shulie.tro.web.data.result.filemanage.FileManageResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptExecuteResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptFileRefResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageDeployResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageResult;
import io.shulie.tro.web.data.result.scriptmanage.ScriptTagRefResult;
import io.shulie.tro.web.data.result.tagmanage.TagManageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.scriptmanage
 * @description:
 * @date 2020/12/8 4:38 下午
 */
@Service
@Slf4j
public class ShellScriptManageServiceImpl implements ShellScriptManageService {

    private static ThreadFactory nameThreadFactory =
        new ThreadFactoryBuilder().setNameFormat("linux-run-thread-%d").build();
    private static ThreadPoolExecutor poolExecutor =
        new ThreadPoolExecutor(20, 20, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    @Value("${web.file.upload.script.path:/opt/tro/script}")
    private String fileScriptPath;
    @Value("${customer.id:0}")
    private String customerId;
    @Autowired
    private ScriptManageDAO scriptManageDAO;
    @Autowired
    private ScriptFileRefDAO scriptFileRefDAO;
    @Autowired
    private FileManageDAO fileManageDAO;
    @Autowired
    private ScriptTagRefDAO scriptTagRefDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private TagManageDAO tagManageDAO;
    @Autowired
    private RedisClientUtils redisClientUtils;

    @Override
    public Long createScriptManage(ShellScriptManageCreateInput input) {
        // todo 目前脚本内容校验不做
        // 脚本类型
        input.setType(1);
        // 脚本文件类型
        input.setFileType(4);
        // 脚本内容转文件
        input.setName(input.getName().trim());
        List<ScriptManageResult> scriptManageResults = scriptManageDAO.selectScriptManageByName(input.getName());
        if (CollectionUtils.isNotEmpty(scriptManageResults)) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_CREATE_VALID_ERROR, "脚本名称重复！");
        }

        ScriptManageDeployCreateParam param = new ScriptManageDeployCreateParam();
        BeanUtils.copyProperties(input, param);
        param.setStatus(0);
        param.setScriptVersion(1);
        User user = RestContext.getUser();
        param.setCreateUserId(user.getId());
        param.setCreateUserName(user.getName());
        param.setCustomerId(user.getCustomerId());
        ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.createScriptManageDeploy(param);
        // web 创建file存web
        FileManageCreateParam fileManageCreateParam = getFileManageCreateParams(input, scriptManageDeployResult);
        FileManagerHelper.createFileByPathAndString(fileManageCreateParam.getUploadPath(), input.getContent());
        // web 落库
        List<FileManageCreateParam> params = Lists.newArrayList();
        params.add(fileManageCreateParam);
        List<Long> fileIds = fileManageDAO.createFileManageList(params);
        scriptFileRefDAO.createScriptFileRefs(fileIds, scriptManageDeployResult.getId());
        return scriptManageDeployResult.getId();
    }

    private FileManageCreateParam getFileManageCreateParams(ShellScriptManageCreateInput input,
        ScriptManageDeployResult result) {
        FileManageCreateParam fileManageCreateParam = new FileManageCreateParam();
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + ".sh";
        fileManageCreateParam.setFileName(fileName);
        try {
            fileManageCreateParam.setFileSize(input.getContent().getBytes("utf-8").length / 1024 / 1024 + "M");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        fileManageCreateParam.setFileType(input.getFileType());
        fileManageCreateParam.setCustomerId(Long.parseLong(customerId));
        fileManageCreateParam.setUploadPath(fileScriptPath + "/shell/" + result.getScriptId() + "/"
            + result.getScriptVersion() + "/" + fileName);
        fileManageCreateParam.setUploadTime(new Date());
        return fileManageCreateParam;
    }

    @Override
    public String updateScriptManage(ShellScriptManageUpdateInput input) {
        // 正在运行中，脚本不能更新
        if (!isStop(input.getScriptDeployId())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_UPDATE_VALID_ERROR, "脚本正在运行中！");
        }
        ScriptManageDeployResult oldDeployResult = scriptManageDAO.selectScriptManageDeployById(input.getScriptDeployId());
        // 这里是否更新版本 根据描述 和 脚本内容版本
        // 获取老版本数据input
        Boolean updateFlag = isUpdate(input, oldDeployResult);
        if (updateFlag) {
            // 版本升级
            // 脚本类型
            input.setType(1);
            // 脚本文件类型
            input.setFileType(4);
            ScriptManageDeployCreateParam createParam = new ScriptManageDeployCreateParam();
            BeanUtils.copyProperties(input, createParam);
            createParam.setStatus(0);
            // 版本应该获取最新的，然后+1
            List<ScriptManageDeployResult> history = scriptManageDAO.selectScriptManageDeployByScriptId(oldDeployResult.getScriptId());
            // 排序
            Integer scriptVersion = history.stream().max(Comparator.comparing(ScriptManageDeployResult::getScriptVersion)).get().getScriptVersion();
            createParam.setScriptVersion(scriptVersion + 1);
            //更新脚本状态，将脚本实例更新为历史状态
            scriptManageDAO.updateScriptVersion(oldDeployResult.getScriptId(), scriptVersion + 1);
            //创建新的脚本实例
            User user = RestContext.getUser();
            createParam.setCreateUserId(user.getId());
            createParam.setCreateUserName(user.getName());
            // 需要赋值进去
            createParam.setScriptId(oldDeployResult.getScriptId());
            ScriptManageDeployResult scriptManageDeployResult = scriptManageDAO.createScriptManageDeploy(createParam);
            // 迁移数据
            ShellScriptManageCreateInput createInput = new ShellScriptManageCreateInput();
            BeanUtils.copyProperties(input, createInput);
            // web 保存脚本
            FileManageCreateParam fileManageCreateParam = getFileManageCreateParams(createInput,
                scriptManageDeployResult);
            FileManagerHelper.createFileByPathAndString(fileManageCreateParam.getUploadPath(), input.getContent());
            // web 落库
            List<FileManageCreateParam> params = Lists.newArrayList();
            params.add(fileManageCreateParam);
            List<Long> fileIds = fileManageDAO.createFileManageList(params);
            scriptFileRefDAO.createScriptFileRefs(fileIds, scriptManageDeployResult.getId());
            return "版本更新成功";
        } else {
            // 切换版本
            scriptManageDAO.switchScriptVersion(oldDeployResult.getScriptId(), input.getScriptVersion());
            return "版本切换成功";
        }
    }

    private Boolean isUpdate(ShellScriptManageUpdateInput input, ScriptManageDeployResult oldDeployResult) {
        Boolean updateFlag = false;
        // 对比应该是将得到
        if (!StringUtils.equals(input.getDescription(), oldDeployResult.getDescription())) {
            updateFlag = true;
        }
        // 获取文本内容
        String content = getContent(oldDeployResult);
        if (!StringUtils.equals(input.getContent(), content)) {
            updateFlag = true;
        }
        return updateFlag;
    }

    private String getContent(ScriptManageDeployResult deployResult) {
        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployId(
            deployResult.getId());
        if (CollectionUtils.isEmpty(scriptFileRefResults)) {
            log.info("没有找相关文件");
            return "";
        }
        List<Long> fileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(
            Collectors.toList());
        List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);
        if (CollectionUtils.isNotEmpty(fileManageResults)) {
            // 一定是1:1
            List<String> paths = fileManageResults.stream().map(FileManageResult::getUploadPath).collect(
                Collectors.toList());
            if (CollectionUtils.isEmpty(paths)) {
                return "";
            }
            FileManageResult fileManageResult = fileManageResults.get(0);
            try {
                if (new File(fileManageResult.getUploadPath()).exists()) {
                    return FileManagerHelper.readFileToString(new File(fileManageResult.getUploadPath()), "UTF-8");
                }
            } catch (IOException e) {
                throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_ERROR, "脚本内容获取错误");
            }
        }
        return "";
    }

    @Override
    public void deleteScriptManage(Long scriptId) {
        // 正在运行中，脚本不能删除
        ScriptManageResult scriptManageResult = scriptManageDAO.selectScriptManageById(scriptId);
        ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployByVersion(scriptManageResult.getId(),scriptManageResult.getScriptVersion());
        if (!isStop(deployResult.getId())) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_DELETE_VALID_ERROR, "脚本正在运行中！");
        }
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCRIPT_MANAGE_DEPLOY_NAME, deployResult.getName());

        List<ScriptManageDeployResult> existScriptManageDeployResults = scriptManageDAO
            .selectScriptManageDeployByScriptId(scriptId);
        scriptManageDAO.deleteScriptManageAndDeploy(scriptId);
        List<Long> existScriptManageDeployIds = existScriptManageDeployResults.stream().map(
            ScriptManageDeployResult::getId).collect(
            Collectors.toList());
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
            // 删除文件
            FileManagerHelper.deleteFiles(filePaths);
            fileManageDAO.deleteByIds(fileIds);
        }
        scriptTagRefDAO.deleteByScriptId(scriptId);

    }

    @Override
    public ShellScriptManageDetailOutput getScriptManageDetail(Long scriptId) {
        ShellScriptManageDetailOutput output = new ShellScriptManageDetailOutput();
        ScriptManageResult scriptManageResult = scriptManageDAO.selectScriptManageById(scriptId);
        if (scriptManageResult == null) {
            return null;
        }
        BeanUtils.copyProperties(scriptManageResult, output);
        // 根据版本获取实例id
        ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployByVersion(scriptId,
            scriptManageResult.getScriptVersion());
        if (deployResult == null) {
            return output;
        }
        output.setDescription(deployResult.getDescription());
        output.setScriptDeployId(deployResult.getId());
        output.setType(deployResult.getType());
        output.setContent(getContent(deployResult));
        List<ScriptManageDeployResult> list = scriptManageDAO.selectScriptManageDeployByScriptId(scriptId);
        List<Map<String,Object>> versions = list.stream().map(result -> {
            Map<String,Object> map = Maps.newHashMap();
            map.put("label","版本" + result.getScriptVersion());
            map.put("value",result.getScriptVersion());
            return map;
        }).collect(
            Collectors.toList());
        output.setVersions(versions);
        return output;

    }

    @Override
    public PagingList<ShellScriptManageOutput> pageQueryScriptManage(ShellScriptManagePageQueryInput input) {
        ScriptManageDeployPageQueryParam queryParam = new ScriptManageDeployPageQueryParam();
        if (input != null) {
            BeanUtils.copyProperties(input, queryParam);
            if (CollectionUtils.isNotEmpty(input.getTagIds())) {
                List<ScriptTagRefResult> scriptTagRefResults = scriptTagRefDAO.selectScriptTagRefByTagIds(
                    input.getTagIds());
                if (CollectionUtils.isEmpty(scriptTagRefResults)) {
                    //标签中没有查到关联数据，直接返回空
                    return PagingList.empty();
                }
                Map<Long, List<ScriptTagRefResult>> scriptTagRefMap = scriptTagRefResults.stream().collect(
                    Collectors.groupingBy(ScriptTagRefResult::getScriptId));
                List<Long> scriptIds = new ArrayList<>();
                for (Map.Entry<Long, List<ScriptTagRefResult>> entry : scriptTagRefMap.entrySet()) {
                    if (entry.getValue().size() == input.getTagIds().size()) {
                        scriptIds.add(entry.getKey());
                    }
                }
                if (CollectionUtils.isEmpty(scriptIds)) {
                    return PagingList.empty();
                }
                queryParam.setScriptIds(scriptIds);
            }
        }
        queryParam.setCurrent(input.getCurrent());
        queryParam.setPageSize(input.getPageSize());
        // 脚本类型
        queryParam.setScriptType(1);
        List<Long> userIdList = RestContext.getQueryAllowUserIdList();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            queryParam.setUserIdList(userIdList);
        }
        PagingList<ScriptManageDeployResult> scriptManageDeployResults = scriptManageDAO
            .pageQueryRecentScriptManageDeploy(queryParam);
        if (scriptManageDeployResults.isEmpty()) {
            return PagingList.empty();
        }
        //用户ids
        List<Long> userIds = scriptManageDeployResults.getList().stream().filter(data -> null != data.getUserId()).map(
            ScriptManageDeployResult::getUserId).collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
        List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
        List<ShellScriptManageOutput> outputs = scriptManageDeployResults.getList().stream().map(
            scriptManageDeployResult -> {
                ShellScriptManageOutput shellScriptManageOutput = new ShellScriptManageOutput();
                BeanUtils.copyProperties(scriptManageDeployResult, shellScriptManageOutput);
                shellScriptManageOutput.setScripDeployId(scriptManageDeployResult.getId());
                if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                    shellScriptManageOutput.setCanEdit(
                        allowUpdateUserIdList.contains(scriptManageDeployResult.getUserId()));
                }
                if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                    shellScriptManageOutput.setCanRemove(
                        allowDeleteUserIdList.contains(scriptManageDeployResult.getUserId()));
                }

                //负责人id
                shellScriptManageOutput.setManagerId(scriptManageDeployResult.getUserId());
                //负责人名称
                String userName = Optional.ofNullable(userMap.get(scriptManageDeployResult.getUserId()))
                    .map(u -> u.getName())
                    .orElse("");
                shellScriptManageOutput.setManagerName(userName);
                return shellScriptManageOutput;
            }).collect(Collectors.toList());
        setTagList(outputs);
        setExecuteState(outputs);
        return PagingList.of(outputs, scriptManageDeployResults.getTotal());
    }

    private void setExecuteState(List<ShellScriptManageOutput> outputs) {
        if (outputs == null || CollectionUtils.isEmpty(outputs)) {
            return;
        }
        outputs.forEach(this::setState);
    }

    private void setState(ShellScriptManageOutput output) {
        //获取redis
        if (!isStop(output.getScripDeployId())) {
            // 正在执行
            output.setExecute(true);
        }
    }

    private void setTagList(List<ShellScriptManageOutput> outputs) {
        if (outputs == null || CollectionUtils.isEmpty(outputs)) {
            return;
        }
        List<Long> scriptIds = outputs.stream().map(ShellScriptManageOutput::getScriptId)
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
        List<TagManageOutput> tagManageOutputs = tagManageResults.stream().map(tagManageResult -> {
            TagManageOutput tagManageOutput = new TagManageOutput();
            tagManageOutput.setId(tagManageResult.getId());
            tagManageOutput.setTagName(tagManageResult.getTagName());
            return tagManageOutput;
        }).collect(Collectors.toList());
        Map<Long, TagManageOutput> tagManageResponseMap = tagManageOutputs.stream().collect(
            Collectors.toMap(TagManageOutput::getId, a -> a, (k1, k2) -> k1));
        outputs.forEach(output -> {
            List<ScriptTagRefResult> scriptTagRefList = scriptTagRefMap.get(output.getScriptId());
            if (CollectionUtils.isEmpty(scriptTagRefList)) {
                return;
            }
            List<TagManageOutput> resultTagManageOutputs = scriptTagRefList.stream().map(
                scriptTagRefResult -> tagManageResponseMap.get(scriptTagRefResult.getTagId())).collect(
                Collectors.toList());
            if (CollectionUtils.isEmpty(resultTagManageOutputs)) {
                return;
            }
            output.setTagManageOutputs(resultTagManageOutputs);
        });
    }

    @Override
    public ShellScriptManageExecuteOutput execute(Long scriptManageDeployId) {
        if (!isStop(scriptManageDeployId)) {
            // 正在执行中
            log.info("执行结果：{}", JsonHelper.bean2Json(
                redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY, String.valueOf(scriptManageDeployId))));
            return (ShellScriptManageExecuteOutput)redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY,
                String.valueOf(scriptManageDeployId));
        }
        ShellScriptManageExecuteOutput output = new ShellScriptManageExecuteOutput();

        List<ScriptFileRefResult> scriptFileRefResults = scriptFileRefDAO.selectFileIdsByScriptDeployId(scriptManageDeployId);
        List<String> data = Lists.newArrayList();

        if (CollectionUtils.isEmpty(scriptFileRefResults)) {
            log.info("没有找相关文件");
            output.setSuccess(false);
            data.add("没有找到相关脚本文件依赖");
            output.setMessage(data);
            return output;
        }
        List<Long> fileIds = scriptFileRefResults.stream().map(ScriptFileRefResult::getFileId).collect(Collectors.toList());
        List<FileManageResult> fileManageResults = fileManageDAO.selectFileManageByIds(fileIds);

        if (CollectionUtils.isNotEmpty(fileManageResults)) {
            FileManageResult fileManageResult = fileManageResults.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("sh ");
            sb.append(fileManageResult.getUploadPath());
            poolExecutor.execute(() -> {
                Map<String, Object> map = Maps.newHashMap();
                // 开始记录
                ShellScriptManageExecuteOutput executeOutput = new ShellScriptManageExecuteOutput();
                data.add(sb.toString() + ":开始执行");
                executeOutput.setIsStop(false);
                executeOutput.setMessage(data);
                map.put(String.valueOf(scriptManageDeployId), executeOutput);
                redisClientUtils.hmset(ScriptManageConstant.SHELL_EXECUTE_KEY, map);
                // 执行
                AtomicReference<Process> shellProcess = new AtomicReference<>();
                int state = LinuxHelper.runShell(sb.toString(), 60L,
                    process -> shellProcess.set(process),
                    message -> {
                        log.info("执行返回结果:{}", message);
                        ShellScriptManageExecuteOutput temp = (ShellScriptManageExecuteOutput)redisClientUtils.hmget
                            (ScriptManageConstant.SHELL_EXECUTE_KEY, String.valueOf(scriptManageDeployId));
                        temp.getMessage().add(message);
                        map.put(String.valueOf(scriptManageDeployId), temp);
                        redisClientUtils.hmset(ScriptManageConstant.SHELL_EXECUTE_KEY, map);
                    }
                );
                ShellScriptManageExecuteOutput temp = (ShellScriptManageExecuteOutput)redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY,
                    String.valueOf(scriptManageDeployId));
                // 执行完先存下redis;
                temp.setIsStop(true);
                temp.setSuccess(state == 0 ? true : false);
                map.put(String.valueOf(scriptManageDeployId), temp);
                // 存三天
                redisClientUtils.hmset(ScriptManageConstant.SHELL_EXECUTE_KEY, map,60*60*24*3);
                //进行落库
                ScriptExecuteResultCreateParam param = new ScriptExecuteResultCreateParam();
                // 获取版本
                ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployById(scriptManageDeployId);
                param.setScriptVersion(deployResult.getScriptVersion());
                param.setScriptId(deployResult.getScriptId());
                param.setSuccess(state == 0 ? true : false);
                param.setExecutor(RestContext.getUser().getName());
                param.setGmtCreate(new Date());
                param.setResult(JsonHelper.bean2Json(temp.getMessage()));
                param.setScripDeployId(scriptManageDeployId);
                scriptManageDAO.createScriptExecuteResult(param);
            });

            ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployById(scriptManageDeployId);
            OperationLogContextHolder.addVars(BizOpConstants.Vars.SCRIPT_MANAGE_DEPLOY_NAME, deployResult.getName());
            return output;
        }else {
            output.setSuccess(false);
            data.add("没有找到执行文件");
            output.setMessage(data);
        }
        return output;
    }

    private Boolean isStop(Long scriptManageDeployId) {
        if( redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY, String.valueOf(scriptManageDeployId)) != null) {
            ShellScriptManageExecuteOutput output = (ShellScriptManageExecuteOutput)redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY,
                String.valueOf(scriptManageDeployId));
            return  output.getIsStop();
        }
        return true;
    }

    @Override
    public ShellScriptManageContentOutput getShellScriptManageContent(Long scriptId, Integer version) {
        ShellScriptManageContentOutput output = new ShellScriptManageContentOutput();
        ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployByVersion(scriptId, version);
        if (deployResult == null) {
            return null;
        }
        output.setScriptManageDeployId(deployResult.getId());
        output.setScriptVersion(deployResult.getScriptVersion());
        output.setContent(getContent(deployResult));
        output.setDescription(deployResult.getDescription());
        return output;
    }

    @Override
    public PagingList<ScriptExecuteOutput> getExecuteResult(ShellExecuteInput input) {

        if("0".equals(input.getType())) {
            ShellScriptManageExecuteOutput output =  (ShellScriptManageExecuteOutput)redisClientUtils.hmget(ScriptManageConstant.SHELL_EXECUTE_KEY,
                String.valueOf(input.getScriptDeployId()));
            List<ScriptExecuteOutput> outputs = Lists.newArrayList();
            if(output != null) {
                ScriptExecuteOutput executeOutput = new ScriptExecuteOutput();
                executeOutput.setSuccess(output.getSuccess());
                executeOutput.setIsStop(output.getIsStop());
                executeOutput.setResult(output.getMessage() != null && output.getMessage().size() > 0 ?output.getMessage().get(output.getMessage().size() -1):"");
                outputs.add(executeOutput);
            }
            return PagingList.of(outputs,outputs.size());

        }else {
            ScriptManageDeployResult deployResult = scriptManageDAO.selectScriptManageDeployById(input.getScriptDeployId());
            ShellExecuteParam param = new ShellExecuteParam();
            BeanUtils.copyProperties(input,param);
            param.setScriptId(deployResult.getScriptId());
            PagingList<ScriptExecuteResult> pagingList = scriptManageDAO.getExecuteResult(param);
            List<ScriptExecuteResult> results = pagingList.getList();
            List<ScriptExecuteOutput> outputs = results.stream().map(result -> {
                ScriptExecuteOutput output = new ScriptExecuteOutput();
                BeanUtils.copyProperties(result,output);
                return output;
            }).collect(Collectors.toList());
            return PagingList.of(outputs,pagingList.getTotal());
        }
    }
}
