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

package io.shulie.surge.data.runtime.digest.handler;

import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigesterWorkerHanlder implements WorkHandler<DigestJob> {
    private static Logger logger = LoggerFactory.getLogger(DigesterHandler.class);
    DataDigester eed;
    int index;
    long[] digesterTimeCost;

    public DigesterWorkerHanlder(DataDigester eed, int index, long[] digesterTimeCost) {
        this.eed = eed;
        this.index = index;
        this.digesterTimeCost = digesterTimeCost;
    }

    @Override
    public void onEvent(DigestJob event) throws Exception {
        if (event.context == null) {
            return;
        }
        long now = System.currentTimeMillis();
        try {
            eed.digest(event.context);
        } catch (Throwable e) {
            logger.error("digest error ", e);
        }
        digesterTimeCost[index] += (System.currentTimeMillis() - now);
    }
}
