package io.shulie.surge.data.common.factory;

/**
 * 将 {@link GenericFactorySpec} 序列化 / 反序列化为 JSON 字符串
 *
 * @author pamirs
 */
public interface GenericFactorySpecSerializer {

    /**
     * 序列化为 JSON
     *
     * @param spec
     * @return
     */
    <T> String toJSONString(GenericFactorySpec<T> spec);

    /**
     * 从 JSON 反序列化为 GenericSpec 实例
     *
     * @param productInterface
     * @param name
     * @param jsonString
     * @return
     * @throws Exception
     */
    <T> GenericFactorySpec<T> fromJSONString(Class<T> productInterface,
                                             String name, String jsonString) throws Exception;
}
