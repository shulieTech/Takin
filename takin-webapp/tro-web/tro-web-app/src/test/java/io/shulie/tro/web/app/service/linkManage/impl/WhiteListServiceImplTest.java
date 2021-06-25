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

package io.shulie.tro.web.app.service.linkManage.impl;

import io.shulie.tro.web.common.vo.whitelist.WhiteListVO;
import org.junit.Assert;
import org.junit.Test;

public class WhiteListServiceImplTest {

    @Test
    public void whiteListCompare() {
        WhiteListServiceImpl serviceImpl = new WhiteListServiceImpl();
        WhiteListVO o1 = new WhiteListVO();
        o1.setInterfaceName("abc");
        WhiteListVO o2 = new WhiteListVO();
        o2.setInterfaceName("bc");
        int i = serviceImpl.whiteListCompare(o1, o2);
        Assert.assertEquals(-1, i);
    }
}