package io.shulie.surge.data.runtime.parser;

import io.shulie.surge.data.runtime.digest.DigestContext;

import java.io.Serializable;
import java.util.Map;

/**
 * @author vincent
 */
public interface DataParser<IN extends Serializable, OUT extends Serializable> {
    /**
     * 创建数据处理上下文
     *
     * @param header
     * @return
     */
    DigestContext<OUT> createContext(Map<String, Object> header, IN data);
}
