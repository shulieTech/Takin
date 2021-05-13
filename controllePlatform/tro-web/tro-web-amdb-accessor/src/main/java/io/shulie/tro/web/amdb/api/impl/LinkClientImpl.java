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

package io.shulie.tro.web.amdb.api.impl;

import io.shulie.tro.web.amdb.api.LinkClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 链路相关
 *
 * @author shiyajian
 * create: 2020-12-14
 */
@Component
@Slf4j
public class LinkClientImpl implements LinkClient {

    @Override
    public Object getFullLinkByEntryName(String entryName) {
        return null;
    }
}
