package io.shulie.amdb.adaptors;


import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 适配器工厂类
 * @author vincent
 */
public class AdaptorFactory {

    private Set<Adaptor> adaptors = new HashSet<Adaptor>();

    private void init() {
        ServiceLoader<Adaptor> adaptorLoader = ServiceLoader.load(Adaptor.class);
        for (Adaptor adaptor : adaptorLoader) {
            adaptors.add(adaptor);
        }
    }

    private AdaptorFactory() {
        init();
    }

    private static class AdaptorFactoryHolder {
        public final static AdaptorFactory INSTANCE = new AdaptorFactory();
    }

    public static AdaptorFactory getFactory() {
        return AdaptorFactoryHolder.INSTANCE;
    }

    /**
     * 获取适配器列表
     * @return
     */
    public Set<Adaptor> getAdaptors() {
        return adaptors;
    }

}
