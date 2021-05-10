package io.shulie.surge.data.common.factory;

/**
 * 按照配置化描述 S 来创建 T 的实例
 *
 * @author pamirs
 */
public interface GenericFactory<T, S extends GenericFactorySpec<T>> {

    /**
     * 按照参数配置来创建 T
     * @param spec
     * @return
     * @throws Exception 参数不正确，或创建失败时抛出异常
     */
    T create(S spec) throws Exception;
}
