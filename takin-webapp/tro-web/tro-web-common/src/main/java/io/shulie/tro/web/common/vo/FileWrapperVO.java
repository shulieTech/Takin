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

package io.shulie.tro.web.common.vo;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import io.shulie.tro.web.common.domain.WebRequest;
import lombok.Data;

/**
 * @ClassName FileWrapperVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/11 下午9:11
 */
@Data
public class FileWrapperVO extends WebRequest implements Serializable {

    private static final long serialVersionUID = 8484664446939598273L;

    private List<File> file;
}
