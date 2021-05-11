package io.shulie.amdb.adaptors.instance;

import com.google.common.collect.Sets;
import io.shulie.amdb.adaptors.AdaptorTemplate;
import io.shulie.amdb.adaptors.base.DefaultAdaptor;
import io.shulie.amdb.adaptors.connector.Connector;
import io.shulie.amdb.adaptors.connector.DataContext;
import io.shulie.amdb.adaptors.instance.model.InstanceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 父实例扫描
 *
 * @author vincent
 */
public class InstanceParentAdaptor extends DefaultAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(InstanceParentAdaptor.class);

    private AdaptorTemplate adaptorTemplate;
    private static final String INSTANCE_PATH = "/config/log/pradar/client";
    private Set<String> paths = Sets.newHashSet();

    @Override
    public void addConnector() {
        adaptorTemplate.addConnector(Connector.ConnectorType.ZOOKEEPER_PATH);
    }

    /**
     * 注册
     */
    @Override
    public void registor() {
        try {
            adaptorTemplate.addPath(Connector.ConnectorType.ZOOKEEPER_PATH, INSTANCE_PATH, InstanceModel.class, this);
        } catch (Exception e) {
            logger.error("Adapter add path error.", e);
        }
    }


    @Override
    public void setAdaptorTemplate(AdaptorTemplate adaptorTemplate) {
        this.adaptorTemplate = adaptorTemplate;
    }

    @Override
    public boolean close() throws Exception {
        return false;
    }

    @Override
    public Object process(DataContext dataContext) {
        if (!paths.contains(dataContext.getChildPaths())) {
            for (Object path : dataContext.getChildPaths()) {
                if (!paths.contains(dataContext.getPath()+"/"+path)) {
                    try {
                        adaptorTemplate.addPath(Connector.ConnectorType.ZOOKEEPER_PATH, dataContext.getPath()+"/"+path, InstanceModel.class, adaptorTemplate.getAdapter(InstanceAppAdaptor.class));
                    } catch (Exception e) {
                        logger.error("Add path listener error");
                    }
                }
            }
        }
        return null;
    }

}
