package io.shulie.tro.exception.entity;

/**
 * 可读性的异常接口
 *
 * @author shiyajian
 * create: 2020-09-24
 */
public interface ExceptionReadable {

    /**
     * 异常在资源文件中的key
     */
    String getErrorCode();

    /**
     * 如果资源文件中没有找到key，那么对应的默认值
     */
    String getDefaultValue();
}
