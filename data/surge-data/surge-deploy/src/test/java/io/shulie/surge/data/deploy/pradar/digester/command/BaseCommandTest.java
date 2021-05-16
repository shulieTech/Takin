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

package io.shulie.surge.data.deploy.pradar.digester.command;

import junit.framework.TestCase;

//TODO P3 赋值空的时候 是否会被加上默认值
public class BaseCommandTest extends TestCase {

    public void testMeta() {
        //长度
    }

    public void testAction() {
        //本来应该是数字的结果放了个字符串
        //本来应该是字符串的结果放了数字
        //ext
        //列和值数量不匹配
    }
}