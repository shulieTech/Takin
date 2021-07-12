package io.shulie.amdb.adaptors.starter;


public abstract class AbstractAdaptorStarter implements IAdaptorStarter {

    public void start() throws Exception {
        beforeStart();
        doStart();
        afterStart();
    }

    protected abstract void doStart() throws Exception;

    abstract void afterStart();

    abstract void beforeStart();

    public void restart() throws Exception {
        start();
        stop();
    }

}
