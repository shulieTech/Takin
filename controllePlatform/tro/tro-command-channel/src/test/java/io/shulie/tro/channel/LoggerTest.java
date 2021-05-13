package io.shulie.tro.channel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {

    Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test() {
        logger.info("test");
    }
}