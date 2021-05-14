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

package io.shulie.tro.cloud.common.utils;

import java.util.Calendar;

import io.shulie.tro.cloud.common.constants.CollectorConstants;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.collector.util
 * @Date 2020-04-20 20:38
 */
public class CollectorUtil {


    /**
     * 时间窗口格式化
     * 取值 5秒以内
     * >0 - <=5 取值 秒：5
     * >5 - <=10 取值 秒：10
     * >55 - <=60 取值 秒：0   分钟：+1
     *
     * @param timestamp
     * @return
     */
    public static Calendar getTimeWindow(long timestamp) {
        int nowSecond = 0;
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timestamp);
        int second = instance.get(Calendar.SECOND);
        for (int time : CollectorConstants.timeWindow) {
            if (second <= time) {
                nowSecond = time;
                break;
            }
        }
        instance.set(Calendar.MILLISECOND, 0);
        if (CollectorConstants.SECOND_60 == nowSecond) {
            instance.set(Calendar.SECOND, 0);
            instance.add(Calendar.MINUTE, 1);
        } else {
            instance.set(Calendar.SECOND, nowSecond);
        }
        return instance;
    }

    /**
<<<<<<< HEAD:tro-cloud-common/src/main/java/io/shulie/tro/cloud/common/utils/CollectorUtil.java
     * 获取延迟10S的写入窗口格式化时间。
     *
=======
     * 获取延迟5S的写入窗口格式化时间。
>>>>>>> 5fbcab6f18c8ec59c319c598b40b92283bfbfaed:tro-cloud-app/src/main/java/io/shulie/tro/cloud/app/utils/CollectorUtil.java
     * @param timestamp
     * @return
     */
    public static long getPushWindowTime(long timestamp) {
        Calendar instance = getTimeWindow(timestamp);
        instance.add(Calendar.SECOND, -5);
        return instance.getTimeInMillis();
    }

    /**
     * 获取5S后的写入窗口格式化时间。
     *
     * @param timestamp
     * @return
     */
    public static long addWindowTime(long timestamp) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timestamp);
        instance.add(Calendar.SECOND, 5);
        return instance.getTimeInMillis();
    }

    /**
     * 获取结束时间的写入窗口格式化时间。
     *
     * @param timestamp
     * @return
     */
    public static long getEndWindowTime(long timestamp) {
        Calendar instance = getTimeWindow(timestamp);
        return instance.getTimeInMillis();
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 56);

        Calendar timeWindow = getTimeWindow(calendar.getTimeInMillis());
        System.out.println(timeWindow.getTime());
    }

}
