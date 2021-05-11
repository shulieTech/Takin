package io.shulie.amdb.adaptors.common;

import java.io.Serializable;

/**
 * @author vincent
 */
public interface Closeable extends Serializable {

    /**
     * 关闭接口
     * @return
     */
    boolean close() throws Exception;
}
