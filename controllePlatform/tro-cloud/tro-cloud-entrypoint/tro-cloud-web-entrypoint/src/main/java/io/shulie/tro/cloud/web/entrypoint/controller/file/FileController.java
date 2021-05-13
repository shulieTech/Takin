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

package io.shulie.tro.cloud.web.entrypoint.controller.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.domain.dto.file.FileDTO;
import com.pamirs.tro.entity.domain.vo.file.FileDeleteVO;
import io.shulie.tro.cloud.common.constants.SceneManageConstant;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.cloud.common.utils.LinuxUtil;
import io.shulie.tro.cloud.web.entrypoint.controller.strategy.LocalFileStrategy;
import io.shulie.tro.cloud.web.entrypoint.request.filemanage.FileCopyParamRequest;
import io.shulie.tro.cloud.web.entrypoint.request.filemanage.FileCreateByStringParamRequest;
import io.shulie.tro.cloud.web.entrypoint.request.filemanage.FileDeleteParamRequest;
import io.shulie.tro.cloud.web.entrypoint.request.filemanage.FileZipParamRequest;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName FileController
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:50
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = "文件管理")
@Slf4j
public class FileController {

    @Value("${script.temp.path}")
    private String tempPath;

    @Value("${script.path}")
    private String scriptPath;

    @Autowired
    private LocalFileStrategy fileStrategy;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public ResponseResult upload(List<MultipartFile> file) {
        List<FileDTO> dtoList = Lists.newArrayList();
        for (MultipartFile mf : file) {
            String uploadId = UUID.randomUUID().toString();
            File targetDir = new File(tempPath + SceneManageConstant.FILE_SPLIT + uploadId);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            File targetFile = new File(tempPath + SceneManageConstant.FILE_SPLIT
                + uploadId + SceneManageConstant.FILE_SPLIT + mf.getOriginalFilename());
            FileDTO dto = new FileDTO();
            try {
                dto.setUploadId(uploadId);
                dto.setUploadTime(DateUtil.getYYYYMMDDHHMMSS(new Date()));
                dto.setFileName(mf.getOriginalFilename());
                dto.setIsDeleted(0);
                dto.setIsSplit(0);
                dto.setDownloadUrl(targetDir + SceneManageConstant.FILE_SPLIT + mf.getOriginalFilename());
                mf.transferTo(targetFile);
                setDataCount(targetFile, dto);
                dto.setUploadResult(true);
                dto.setFileType(mf.getOriginalFilename().endsWith("jmx") ? 0 : 1);
            } catch (IOException e) {
                e.printStackTrace();
                dto.setUploadResult(false);
                dto.setErrorMsg(e.getMessage());
            }
            dtoList.add(dto);
        }
        return ResponseResult.success(dtoList);
    }

    @DeleteMapping
    @ApiOperation(value = "临时文件删除")
    public ResponseResult delete(@RequestBody FileDeleteVO vo) {
        if (vo.getUploadId() != null) {
            String targetDir = tempPath + SceneManageConstant.FILE_SPLIT + vo.getUploadId();
            LinuxUtil.executeLinuxCmd("rm -rf " + targetDir);
        }

        //根据文件： 删除大文件行数，删除大文件起始位置
        return ResponseResult.success();
    }

    @ApiOperation("脚本文件下载")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            String filePath = scriptPath + SceneManageConstant.FILE_SPLIT + fileName;

            if (new File(filePath).exists()) {
                ServletOutputStream outputStream = response.getOutputStream();
                Files.copy(Paths.get(filePath), outputStream);
                response.setContentType("application/octet-stream");
                String saveName = StringUtils.substring(fileName, StringUtils.indexOf(fileName, "/") + "/".length());
                response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(saveName.getBytes("UTF-8"), "iso-8859-1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("文件下载")
    @GetMapping(value = "/downloadFileByPath")
    public void downloadFileByPath(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        try {
            //反编码
            filePath =  URLDecoder.decode(filePath,"utf-8");
            boolean permit = fileStrategy.filePathValidate(filePath);

            if (!permit) {
                log.warn("非法下载路径文件，禁止下载：{}", filePath);
                return;
            }

            if (new File(filePath).exists()) {
                ServletOutputStream outputStream = response.getOutputStream();
                Files.copy(Paths.get(filePath), outputStream);
                response.setContentType("application/octet-stream");
                String saveName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(saveName.getBytes("UTF-8"), "iso-8859-1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/deleteFile")
    @ApiOperation(value = "文件删除")
    public Boolean deleteFile(@RequestBody FileDeleteParamRequest fileDeleteParamDTO) {
        return FileManagerHelper.deleteFiles(fileDeleteParamDTO.getPaths());
    }

    @PostMapping("/copyFile")
    @ApiOperation(value = "复制文件")
    public Boolean copyFile(@RequestBody FileCopyParamRequest fileCopyParamDTO) {
        try {
            FileManagerHelper.copyFiles(fileCopyParamDTO.getSourcePaths(), fileCopyParamDTO.getTargetPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @PostMapping("/zipFile")
    @ApiOperation(value = "打包文件")
    public Boolean zipFile(@RequestBody FileZipParamRequest fileZipParamDTO) {
        try {
            FileManagerHelper.zipFiles(fileZipParamDTO.getSourcePaths(), fileZipParamDTO.getTargetPath()
                , fileZipParamDTO.getZipFileName(), fileZipParamDTO.getIsCovered());
        } catch (Exception e) {
            log.error("文件打包失败", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @PostMapping("/createFileByPathAndString")
    @ApiOperation(value = "根据字符串创建文件")
    public Boolean createFileByPathAndString(@RequestBody FileCreateByStringParamRequest fileContent) {
        return FileManagerHelper.createFileByPathAndString(fileContent.getFilePath(), fileContent.getFileContent());
    }

    private void setDataCount(File file, FileDTO dto) {
        if (!file.getName().endsWith(".csv")) {
            return;
        }
        String topic = SceneManageConstant.SCENE_TOPIC_PREFIX + System.currentTimeMillis();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            Long length = 0L;
            String readData = null;
            while ((readData = br.readLine()) != null) {
                length++;
            }
            dto.setUploadedData(length);
            dto.setTopic(topic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
