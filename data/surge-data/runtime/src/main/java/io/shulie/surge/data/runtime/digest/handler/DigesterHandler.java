package io.shulie.surge.data.runtime.digest.handler;

import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigesterHandler implements EventHandler<DigestJob> {
    private static Logger logger = LoggerFactory.getLogger(DigesterHandler.class);
    DataDigester eed;
    int index;
    long[] digesterTimeCost;

    public DigesterHandler(DataDigester eed, int index, long[] digesterTimeCost) {
        this.eed = eed;
        this.index = index;
        this.digesterTimeCost = digesterTimeCost;
    }

    @Override
    public void onEvent(DigestJob event, long sequence, boolean endOfBatch) throws Exception {
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
