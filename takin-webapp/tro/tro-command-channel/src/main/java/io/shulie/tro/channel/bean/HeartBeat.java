package io.shulie.tro.channel.bean;

/**
 * @author: HengYu
 * @className: Constants
 * @date: 2020/12/29 10:20 下午
 * @description:
 */
public class HeartBeat extends CommandCommon {

    private long ttl;

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}