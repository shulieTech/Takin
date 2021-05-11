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

public class InstanceAppAdaptor extends DefaultAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(InstanceParentAdaptor.class);

    private AdaptorTemplate adaptorTemplate;
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
        if (dataContext.getChildPaths() != null) {
            for (Object path : dataContext.getChildPaths()) {
                if (!paths.contains(dataContext.getPath() + "/" + path)) {
                    try {
                        adaptorTemplate.addPath(Connector.ConnectorType.ZOOKEEPER_NODE, dataContext.getPath() + "/" + path, InstanceModel.class, adaptorTemplate.getAdapter(InstanceAdaptor.class));
                    } catch (Exception e) {
                        logger.error("Add path listener error");
                    }
                }
            }
        }
        return null;
    }
}