/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.attach.plugin.redisson.RedissonConstants;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.apache.commons.lang.StringUtils;
import org.redisson.config.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/8 2:07 下午
 */
public abstract class BaseRedissonTimeSeriesMethodInterceptor extends TraceInterceptorAdaptor {

    private Method singleServerConfigGetAddressMethod;
    private Method masterSlaveServersConfigGetAddressMethod;

    @Resource
    protected DynamicFieldManager manager;

    public BaseRedissonTimeSeriesMethodInterceptor() {
        try {
            singleServerConfigGetAddressMethod = SingleServerConfig.class.getDeclaredMethod("getAddress");
            singleServerConfigGetAddressMethod.setAccessible(true);
        } catch (Throwable e) {
        }
        try {
            masterSlaveServersConfigGetAddressMethod = MasterSlaveServersConfig.class.getDeclaredMethod("getMasterAddress");
            masterSlaveServersConfigGetAddressMethod.setAccessible(true);
        } catch (Throwable e) {
        }
    }

    private String getAddress(SingleServerConfig config) {
        if (singleServerConfigGetAddressMethod != null) {
            try {
                Object result = singleServerConfigGetAddressMethod.invoke(config);
                if (result != null) {
                    return result.toString();
                }
                return null;
            } catch (Throwable e) {
                try {
                    Object result = Reflect.on(config).call("getAddress").get();
                    if (result != null) {
                        return result.toString();
                    }
                    return null;
                } catch (Throwable ex) {
                    LOGGER.warn("SIMULATOR: can't found getAddress method from {}, check {} compatibility.", SingleServerConfig.class.getName(), getClass().getName());
                }
            }
        } else {
            try {
                Object result = Reflect.on(config).call("getAddress").get();
                if (result != null) {
                    return result.toString();
                }
                return null;
            } catch (Throwable ex) {
                LOGGER.warn("SIMULATOR: can't found getAddress method from {}, check {} compatibility.", SingleServerConfig.class.getName(), getClass().getName());
            }
        }
        return null;
    }

    private String getMasterAddress(MasterSlaveServersConfig config) {
        if (masterSlaveServersConfigGetAddressMethod != null) {
            try {
                Object result = masterSlaveServersConfigGetAddressMethod.invoke(config);
                if (result != null) {
                    return result.toString();
                }
                return null;
            } catch (Throwable e) {
                try {
                    Object result = Reflect.on(config).call("getMasterAddress").get();
                    if (result != null) {
                        return result.toString();
                    }
                    return null;
                } catch (Throwable ex) {
                    LOGGER.warn("SIMULATOR: can't found getAddress method from {}, check {} compatibility.", MasterSlaveServersConfig.class.getName(), getClass().getName());
                }
            }
        } else {
            try {
                Object result = Reflect.on(config).call("getMasterAddress").get();
                if (result != null) {
                    return result.toString();
                }
                return null;
            } catch (Throwable ex) {
                LOGGER.warn("SIMULATOR: can't found getAddress method from {}, check {} compatibility.", SingleServerConfig.class.getName(), getClass().getName());
            }
        }
        return null;
    }

    @Override
    public String getPluginName() {
        return RedissonConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return RedissonConstants.PLUGIN_TYPE;
    }

    public int getDatabase(Object target, String methodName, Object[] args) {
        Config config = null;
        try {
            config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
        } catch (Throwable e) {
            return 0;
        }
        if (config == null) {
            return 0;
        }

        SentinelServersConfig sentinelServersConfig = getSentinelServersConfig(config);
        if (sentinelServersConfig != null) {
            int database = sentinelServersConfig.getDatabase();
            return database;
        }

        MasterSlaveServersConfig masterSlaveServersConfig = getMasterSlaveServersConfig(config);
        if (masterSlaveServersConfig != null) {
            return masterSlaveServersConfig.getDatabase();
        }

        SingleServerConfig singleServerConfig = getSingleServerConfig(config);
        if (singleServerConfig != null) {
            return singleServerConfig.getDatabase();
        }

        ClusterServersConfig clusterServersConfig = getClusterServersConfig(config);
        if (clusterServersConfig != null) {
            return 0;
        }

        ReplicatedServersConfig replicatedServersConfig = getReplicatedServersConfig(config);
        if (replicatedServersConfig != null) {
            return replicatedServersConfig.getDatabase();
        }

        return 0;
    }

    private SentinelServersConfig getSentinelServersConfig(Config config) {
        SentinelServersConfig sentinelServersConfig = null;
        try {
            sentinelServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SENTINEL_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        return sentinelServersConfig;
    }

    private MasterSlaveServersConfig getMasterSlaveServersConfig(Config config) {
        MasterSlaveServersConfig masterSlaveServersConfig = null;
        try {
            masterSlaveServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_MASTER_SLAVE_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        return masterSlaveServersConfig;
    }

    private SingleServerConfig getSingleServerConfig(Config config) {
        SingleServerConfig singleServerConfig = null;
        try {
            singleServerConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SINGLE_SERVER_CONFIG);
        } catch (ReflectException e) {
        }
        return singleServerConfig;
    }

    private ClusterServersConfig getClusterServersConfig(Config config) {
        ClusterServersConfig clusterServersConfig = null;
        try {
            clusterServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_CLUSTER_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        return clusterServersConfig;
    }

    private ReplicatedServersConfig getReplicatedServersConfig(Config config) {
        ReplicatedServersConfig replicatedServersConfig = null;
        try {
            replicatedServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_REPLICATED_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        return replicatedServersConfig;
    }

    public String getHost(Object target, String methodName, Object[] args) {
        Config config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
        if (config == null) {
            return null;
        }


        SentinelServersConfig sentinelServersConfig = getSentinelServersConfig(config);
        if (sentinelServersConfig != null) {
            List<String> list = sentinelServersConfig.getSentinelAddresses();
            if (list != null && !list.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (String str : list) {
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") != -1) {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[1]).append(',');
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                if (builder.length() > 0) {
                    return builder.toString();
                } else {
                    return "6379";
                }
            }
        }

        MasterSlaveServersConfig masterSlaveServersConfig = getMasterSlaveServersConfig(config);
        if (masterSlaveServersConfig != null) {
            String masterAddress = masterSlaveServersConfig.getMasterAddress();
            StringBuilder builder = new StringBuilder();
            if (StringUtils.isNotBlank(masterAddress)) {
                if (StringUtils.indexOf(masterAddress, ":") != -1) {
                    String[] arr = StringUtils.split(masterAddress, ':');
                    builder.append(arr[0]).append(',');
                }
            }

            Set<String> set = masterSlaveServersConfig.getSlaveAddresses();
            if (set != null && !set.isEmpty()) {
                for (String str : set) {
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") != -1) {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[0]).append(',');
                    }
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            if (builder.length() > 0) {
                return builder.toString();
            } else {
                return "6379";
            }
        }

        SingleServerConfig singleServerConfig = getSingleServerConfig(config);
        if (singleServerConfig != null) {
            String address = getAddress(singleServerConfig);
            if (StringUtils.isNotBlank(address)) {
                if (StringUtils.indexOf(address, ":") == -1) {
                    return "6379";
                } else {
                    String[] arr = StringUtils.split(address, ':');
                    return StringUtils.trim(arr[1]);
                }
            }
        }

        ClusterServersConfig clusterServersConfig = getClusterServersConfig(config);
        if (clusterServersConfig != null) {
            List list = clusterServersConfig.getNodeAddresses();
            if (list != null && !list.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (Object obj : list) {
                    String str = obj == null ? null : obj.toString();
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") != -1) {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[0]).append(',');
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                if (builder.length() > 0) {
                    return builder.toString();
                } else {
                    return "6379";
                }
            }
        }

        ReplicatedServersConfig replicatedServersConfig = getReplicatedServersConfig(config);
        if (replicatedServersConfig != null) {
            List list = replicatedServersConfig.getNodeAddresses();
            StringBuilder builder = new StringBuilder();
            for (Object obj : list) {
                String str = obj == null ? null : obj.toString();
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                if (StringUtils.indexOf(str, ":") != -1) {
                    String[] arr = StringUtils.split(str, ':');
                    builder.append(arr[0]).append(',');
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            if (builder.length() > 0) {
                return builder.toString();
            } else {
                return "6379";
            }
        }

        return null;
    }

    public String getPort(Object target, String methodName, Object[] args) {
        Config config = manager.getDynamicField(target, RedissonConstants.DYNAMIC_FIELD_CONFIG);
        if (config == null) {
            return null;
        }
        SentinelServersConfig sentinelServersConfig = getSentinelServersConfig(config);
        if (sentinelServersConfig != null) {
            List list = sentinelServersConfig.getSentinelAddresses();
            if (list != null && !list.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (Object obj : list) {
                    String str = obj == null ? null : obj.toString();
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") == -1) {
                        builder.append(str).append(',');
                    } else {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[0]).append(',');
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                if (builder.length() > 0) {
                    return builder.toString();
                }
            }
        }

        MasterSlaveServersConfig masterSlaveServersConfig = getMasterSlaveServersConfig(config);
        if (masterSlaveServersConfig != null) {
            String masterAddress = getMasterAddress(masterSlaveServersConfig);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.isNotBlank(masterAddress)) {
                if (StringUtils.indexOf(masterAddress, ":") == -1) {
                    builder.append(masterAddress).append(',');
                } else {
                    String[] arr = StringUtils.split(masterAddress, ':');
                    builder.append(arr[0]).append(',');
                }
            }

            Set set = masterSlaveServersConfig.getSlaveAddresses();
            if (set != null && !set.isEmpty()) {
                for (Object obj : set) {
                    String str = obj == null ? null : obj.toString();
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") == -1) {
                        builder.append(str).append(',');
                    } else {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[0]).append(',');
                    }
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            if (builder.length() > 0) {
                return builder.toString();
            }
        }

        SingleServerConfig singleServerConfig = getSingleServerConfig(config);
        if (singleServerConfig != null) {
            String address = getAddress(singleServerConfig);
            if (StringUtils.isNotBlank(address)) {
                if (StringUtils.indexOf(address, ":") == -1) {
                    return address;
                } else {
                    String[] arr = StringUtils.split(address, ':');
                    return StringUtils.trim(arr[0]);
                }
            }
        }

        ClusterServersConfig clusterServersConfig = getClusterServersConfig(config);
        if (clusterServersConfig != null) {
            List list = clusterServersConfig.getNodeAddresses();
            if (list != null && !list.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (Object obj : list) {
                    String str = obj == null ? null : obj.toString();
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (StringUtils.indexOf(str, ":") == -1) {
                        builder.append(str).append(',');
                    } else {
                        String[] arr = StringUtils.split(str, ':');
                        builder.append(arr[0]).append(',');
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                if (builder.length() > 0) {
                    return builder.toString();
                }
            }
        }

        ReplicatedServersConfig replicatedServersConfig = getReplicatedServersConfig(config);
        if (replicatedServersConfig != null) {
            List list = replicatedServersConfig.getNodeAddresses();
            StringBuilder builder = new StringBuilder();
            for (Object obj : list) {
                String str = obj == null ? null : obj.toString();
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                if (StringUtils.indexOf(str, ":") == -1) {
                    builder.append(str).append(',');
                } else {
                    String[] arr = StringUtils.split(str, ':');
                    builder.append(arr[0]).append(',');
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            if (builder.length() > 0) {
                return builder.toString();
            }
        }

        return null;
    }

    protected Object[] toArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] ret = new Object[args.length];
        for (int i = 0, len = args.length; i < len; i++) {
            Object arg = args[i];
            if (arg instanceof byte[]) {
                ret[i] = new String((byte[]) arg);
            } else if (arg instanceof char[]) {
                ret[i] = new String((char[]) arg);
            } else {
                ret[i] = arg;
            }
        }
        return ret;
    }
}
