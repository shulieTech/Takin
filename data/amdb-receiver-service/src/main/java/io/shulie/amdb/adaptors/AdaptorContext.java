package io.shulie.amdb.adaptors;


import io.shulie.amdb.adaptors.common.Closeable;
import io.shulie.amdb.adaptors.connector.DataContext;

/**
 * 适配器上下文
 *
 * @author vincent
 */
public class AdaptorContext extends DataContext implements Closeable {

    private AdaptorModel model;

    /**
     * 获取model
     *
     * @return
     */
    public AdaptorModel model() {
        return model;
    }

    @Override
    public boolean close() {
        return true;
    }
}
