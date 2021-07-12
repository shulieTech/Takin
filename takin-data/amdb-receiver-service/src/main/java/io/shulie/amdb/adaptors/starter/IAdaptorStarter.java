package io.shulie.amdb.adaptors.starter;

public interface IAdaptorStarter {

    void start() throws Exception;

    void stop() throws Exception;

    void restart() throws Exception;
}
