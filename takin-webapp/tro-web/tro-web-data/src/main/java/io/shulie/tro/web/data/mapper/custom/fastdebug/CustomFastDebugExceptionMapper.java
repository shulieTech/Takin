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

package io.shulie.tro.web.data.mapper.custom.fastdebug;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.shulie.tro.web.data.mapper.mysql.FastDebugExceptionMapper;
import io.shulie.tro.web.data.model.mysql.FastDebugExceptionEntity;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.mapper.custom.fastdebug
 * @date 2020/12/29 11:22 上午
 */
@Service
public class CustomFastDebugExceptionMapper extends ServiceImpl<FastDebugExceptionMapper, FastDebugExceptionEntity> {
}
