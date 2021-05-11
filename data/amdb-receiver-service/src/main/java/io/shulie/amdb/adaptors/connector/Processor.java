package io.shulie.amdb.adaptors.connector;

/**
 * 执行器接口类
 *
 * @author vincent
 */
public interface Processor {

    /**
     * getway执行器
     *
     * @param dataContext
     */
    Object process(DataContext dataContext);


    /**
     * 获取上下文
     * @return
     */
    DataContext getContext();
}
