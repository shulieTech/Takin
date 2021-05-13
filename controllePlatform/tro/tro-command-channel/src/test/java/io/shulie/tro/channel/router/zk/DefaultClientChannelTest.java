package io.shulie.tro.channel.router.zk;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultClientChannelTest {

    @Test
    public void getPathIp() {


        DefaultClientChannel channel = new DefaultClientChannel();

        String pathIp = channel.getPathIp("dms-mall-8888");

        Assert.assertEquals("dms",pathIp);

        pathIp = channel.getPathIp("192.168.100.47-27248");


        Assert.assertEquals("192.168.100.47",pathIp);
    }
}