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

package io.shulie.tro.cloud.biz.service.cloudServer.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.entity.dao.scenemanage.TSceneManageMapper;
import com.pamirs.tro.entity.dao.scenemanage.TSceneScriptRefMapper;
import com.pamirs.tro.entity.domain.entity.scenemanage.SceneManage;
import com.pamirs.tro.entity.domain.entity.scenemanage.SceneScriptRef;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.SceneScriptRefQueryParam;
import com.pamirs.tro.entity.domain.vo.cloudserver.BigFileUploadVO;
import io.shulie.tro.cloud.biz.service.cloudServer.BigFileService;
import io.shulie.tro.cloud.biz.service.user.TroCloudUserService;
import io.shulie.tro.cloud.common.utils.LicenceUtils;
import io.shulie.tro.cloud.common.exception.TROModuleException;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.LinuxUtil;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-05-12 14:50
 * @Description:
 */
@Slf4j
@Service
public class BigFileServiceImpl implements BigFileService {

    public static final String FILE_LINE_NUM_SUFFIX = "-NUM";
    private static final String false_code = "0";
    private static final String SCENE_REF_DATA_COUNT = "dataCount";
    @Value("${pradar.upload.client.dir}")
    private String uploadClientPath;
    @Resource
    private TSceneScriptRefMapper TSceneScriptRefMapper;
    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private TroCloudUserService userService;

    @Resource
    private TSceneManageMapper TSceneManageMapper;

    @Autowired
    private FileWriteService fileWriteService;

    @Override
    public ResponseResult upload(String license, BigFileUploadVO dto) {
        if (StringUtils.isBlank(license)) {
            return ResponseResult.fail("license 不能为空", "");
        }
        try {
            if (dto != null && dto.getFileName() != null && dto.getSceneId() != null) {
                Map<String, Object> map = LicenceUtils.analysisLicense(license);
                if (map == null) {
                    return ResponseResult.fail("license 不能为空", "");
                }
                if (map != null) {
                    String userKey = (String)map.get("key");
                    User user = userService.queryUserByKey(userKey);
                    SceneManage sceneManage = TSceneManageMapper.selectByPrimaryKey(dto.getSceneId());
                    if (user == null || sceneManage == null || !sceneManage.getCustomId().equals(user.getId())) {
                        return ResponseResult.fail("user | license 对应的用户为空| scene  | 场景对应的用户和license用户不统一", "");
                    }
                }
                //生成唯一键 ： 文件总行数放入redis ；
                generateBigFileLineNumKey(dto);
                writeByteToDist(dto);
                SceneScriptRefQueryParam queryParam = new SceneScriptRefQueryParam();
                queryParam.setFileName(dto.getFileName());
                queryParam.setSceneId(dto.getSceneId());
                SceneScriptRef dbData = TSceneScriptRefMapper.selectByExample(queryParam);
                if (dbData != null) {
                    updateSceneScriptRef(dbData, dto);
                } else {
                    addSceneScriptRef(dto);
                }
            }
        } catch (Exception e) {
            log.error("发送数据失败", e);
            return ResponseResult.fail(false_code, "发送数据失败 ;" + e.getMessage(), "");
        }
        return ResponseResult.success();
    }

    public void generateBigFileLineNumKey(BigFileUploadVO dto) {
        //生成唯一键 ： 文件总行数放入redis ；
        StringBuilder builder = new StringBuilder();
        builder.append(dto.getSceneId());
        builder.append("-");
        builder.append(dto.getFileName());
        builder.append(FILE_LINE_NUM_SUFFIX);
        dto.setFileLineNumKey(builder.toString());
    }

    /**
     * 更新场景文件
     *
     * @param dto
     */
    public void updateSceneScriptRef(SceneScriptRef dbData, BigFileUploadVO dto) {
        String fileExtend = dbData.getFileExtend();
        Map<String, Object> extend = null;
        if (StringUtils.isBlank(fileExtend)) {
            extend = new HashMap<>();
        } else {
            extend = JSON.parseObject(fileExtend, Map.class);
        }
        SceneScriptRef updateParam = new SceneScriptRef();
        String lineNum = redisClientUtils.getString(dto.getFileLineNumKey());
        if (lineNum != null) {
            extend.put(SCENE_REF_DATA_COUNT, Integer.valueOf(lineNum));
        }
        updateParam.setFileExtend(JSON.toJSONString(extend));
        updateParam.setId(dbData.getId());
        StringBuilder fileLengthBuilder = new StringBuilder();
        fileLengthBuilder.append(dto.getSceneId());
        fileLengthBuilder.append("-");
        fileLengthBuilder.append(dto.getFileName());
        long fileSize = 0;
        Object object = redisClientUtils.getObject(fileLengthBuilder.toString());
        if (object != null) {
            fileSize = Long.valueOf(object.toString());
        }
        updateParam.setFileSize(LinuxUtil.getPrintSize((fileSize)));
        TSceneScriptRefMapper.updateByPrimaryKeySelective(updateParam);
    }

    /**
     * 添加，同步添加
     */
    public synchronized void addSceneScriptRef(BigFileUploadVO dto) {
        SceneScriptRefQueryParam queryParam = new SceneScriptRefQueryParam();
        queryParam.setFileName(dto.getFileName());
        queryParam.setSceneId(dto.getSceneId());
        SceneScriptRef dbData = TSceneScriptRefMapper.selectByExample(queryParam);
        if (dbData != null) {
            return;
        }
        SceneScriptRef insertParam = new SceneScriptRef();
        insertParam.setFileName(dto.getFileName());
        insertParam.setSceneId(dto.getSceneId());
        insertParam.setScriptType(0);
        insertParam.setFileType(1);
        StringBuilder fileLengthBuilder = new StringBuilder();
        fileLengthBuilder.append(dto.getSceneId());
        fileLengthBuilder.append("-");
        fileLengthBuilder.append(dto.getFileName());
        long fileSize = 0;
        Object object = redisClientUtils.getObject(fileLengthBuilder.toString());
        if (object != null) {
            fileSize = Long.valueOf(object.toString());
        }
        insertParam.setFileSize(LinuxUtil.getPrintSize(fileSize));
        Map<String, Object> extend = new HashMap<>();
        String lineNum = redisClientUtils.getString(dto.getFileLineNumKey());
        if (lineNum != null) {
            extend.put(SCENE_REF_DATA_COUNT, Integer.valueOf(lineNum));
        }
        insertParam.setFileExtend(JSON.toJSONString(extend));
        try {
            TSceneScriptRefMapper.insertSelective(insertParam);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 将字节数组写入到文件
     *
     * @param dto
     */
    public void writeByteToDist(BigFileUploadVO dto) throws Exception {
        byte[] byteData = dto.getByteData();
        // 计算总行数
        String content = null;
        String[] data = null;
        try {
            content = new String(byteData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        if (content != null && content.contains("\r\n")) {
            data = content.split("\r\n");
            redisClientUtils.incrementAndNotExpire(dto.getFileLineNumKey(), data.length);
            content = null;
            data = null;
        }
        //写文件
        fileWriteService.write(dto.getStartPos(), dto.getFileName(), dto.getSceneId(), dto.getByteData());
    }

    @Override
    public File getPradarUploadFile() throws Exception {
        File docDir = new File(uploadClientPath);
        if (!docDir.exists()) {
            if (!docDir.mkdirs()) {
                throw new TROModuleException("pradarUpload client dir create failed.");
            }
        }
        File[] files = docDir.listFiles();
        if (files == null || files.length == 0) {
            throw new TROModuleException("pradarUpload client not found.");
        }
        for (File file : files) {
            if (file.isFile()) {
                return file;
            }
        }
        throw new TROModuleException("pradarUpload client not found.");
    }

}
