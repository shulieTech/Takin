package io.shulie.surge.data.deploy.pradar.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.pamirs.pradar.log.parser.DataType;
import io.shulie.surge.data.deploy.pradar.digester.BaseDataDigester;
import io.shulie.surge.data.deploy.pradar.digester.LogDigester;
import io.shulie.surge.data.deploy.pradar.digester.MetricsDigester;
import io.shulie.surge.data.runtime.common.DataBootstrap;
import io.shulie.surge.data.runtime.common.DataRuntime;
import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.processor.DataQueue;
import io.shulie.surge.data.runtime.processor.ProcessorConfigSpec;
import io.shulie.surge.data.sink.clickhouse.ClickHouseModule;
import io.shulie.surge.data.sink.influxdb.InfluxDBModule;
import io.shulie.surge.data.sink.mysql.MysqlModule;
import io.shulie.surge.data.suppliers.nettyremoting.NettyRemotingModule;
import io.shulie.surge.data.suppliers.nettyremoting.NettyRemotingSupplier;
import io.shulie.surge.data.suppliers.nettyremoting.NettyRemotingSupplierSpec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * pradar supplier初始化
 */
public class PradarSupplierConfiguration {
    private Integer workPort;
    private Map<String, String> hostMap;
    private boolean registerZK;

    public PradarSupplierConfiguration() {
    }

    public PradarSupplierConfiguration(Integer workPort) {
        this.workPort = workPort;
    }

    public PradarSupplierConfiguration(Integer workPort, Object hostMapStr) {
        this.workPort = workPort;
        if (null != hostMapStr && StringUtils.isNotBlank(String.valueOf(hostMapStr))) {
            this.hostMap = JSON.parseObject(String.valueOf(hostMapStr), Map.class);
        }
    }

    public PradarSupplierConfiguration(Integer workPort, Object hostMapStr, Object registerZK) {
        this.workPort = workPort;
        if (null != hostMapStr && StringUtils.isNotBlank(String.valueOf(hostMapStr))) {
            this.hostMap = JSON.parseObject(String.valueOf(hostMapStr), Map.class);
        }
        if (null != registerZK && "false".equals(String.valueOf(registerZK))) {
            this.registerZK = false;
        } else {
            this.registerZK = true;
        }
    }

    /**
     * 初始化initDataRuntime
     *
     * @throws Exception
     */
    public DataRuntime initDataRuntime() {
        DataBootstrap bootstrap = DataBootstrap.create("deploy.properties", "pradar");
        bootstrap.install(
                new PradarModule(workPort),
                new NettyRemotingModule(),
                new InfluxDBModule(),
                new ClickHouseModule(),
                new MysqlModule());
        DataRuntime dataRuntime = bootstrap.startRuntime();
        return dataRuntime;
    }

    /**
     * 初始化
     *
     * @throws Exception
     */
    public void init() throws Exception {
        buildSupplier(initDataRuntime()).start();
    }

    /**
     * trace 日志 构建基础的消费digester ,可使用在单节点启动和storm集群中
     *
     * @param dataRuntime
     * @return
     */
    public DataDigester[] buildTraceLogProcess(DataRuntime dataRuntime) {
        LogDigester logDigester = dataRuntime.getInstance(LogDigester.class);
        return new DataDigester[]{
                logDigester
        };
    }


    /**
     * 基础cpu、load处理
     *
     * @param dataRuntime
     * @return
     */
    public DataDigester[] buildMonitorProcess(DataRuntime dataRuntime) {
        BaseDataDigester baseDataDigester = dataRuntime.getInstance(BaseDataDigester.class);
        return new DataDigester[]{baseDataDigester};
    }

    /**
     * 单节点的metrics计算
     *
     * @param dataRuntime
     * @return
     */
    public DataDigester[] buildMetricsProcess(DataRuntime dataRuntime) {
        MetricsDigester metricsDigester = dataRuntime.getInstance(MetricsDigester.class);
        return new DataDigester[]{metricsDigester};
    }

    /**
     * 创建订阅器
     *
     * @param dataRuntime
     * @return
     * @throws Exception
     */
    public NettyRemotingSupplier buildSupplier(DataRuntime dataRuntime) throws Exception {
        NettyRemotingSupplierSpec nettyRemotingSupplierSpec = new NettyRemotingSupplierSpec();
        nettyRemotingSupplierSpec.setHostMap(hostMap);
        nettyRemotingSupplierSpec.setRegisterZk(true);
        NettyRemotingSupplier nettyRemotingSupplier = dataRuntime.createGenericInstance(nettyRemotingSupplierSpec);
        ProcessorConfigSpec<PradarProcessor> traceLogProcessorConfigSpec = new PradarProcessorConfigSpec();
        traceLogProcessorConfigSpec.setName("trace-log");
        traceLogProcessorConfigSpec.setDigesters(ArrayUtils.addAll(buildTraceLogProcess(dataRuntime)));
        PradarProcessor traceLogProcessor = dataRuntime.createGenericInstance(traceLogProcessorConfigSpec);

        ProcessorConfigSpec<PradarProcessor> baseProcessorConfigSpec = new PradarProcessorConfigSpec();
        baseProcessorConfigSpec.setName("base");
        baseProcessorConfigSpec.setDigesters(buildMonitorProcess(dataRuntime));
        PradarProcessor baseProcessor = dataRuntime.createGenericInstance(baseProcessorConfigSpec);

        ProcessorConfigSpec<PradarProcessor> metricsProcessorConfigSpec = new PradarProcessorConfigSpec();
        metricsProcessorConfigSpec.setName("metrics");
        metricsProcessorConfigSpec.setDigesters(buildMetricsProcess(dataRuntime));
        PradarProcessor metricsProcessor = dataRuntime.createGenericInstance(metricsProcessorConfigSpec);


        Map<String, DataQueue> queueMap = Maps.newHashMap();
        queueMap.put(String.valueOf(DataType.TRACE_LOG), traceLogProcessor);
        queueMap.put(String.valueOf(DataType.MONITOR_LOG), baseProcessor);
        queueMap.put(String.valueOf(DataType.METRICS_LOG), metricsProcessor);
        nettyRemotingSupplier.setQueue(queueMap);
        return nettyRemotingSupplier;
    }


    public Map<String, String> getHostMap() {
        return hostMap;
    }

    public boolean isRegisterZK() {
        return registerZK;
    }

    public static void main(String[] args) throws Exception {
        PradarSupplierConfiguration pradarSupplierConfiguration = new PradarSupplierConfiguration();
        pradarSupplierConfiguration.init();
    }
}
