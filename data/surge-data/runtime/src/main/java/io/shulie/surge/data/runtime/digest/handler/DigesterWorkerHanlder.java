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
