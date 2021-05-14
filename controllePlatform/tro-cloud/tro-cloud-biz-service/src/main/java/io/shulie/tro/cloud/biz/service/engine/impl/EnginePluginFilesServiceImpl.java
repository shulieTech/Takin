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

package io.shulie.tro.cloud.biz.service.engine.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginFileOutput;
import io.shulie.tro.cloud.biz.service.engine.EnginePluginFilesService;
import io.shulie.tro.cloud.common.bean.file.FileManageInfo;
import io.shulie.tro.cloud.common.constants.FileManageConstants;
import io.shulie.tro.cloud.data.mapper.mysql.EnginePluginFilesMapper;
import io.shulie.tro.cloud.data.model.mysql.EnginePluginFilesRef;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.shulie.tro.utils.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 引擎插件文件信息接口实现
 *
 * @author lipeng
 * @date 2021-01-13 5:28 下午
 */
@Slf4j
@Service
public class EnginePluginFilesServiceImpl extends ServiceImpl<EnginePluginFilesMapper, EnginePluginFilesRef> implements EnginePluginFilesService {

    @Value("${script.temp.path}")
    private String tempPath;

    @Value("${engine.plugins.nfs.prefix:'/nfs/tro/engine/plugins'}")
    private String enginePluginsNfsPrefix;

    @Resource
    private EnginePluginFilesMapper enginePluginFilesMapper;

    /**
     * 根据插件id获取文件路径
     *
     * @param pluginIds
     */
    @Override
    public List<String> findPluginFilesPathByPluginIds(List<Long> pluginIds) {
        List<String> result = Lists.newArrayList();
        //获取所有插件文件信息
        QueryWrapper<EnginePluginFilesRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("plugin_id", pluginIds);
        List<EnginePluginFilesRef> filesRefs = enginePluginFilesMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(filesRefs)) {
            //获取插件文件信息
            result = filesRefs.stream().map(EnginePluginFilesRef::getFilePath).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 根据插件id获取文件信息
     *
     * @param pluginId
     */
    @Override
    public List<EnginePluginFileOutput> findPluginFilesInfoByPluginId(Long pluginId) {
        List<EnginePluginFileOutput> result = Lists.newArrayList();
        if(Objects.isNull(pluginId)) {
            log.warn("pluginId is null");
            return result;
        }
        //获取所有插件文件信息
        QueryWrapper<EnginePluginFilesRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plugin_id", pluginId);
        List<EnginePluginFilesRef> filesRefs = enginePluginFilesMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(filesRefs)) {
            filesRefs.forEach(item -> {
                EnginePluginFileOutput row = new EnginePluginFileOutput();
                row.setFileId(item.getId());
                row.setFileName(item.getFileName());
                row.setFilePath(item.getFilePath());
                row.setIsDeleted(FileManageConstants.FILE_STATUS_UNDELETED);
                result.add(row);
            });
        }
        return result;
    }

    /**
     * 批量保存引擎插件文件信息
     *
     * @param pluginId 插件ID
     * @param files 文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveEnginePluginFiles(List<FileManageInfo> files, Long pluginId) {
        if(Objects.isNull(pluginId)) {
            log.warn("pluginId is null");
            return;
        }
        if(CollectionUtils.isNotEmpty(files)) {
            //要移除的文件ID
            List<Long> removeIds = Lists.newArrayList();
            //临时文件文件夹（方便删除）
            List<String> tempFileFolder = Lists.newArrayList();
            //本次要新增或覆盖的文件
            List<FileManageInfo> newAddFiles = Lists.newArrayList();
            for(FileManageInfo file : files) {
                Integer isDeleted = file.getIsDeleted();
                //已存在
                Long fileId = file.getFileId();
                if(fileId != null && fileId != 0) {
                    //已存在但要移除的
                    if(FileManageConstants.FILE_STATUS_DELETED == isDeleted) {
                        removeIds.add(file.getFileId());
                    }
                }
                //没有fileId说明是新增的文件，并且有uploadId说明有文件上传
                else if(!StringUtil.isBlank(file.getUploadId())) {
                    //存在但没有移除的，如果有uploadId说明文件被修改
                    newAddFiles.add(file);
                }
                //临时目录
                tempFileFolder.add(tempPath + File.separator + file.getUploadId());
            }
            //如果有移除的文件
            if(CollectionUtils.isNotEmpty(removeIds)) {
                //移除前获取信息
                List<EnginePluginFilesRef> removeFiles = this.listByIds(removeIds);
                //首先移除数据库的内容
                this.removeByIds(removeIds);
                //然后移除已存在的文件
                List<String> removePaths =
                        removeFiles.stream()
                        .map(EnginePluginFilesRef::getFilePath)
                        .collect(Collectors.toList());
                FileManagerHelper.deleteFiles(removePaths);
            }

            //要保存的文件夹
            String saveFolder = enginePluginsNfsPrefix + File.separator + pluginId;
            //处理新增的文件
            if(CollectionUtils.isNotEmpty(newAddFiles)) {
                List<EnginePluginFilesRef> filesRefs = Lists.newArrayList();
                //临时文件列表
                List<String> tempFilePaths = Lists.newArrayList();
                //组装参数
                newAddFiles.forEach(item -> {
                    String fileName = item.getFileName();
                    //临时目录
                    String tempFolder = tempPath + File.separator + item.getUploadId();
                    //临时目录文件位置
                    tempFilePaths.add(tempFolder + File.separator + fileName);
                    EnginePluginFilesRef filesRef = new EnginePluginFilesRef();
                    //设置保存后的文件路径
                    filesRef.setFilePath(saveFolder + File.separator + fileName);
                    filesRef.setPluginId(pluginId);
                    filesRef.setFileName(fileName);
                    filesRefs.add(filesRef);
                });

                //将新添的文件信息添加到数据库
                this.saveBatch(filesRefs);

                try {
                    //将新增的文件存到nfs， nfs路径 = 前缀 + 插件id
                    FileManagerHelper.copyFiles(tempFilePaths
                            , enginePluginsNfsPrefix + File.separator + pluginId);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }

                //操作完成后移除临时目录
                FileManagerHelper.deleteFiles(tempFileFolder);
            }
        }
    }
}
