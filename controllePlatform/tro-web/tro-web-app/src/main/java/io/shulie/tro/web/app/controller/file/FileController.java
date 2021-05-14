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

package io.shulie.tro.web.app.controller.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.dto.file.FileDTO;
import com.pamirs.tro.entity.domain.vo.file.FileDeleteVO;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.http.HttpWebClient;
import io.shulie.tro.web.common.util.FileUtil;
import io.shulie.tro.web.common.vo.FileWrapperVO;
import io.shulie.tro.web.diff.api.DiffFileApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private HttpWebClient httpWebClient;

    @Autowired
    private DiffFileApi fileApi;

    @Value("${file.upload.user.data.dir:/data/tmp}")
    private String fileDir;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public WebResponse upload(List<MultipartFile> file) {
        if (file == null || file.size() == 0) {
            return WebResponse.fail("上传文件不能为空");
        }
        FileWrapperVO wrapperVO = new FileWrapperVO();
        wrapperVO.setFile(FileUtil.convertMultipartFileList(file));
        wrapperVO.setRequestUrl(RemoteConstant.FILE_UPLOAD_URL);
        wrapperVO.setHttpMethod(HttpMethod.POST);
        WebResponse webResponse = httpWebClient.requestFile(wrapperVO);
        FileUtil.deleteTempFile(file);
        return webResponse;
    }

    @PostMapping("/attachment/upload")
    @ApiOperation(value = "文件上传")
    public WebResponse uploadAttachment(List<MultipartFile> file) {
        if (file == null || file.size() == 0) {
            return WebResponse.fail("上传文件不能为空");
        }
        FileWrapperVO wrapperVO = new FileWrapperVO();
        wrapperVO.setFile(FileUtil.convertMultipartFileList(file));
        wrapperVO.setRequestUrl(RemoteConstant.FILE_UPLOAD_URL);
        wrapperVO.setHttpMethod(HttpMethod.POST);
        WebResponse webResponse = httpWebClient.requestFile(wrapperVO);
        FileUtil.deleteTempFile(file);
        String jsonString = JsonHelper.bean2Json(webResponse.getData());
        List<FileDTO> dtoList = JsonHelper.json2List(jsonString, FileDTO.class);
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (FileDTO fileDTO : dtoList) {
                fileDTO.setFileType(2);
            }
        }
        return WebResponse.success(dtoList);
    }

    @DeleteMapping
    @ApiOperation(value = "文件删除")
    public WebResponse delete(@RequestBody FileDeleteVO vo) {
        if (vo.getUploadId() == null) {
            return WebResponse.fail("删除文件不能为空");
        }
        vo.setRequestUrl(RemoteConstant.FILE_URL);
        vo.setHttpMethod(HttpMethod.DELETE);
        return httpWebClient.request(vo);
    }

    @ApiOperation("文件下载")
    @GetMapping(value = "/downloadFileByPath")
    public void downloadFileByPath(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        try {

            if (!filePathValidate(filePath)) {
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
            // 最后删除文件
            FileManagerHelper.deleteFilesByPath(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 文件路径是否管理策略
     * @param filePath 文件路径
     * @return
     */
    private boolean filePathValidate(String filePath) { List<String> arrayList = init();

        for (int i = 0; i < arrayList.size(); i++) {
            if (filePath.startsWith(arrayList.get(i))){
                return true;
            }
        }
        return false;
    }
    private List<String> init() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(fileDir);
        return arrayList;
    }

}
