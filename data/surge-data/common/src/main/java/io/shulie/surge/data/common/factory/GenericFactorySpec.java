package io.shulie.surge.data.common.factory;

/**
 * 工厂 GenericFactory 创建 T 的实例的配置说明，实现类必须提供无参构造函数
 * @param <T>
 * @author pamirs
 * @author pamirs
 * @see GenericFactory
 */
public interface GenericFactorySpec<T> {

    /**
     * 返回工厂在注入时需要指定的名称
     * @return
     */
    String factoryName();

    /**
     * @return 被创建的对象的 interface
     */
    Class<T> productClass();
}
