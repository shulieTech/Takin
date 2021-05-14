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

package io.shulie.tro.web.app.service.risk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: xingchen
 * @ClassName: DateUtil
 * @Package: com.pamirs.tro.web.api.service.risk.util
 * @Date: 2020/7/2819:32
 * @Description:
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static ThreadLocal<SimpleDateFormat> secondSimpledateFormatter = ThreadLocal.withInitial(() -> {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        return format;
    });

    public static Date parseSecondFormatter(String date) {
        try {
            return secondSimpledateFormatter.get().parse(date);
        } catch (ParseException e) {
            logger.error("时间格式错误{}", date);
        }
        return new Date();
    }
}
