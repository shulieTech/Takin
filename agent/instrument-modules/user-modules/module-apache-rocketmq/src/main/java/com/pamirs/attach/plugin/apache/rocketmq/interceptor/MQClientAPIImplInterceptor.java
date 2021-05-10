/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.apache.rocketmq.interceptor;

import com.pamirs.attach.plugin.apache.rocketmq.RocketmqConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.common.message.Message;

/**
 * @Author: guohz
 * @ClassName: MQClientAPIImplInterceptor
 * @Package: com.pamirs.attach.plugin.apache.rocketmq.interceptor
 * @Date: 2019/10/24 11:31 下午
 * @Description: 处理异步RocketMQ发送回调方法识别压测标
 */
public class MQClientAPIImplInterceptor extends AroundInterceptor {

    @Override
    public void doBefore(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (PradarSwitcher.isClusterTestEnabled()) {
            Message msg = (Message) args[1];
            //压测代码-
            try {
                validatePressureMeasurement(msg);
            } catch (Throwable e) {
                throw new PressureMeasureError(e);
            }

        }
    }

    private void validatePressureMeasurement(Message msg) {
        String topic = msg.getTopic();
        Pradar.setClusterTest(false);
        topic = StringUtils.trimToNull(topic);
        if (topic == null) {
            return;
        }
        if ((Pradar.isClusterTestPrefix(topic))
                || (Pradar.isClusterTestPrefix(topic, RocketmqConstants.RETRYSTR))
                || (Pradar.isClusterTestPrefix(topic, RocketmqConstants.DLQSTR))) {
            Pradar.setClusterTest(true);
        }
        if (ClusterTestUtils.isClusterTestRequest(msg.getUserProperty(PradarService.PRADAR_CLUSTER_TEST_KEY))) {
            Pradar.setClusterTest(true);
        }
    }
}
