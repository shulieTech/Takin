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

package io.shulie.tro.web.data.dao.filemanage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.mapper.mysql.FileManageMapper;
import io.shulie.tro.web.data.model.mysql.FileManageEntity;
import io.shulie.tro.web.data.param.filemanage.FileManageCreateParam;
import io.shulie.tro.web.data.result.filemanage.FileManageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class FileManageDAOImpl implements FileManageDAO {

    @Autowired
    private FileManageMapper fileManageMapper;

    @Override
    public List<FileManageResult> selectFileManageByIds(List<Long> fileIds) {
        LambdaQueryWrapper<FileManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            FileManageEntity::getFileExtend,
            FileManageEntity::getFileName,
            FileManageEntity::getFileSize,
            FileManageEntity::getFileType,
            FileManageEntity::getUploadPath,
            FileManageEntity::getUploadTime,
            FileManageEntity::getId,
            FileManageEntity::getIsDeleted
        );
        wrapper.in(FileManageEntity::getId, fileIds);
        List<FileManageEntity> fileManageEntities = fileManageMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(fileManageEntities)) {
            return null;
        }
        return fileManageEntities.stream().map(fileManageEntity -> {
            FileManageResult fileManageResult = new FileManageResult();
            fileManageResult.setId(fileManageEntity.getId());
            fileManageResult.setFileName(fileManageEntity.getFileName());
            fileManageResult.setFileSize(fileManageEntity.getFileSize());
            fileManageResult.setFileExt(fileManageEntity.getFileExt());
            fileManageResult.setFileType(fileManageEntity.getFileType());
            fileManageResult.setFileExtend(fileManageEntity.getFileExtend());
            fileManageResult.setCustomerId(fileManageEntity.getCustomerId());
            fileManageResult.setUploadTime(fileManageEntity.getUploadTime());
            fileManageResult.setUploadPath(fileManageEntity.getUploadPath());
            fileManageResult.setIsDeleted(fileManageEntity.getIsDeleted());
            fileManageResult.setGmtCreate(fileManageEntity.getGmtCreate());
            fileManageResult.setGmtUpdate(fileManageEntity.getGmtUpdate());
            return fileManageResult;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteByIds(List<Long> fileIds) {
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileManageMapper.deleteBatchIds(fileIds);
        }
    }

    @Override
    public List<Long> createFileManageList(List<FileManageCreateParam> fileManageCreateParams) {
        if (CollectionUtils.isEmpty(fileManageCreateParams)) {
            return Collections.emptyList();
        }

        // 收集 fileIds, 并赋值给 params
        return fileManageCreateParams.stream().map(fileManageCreateParam -> {
            FileManageEntity fileManageEntity = new FileManageEntity();
            BeanUtils.copyProperties(fileManageCreateParam, fileManageEntity);
            fileManageMapper.insert(fileManageEntity);

            // 并赋值给 params
            fileManageCreateParam.setId(fileManageEntity.getId());
            return fileManageEntity.getId();
        })
            .collect(Collectors.toList());
    }
}
