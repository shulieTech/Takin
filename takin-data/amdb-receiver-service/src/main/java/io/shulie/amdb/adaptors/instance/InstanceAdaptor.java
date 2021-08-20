/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.amdb.adaptors.instance;

import com.alibaba.fastjson.JSON;
import io.shulie.amdb.adaptors.AdaptorTemplate;
import io.shulie.amdb.adaptors.base.DefaultAdaptor;
import io.shulie.amdb.adaptors.connector.Connector;
import io.shulie.amdb.adaptors.connector.DataContext;
import io.shulie.amdb.adaptors.instance.model.InstanceModel;
import io.shulie.amdb.adaptors.utils.FlagUtil;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vincent
 */
// FIXME 如果增加分布式部署，需要增加分布式锁，保证每个path只有一个服务在处理
@Slf4j
public class InstanceAdaptor extends DefaultAdaptor {


    private static final String INSTANCE_PATH = "/config/log/pradar/client/";
    private static String serverUrl = System.getProperty("instance.amdb.server.url");

    /**
     * path-> appName+"#"+ip+"#"+pid
     */
    private static final Map<String, String> instanceIdCache = new HashMap<>();

    private AppService appService;

    private AppInstanceService appInstanceService;

    private AdaptorTemplate adaptorTemplate;

    public InstanceAdaptor() {

    }


    @Override
    public void addConnector() {
        adaptorTemplate.addConnector(Connector.ConnectorType.ZOOKEEPER_NODE);
    }

    /**
     *
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
        System.out.println(dataContext);
        String path[] = dataContext.getPath().replaceAll(INSTANCE_PATH, "").split("/");
        String appName = path[0];
        InstanceModel instanceModel = (InstanceModel) dataContext.getModel();
        String oldInstanceKey = instanceIdCache.get(dataContext.getPath());
        if (instanceModel != null) {
            updateAppAndInstance(appName, instanceModel, oldInstanceKey);
            String newInstanceKey = appName + "#" + instanceModel.getAddress() + "#" + instanceModel.getPid();
            instanceIdCache.put(dataContext.getPath(), newInstanceKey);
        } else {
            // 说明节点被删除，执行实例下线
            if (oldInstanceKey != null) {
                instanceOffline(oldInstanceKey);
            }
        }
        return null;
    }

    private void updateAppAndInstance(String appName, InstanceModel instanceModel, String oldInstanceKey) {
        Date curr = new Date();
        // 判断APP记录是否存在
        AppDO params = new AppDO();
        params.setAppName(appName);
        AppDO appDO = appService.selectOneByParam(params);
        if (appDO == null) {
            appDO = getTAmdAppCreateModelByInstanceModel(appName, instanceModel, curr);
            // insert,拿到返回ID
            Response insertResponse = appService.insert(appDO);
            appDO.setId(NumberUtils.toLong(insertResponse.getData() + ""));
        } else {
            appDO = getTAmdAppUpdateModelByInstanceModel(instanceModel, appDO, curr);
            // update
            appService.update(appDO);
        }
        // 判断instance是否存在
        TAmdbAppInstanceDO amdbAppInstance = null;
        TAmdbAppInstanceDO selectParam = new TAmdbAppInstanceDO();
        // 如果有更新则需要拿到原来的唯一值检索
        if (StringUtils.isBlank(oldInstanceKey)) {
            selectParam.setAppName(appName);
            selectParam.setIp(instanceModel.getAddress());
            // 老的没有，说明服务重启缓存重置，这种情况下只能根据AgentID来更新
            selectParam.setAgentId(instanceModel.getAgentId());
        } else {
            String instanceInfo[] = oldInstanceKey.split("#");
            selectParam.setAppName(instanceInfo[0]);
            selectParam.setIp(instanceInfo[1]);
            selectParam.setPid(instanceInfo[2]);
        }
        TAmdbAppInstanceDO appInstanceDO = appInstanceService.selectOneByParam(selectParam);
        if (appInstanceDO == null) {
            amdbAppInstance = getTAmdAppInstanceCreateModelByInstanceModel(appDO, instanceModel, curr);
            appInstanceService.insert(amdbAppInstance);
        } else {
            amdbAppInstance = getTAmdAppInstanceUpdateModelByInstanceModel(appDO, instanceModel, appInstanceDO, curr);
            appInstanceService.update(amdbAppInstance);
        }
    }

    /**
     * 创建APP对象
     *
     * @param appName
     * @param instanceModel
     * @param curr
     * @return
     */
    private AppDO getTAmdAppCreateModelByInstanceModel(String appName, InstanceModel instanceModel, Date curr) {
        AppDO tAmdbApp = new AppDO();
        tAmdbApp.setAppName(appName);
        if (instanceModel.getExt() == null || instanceModel.getExt().length() == 0) {
            instanceModel.setExt("{}");
        }
        Map<String, Object> ext = JSON.parseObject(instanceModel.getExt());
        ext.put("jars", instanceModel.getJars());
        tAmdbApp.setExt(JSON.toJSONString(ext));
        tAmdbApp.setCreator("");
        tAmdbApp.setCreatorName("");
        tAmdbApp.setModifier("");
        tAmdbApp.setModifierName("");
        tAmdbApp.setGmtCreate(curr);
        tAmdbApp.setGmtModify(curr);
        return tAmdbApp;
    }

    /**
     * APP更新对象
     *
     * @param instanceModel
     * @param oldAmdbApp
     * @param curr
     * @return
     */
    private AppDO getTAmdAppUpdateModelByInstanceModel(InstanceModel instanceModel, AppDO oldAmdbApp, Date curr) {
        Map<String, Object> ext = JSON.parseObject(oldAmdbApp.getExt() == null ? "{}" : oldAmdbApp.getExt());
        if (ext == null) {
            ext = new HashMap<>();
        }
        ext.put("jars", instanceModel.getJars());
        oldAmdbApp.setExt(JSON.toJSONString(ext));
        oldAmdbApp.setGmtModify(curr);
        return oldAmdbApp;
    }

    /**
     * 实例创建对象
     *
     * @param amdbApp
     * @param instanceModel
     * @param curr
     * @return
     */
    private TAmdbAppInstanceDO getTAmdAppInstanceCreateModelByInstanceModel(AppDO amdbApp, InstanceModel instanceModel, Date curr) {
        TAmdbAppInstanceDO amdbAppInstance = new TAmdbAppInstanceDO();
        amdbAppInstance.setAppName(amdbApp.getAppName());
        amdbAppInstance.setAppId(amdbApp.getId());
        amdbAppInstance.setAgentId(instanceModel.getAgentId());
        amdbAppInstance.setIp(instanceModel.getAddress());
        amdbAppInstance.setPid(instanceModel.getPid());
        amdbAppInstance.setAgentVersion(instanceModel.getAgentVersion());
        amdbAppInstance.setMd5(instanceModel.getMd5());
        amdbAppInstance.setAgentLanguage(instanceModel.getAgentLanguage());
        Map<String, Object> ext = new HashMap<String, Object>();

        ext.put("errorMsgInfos", "{}");
        ext.put("gcType", instanceModel.getGcType());
        ext.put("host", instanceModel.getHost());
        ext.put("startTime", instanceModel.getStartTime());
        ext.put("jdkVersion", instanceModel.getJdkVersion());
        amdbAppInstance.setHostname(instanceModel.getHost());
        amdbAppInstance.setExt(JSON.toJSONString(ext));
        amdbAppInstance.setFlag(0);
        amdbAppInstance.setFlag(FlagUtil.setFlag(amdbAppInstance.getFlag(), 1, true));
        if (instanceModel.isStatus()) {
            amdbAppInstance.setFlag(FlagUtil.setFlag(amdbAppInstance.getFlag(), 2, true));
        } else {
            amdbAppInstance.setFlag(FlagUtil.setFlag(amdbAppInstance.getFlag(), 2, false));
        }
        amdbAppInstance.setCreator("");
        amdbAppInstance.setCreatorName("");
        amdbAppInstance.setModifier("");
        amdbAppInstance.setModifierName("");
        amdbAppInstance.setGmtCreate(curr);
        amdbAppInstance.setGmtModify(curr);
        return amdbAppInstance;
    }

    /**
     * 实例更新对象
     *
     * @param amdbApp
     * @param instanceModel
     * @param oldAmdbAppInstance
     * @param curr
     * @return
     */
    private TAmdbAppInstanceDO getTAmdAppInstanceUpdateModelByInstanceModel(AppDO amdbApp, InstanceModel instanceModel, TAmdbAppInstanceDO oldAmdbAppInstance, Date curr) {
        oldAmdbAppInstance.setAppName(amdbApp.getAppName());
        oldAmdbAppInstance.setAppId(amdbApp.getId());
        oldAmdbAppInstance.setAgentId(instanceModel.getAgentId());
        oldAmdbAppInstance.setIp(instanceModel.getAddress());
        oldAmdbAppInstance.setPid(instanceModel.getPid());
        oldAmdbAppInstance.setAgentVersion(instanceModel.getAgentVersion());
        oldAmdbAppInstance.setMd5(instanceModel.getMd5());
        oldAmdbAppInstance.setAgentLanguage(instanceModel.getAgentLanguage());
        oldAmdbAppInstance.setHostname(instanceModel.getHost());
        Map<String, Object> ext = JSON.parseObject(oldAmdbAppInstance.getExt());
        ext.put("errorMsgInfos", "{}");
        ext.put("gcType", instanceModel.getGcType());
        ext.put("host", instanceModel.getHost());
        ext.put("startTime", instanceModel.getStartTime());
        ext.put("jdkVersion", instanceModel.getJdkVersion());
        oldAmdbAppInstance.setExt(JSON.toJSONString(ext));
        // 改为在线状态
        oldAmdbAppInstance.setFlag(FlagUtil.setFlag(oldAmdbAppInstance.getFlag(), 1, true));
        // 设置Agent状态
        if (instanceModel.isStatus()) {
            // 设为正常状态
            oldAmdbAppInstance.setFlag(FlagUtil.setFlag(oldAmdbAppInstance.getFlag(), 2, true));
        } else {
            // 设置为异常状态
            oldAmdbAppInstance.setFlag(FlagUtil.setFlag(oldAmdbAppInstance.getFlag(), 2, false));
        }
        oldAmdbAppInstance.setGmtModify(curr);
        return oldAmdbAppInstance;
    }

    /**
     * 执行实例下线
     *
     * @param oldInstanceKey
     */
    private void instanceOffline(String oldInstanceKey) {
        TAmdbAppInstanceDO selectParam = new TAmdbAppInstanceDO();
        // 如果AgentId被修改，则用原先的ID来更新
        String instanceInfo[] = oldInstanceKey.split("#");
        selectParam.setAppName(instanceInfo[0]);
        selectParam.setIp(instanceInfo[1]);
        selectParam.setPid(instanceInfo[2]);
        TAmdbAppInstanceDO amdbAppInstanceDO = appInstanceService.selectOneByParam(selectParam);
        if (amdbAppInstanceDO == null) {
            return;
        }
        amdbAppInstanceDO.setFlag(FlagUtil.setFlag(amdbAppInstanceDO.getFlag(), 1, false));
        appInstanceService.update(amdbAppInstanceDO);
    }


    /**
     * @param config
     */
    @Override
    public void addConfig(Map<String, Object> config) {
        super.addConfig(config);

        /**
         *     private AppService appService;
         *
         *     private AppInstanceService appInstanceService;
         */
        if (!config.containsKey("appService") || !config.containsKey("appInstanceService")) {
            throw new IllegalArgumentException("AppService and appInstanceService is not init.");
        }
        this.appService = (AppService) config.get("appService");
        this.appInstanceService = (AppInstanceService) config.get("appInstanceService");
    }
}
