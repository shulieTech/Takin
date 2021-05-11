package io.shulie.amdb.adaptors.base;

import io.shulie.amdb.adaptors.Adaptor;
import io.shulie.amdb.adaptors.AdaptorContext;
import io.shulie.amdb.adaptors.connector.DataContext;

import java.util.Map;

/**
 * @author vincent
 */
public abstract class DefaultAdaptor implements Adaptor {
    protected Map<String, Object> config;

    @Override
    public DataContext getContext() {
        return new AdaptorContext();
    }


    /**
     * @param config
     */
    @Override
    public void addConfig(Map<String, Object> config) {
        this.config = config;
    }
}
