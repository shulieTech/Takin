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

package io.shulie.tro.schedule.taskmanage.Impl;

import java.util.Calendar;

import io.shulie.tro.cloud.common.utils.CollectorUtil;
import org.junit.Test;

/**
 * @author 无涯
 * @Package io.shulie.tro.schedule.taskmanage.Impl
 * @date 2020/10/28 9:40 上午
 */
public class CollectorUtilTest {
    @Test
    public void test() {
        long time = 1603847885020L;
        Calendar calendar = CollectorUtil.getTimeWindow(time);
        System.out.println(calendar.getTimeInMillis());
    }
}
