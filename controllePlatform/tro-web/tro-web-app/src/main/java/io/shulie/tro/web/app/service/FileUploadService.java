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

package io.shulie.tro.web.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.TroFileUtil;
import io.shulie.tro.web.app.common.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明:
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/5 21:18
 */
@Service
public class FileUploadService extends CommonService {

    /**
     * 说明: 批量上传文件并返回文件路径集合
     *
     * @param files 多个文件
     * @return 文件路径集合
     * @author shulie
     * @date 2018/9/5 21:22
     */
    public List<String> batchUploadFile(MultipartFile[] files) throws IOException, TROModuleException {
        if (files == null || files.length == 0) {
            throw new TROModuleException(TROErrorEnum.ASSIST_DBCONF_SQL_BATHC_UPLOAD_PARAM_EXCEPTION);
        }

        return new ArrayList<String>() {
            {
                for (MultipartFile multipartFile : files) {
                    add(executeFile(multipartFile));
                }
            }
        };
    }

    /**
     * 说明: 单个文件上传并返回文件路径;
     * 文件目录不存在则创建文件目录,文件若是名称已经存在,则重命名文件.
     *
     * @param file A representation of an uploaded file received in a multipart request.
     * @author shulie
     * @date 2018/9/5 19:22
     */
    public String executeFile(MultipartFile file) throws IOException, TROModuleException {
        long size = file.getSize();
        int length = file.getBytes().length;
        String fileName = file.getOriginalFilename();
        String suffixName = StringUtils.substringAfterLast(fileName, ".");
        LOGGER.info("上传的文件名为：" + fileName + " 文件的后缀名为：" + suffixName + " 文件的大小为：" + size + " 文件的长度为：" + length);

        if (!fileName.endsWith(".sql")) {
            throw new TROModuleException(TROErrorEnum.ASSIST_DBCONF_SQL_UPLOAD_TYPE_ERROR);
        }

        // 设置文件存储路径
        String path = getLoadSqlPath() + fileName;

        TroFileUtil.createDirByString(path);
        if (Files.exists(Paths.get(path))) {
            fileName = generateUUID() + "." + suffixName;
            path = getLoadSqlPath() + fileName;
        }
        //      file.transferTo(dest);// 文件写入
        Files.copy(file.getInputStream(), Paths.get(path));
        return path;
    }

    /**
     * 说明: 下载文件
     *
     * @param fileName 文件名称
     * @author shulie
     * @date 2018/9/6 11:12
     */
    public void downloadFile(String fileName, HttpServletResponse response) throws IOException, TROModuleException {
        String filePath = getLoadSqlPath() + fileName;
        TroFileUtil.createDirByString(filePath);
        if (new File(filePath).exists()) {
            ServletOutputStream outputStream = response.getOutputStream();
            Files.copy(Paths.get(filePath), outputStream);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
        }
    }
}
