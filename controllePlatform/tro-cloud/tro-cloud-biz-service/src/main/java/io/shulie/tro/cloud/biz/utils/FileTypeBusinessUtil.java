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

package io.shulie.tro.cloud.biz.utils;

import io.shulie.tro.cloud.common.enums.scenemanage.FileTypeEnum;

/**
 * 文件类型 业务工具类
 * 配合 @see FileTypeEnum
 *
 * @author liuchuan
 * @date 2021/4/25 9:46 上午
 */
public class FileTypeBusinessUtil {

    /**
     * 文件是脚本类型, 或是数据类型
     *
     * @param fileType 文件类型
     * @return 是否
     */
    public static boolean isScriptOrData(Integer fileType) {
        return isScript(fileType) || isData(fileType);
    }

    /**
     * 文件是脚本类型
     *
     * @param fileType 文件类型
     * @return 是否
     */
    public static boolean isScript(Integer fileType) {
        return FileTypeEnum.SCRIPT.getCode().equals(fileType);
    }

    /**
     * 文件是数据类型
     *
     * @param fileType 文件类型
     * @return 是否
     */
    public static boolean isData(Integer fileType) {
        return FileTypeEnum.DATA.getCode().equals(fileType);
    }

    /**
     * 文件是附件类型
     *
     * @param fileType 文件类型
     * @return 是否
     */
    public static boolean isAttachment(Integer fileType) {
        return FileTypeEnum.ATTACHMENT.getCode().equals(fileType);
    }

}
