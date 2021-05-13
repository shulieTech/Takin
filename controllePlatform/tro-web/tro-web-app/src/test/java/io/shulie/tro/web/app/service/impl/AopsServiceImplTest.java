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

package io.shulie.tro.web.app.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.web.api.service.impl
 * @date 2020/9/2 9:15 下午
 * @see AopsServiceImpl
 */
public class AopsServiceImplTest {
    public static void main(String[] args) throws ParseException {
        AopsServiceImpl a = new AopsServiceImpl();
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-07-18 00:51:14");
        String startMin = "-" + StringUtils.substringBeforeLast(
            ((System.currentTimeMillis() - date.getTime()) / (1000 * 60)) + "", ".") + "m";

        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-07-28 01:51:14");
        String endMin = "-" + StringUtils.substringBeforeLast(
            ((System.currentTimeMillis() - date1.getTime()) / (1000 * 60)) + "", ".") + "m";

        Map map = a.getAopsData("172.16.25.17", startMin, endMin,
            StringUtils.substringBefore((System.currentTimeMillis() / 1000 + ""), "."));
        System.out.println(map);
    }

}
