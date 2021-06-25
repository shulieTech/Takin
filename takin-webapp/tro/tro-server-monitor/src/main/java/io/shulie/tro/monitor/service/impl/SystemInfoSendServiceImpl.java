package io.shulie.tro.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.*;
import io.shulie.tro.monitor.request.PressureMachineRequest;
import io.shulie.tro.monitor.content.SystemHardwareInfo;
import io.shulie.tro.monitor.service.SystemInfoSendService;
import io.shulie.tro.monitor.util.HttpClientUtils;
import io.shulie.tro.monitor.util.K8sClient;
import io.shulie.tro.monitor.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: HengYu
 */
@Slf4j
@Component
public class SystemInfoSendServiceImpl implements SystemInfoSendService {

    @Value("${receive.urls}")
    private String receiveUrls;

    @Autowired
    private K8sClient k8sClient;

    @Override
    public void sendSystemInfo() {

        try {
            log.info("开始收集信息");
            PressureMachineRequest pressureMachineRequest = getPressureMachineRequest();

            String[] splits = receiveUrls.split(",");
            for (String url : splits) {
                log.info("收集信息完毕，开始发送信息,入参为："+JSONObject.toJSONString(pressureMachineRequest));
                String doJsonPost = HttpClientUtils.getInstance().doJsonPost(url, JSONObject.toJSONString(pressureMachineRequest));
                log.info("发送结束，结果为："+doJsonPost);
            }
        } catch (Exception e) {
            log.error("获取机器信息失败！", e);
        }

    }

    private PressureMachineRequest getPressureMachineRequest() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.setCpuDTO(ServerUtils.getCpuInfo(hal, os));
        systemHardwareInfo.setMemDTO(ServerUtils.getMemInfo(hal, os));
        systemHardwareInfo.setServerDTO(ServerUtils.getServerInfo(hal, os));
        systemHardwareInfo.setServerFileDTO(ServerUtils.getServerFileInfo(hal, os));
        PressureMachineRequest pressureMachineRequest = getPressureMachineRequest(systemHardwareInfo);
        setSceneId(pressureMachineRequest);
        return pressureMachineRequest;
    }

    private void setSceneId(PressureMachineRequest pressureMachineRequest){
        try {
            List<Pod> items = k8sClient.getKubernetesClient().pods().list().getItems();
            Map<String, List<Long>> sceneIpMap = new HashMap<>();
            for (Pod pod : items) {
                String name = pod.getMetadata().getName();
                if (name != null && name.startsWith("scene-task-") && "Running".equals(pod.getStatus().getPhase())) {
                    String[] split = StringUtils.split(name, "-");
                    String hostIp = pod.getStatus().getHostIP();
                    if (sceneIpMap.get(hostIp) != null){
                        sceneIpMap.get(hostIp).add(Long.valueOf(split[2]));
                    }else {
                        ArrayList<Long> longs = new ArrayList<>();
                        longs.add(Long.valueOf(split[2]));
                        sceneIpMap.put(hostIp, longs);
                    }
                }
            }
            if (sceneIpMap.get(pressureMachineRequest.getIp()) != null) {
                pressureMachineRequest.setSceneId(sceneIpMap.get(pressureMachineRequest.getIp()));
                pressureMachineRequest.setStatus(1);
            } else {
                pressureMachineRequest.setStatus(0);
            }
        }catch (Exception e){
            log.error("获取场景信息失败！",e);
        }
    }

    private PressureMachineRequest getPressureMachineRequest(SystemHardwareInfo systemHardwareInfo) {
        PressureMachineRequest pressureMachineRequest = new PressureMachineRequest();
        pressureMachineRequest.setName(systemHardwareInfo.getServerDTO().getComputerName());
        pressureMachineRequest.setIp(systemHardwareInfo.getServerDTO().getComputerIp());

        pressureMachineRequest.setCpu(systemHardwareInfo.getCpuDTO().getCpuNum());
        pressureMachineRequest.setMemory(systemHardwareInfo.getMemDTO().getTotal());
        pressureMachineRequest.setDisk(systemHardwareInfo.getServerFileDTO().getTotal());
        if (systemHardwareInfo.getCpuDTO().getTotal() != 0) {
            pressureMachineRequest.setCpuUsage(BigDecimal.valueOf((systemHardwareInfo.getCpuDTO().getTotal() - systemHardwareInfo.getCpuDTO().getFree()) / systemHardwareInfo.getCpuDTO().getTotal()));
        }
        pressureMachineRequest.setCpuLoad(BigDecimal.valueOf(systemHardwareInfo.getCpuDTO().getCpuAvgLoad()));
        if (systemHardwareInfo.getMemDTO().getTotal() != 0) {
            pressureMachineRequest.setMemoryUsed(BigDecimal.valueOf(systemHardwareInfo.getMemDTO().getUsed() * 1.0 / systemHardwareInfo.getMemDTO().getTotal()));
        }
        pressureMachineRequest.setDiskIoWait(BigDecimal.valueOf(systemHardwareInfo.getServerFileDTO().getIoUsage()));
        pressureMachineRequest.setTransmittedIn(systemHardwareInfo.getServerDTO().getReceive());
        if (systemHardwareInfo.getServerDTO().getTotalReceive() != null && systemHardwareInfo.getServerDTO().getTotalReceive() != 0) {
            pressureMachineRequest.setTransmittedInUsage(BigDecimal.valueOf(systemHardwareInfo.getServerDTO().getReceive() * 1.0 / systemHardwareInfo.getServerDTO().getTotalReceive()));
        }
        pressureMachineRequest.setTransmittedOut(systemHardwareInfo.getServerDTO().getTransmitted());
        if (systemHardwareInfo.getServerDTO().getTotalTransmitted() != null && systemHardwareInfo.getServerDTO().getTotalTransmitted() != 0) {
            pressureMachineRequest.setTransmittedOutUsage(BigDecimal.valueOf(systemHardwareInfo.getServerDTO().getTransmitted() * 1.0 / systemHardwareInfo.getServerDTO().getTotalTransmitted()));
        }
        if (pressureMachineRequest.getTransmittedInUsage() != null && pressureMachineRequest.getTransmittedOutUsage() != null){
            pressureMachineRequest.setTransmittedUsage(pressureMachineRequest.getTransmittedInUsage().compareTo(pressureMachineRequest.getTransmittedOutUsage()) > 0 ? pressureMachineRequest.getTransmittedInUsage() : pressureMachineRequest.getTransmittedOutUsage());
        }else {
            pressureMachineRequest.setTransmittedUsage(new BigDecimal(0));
        }
        return pressureMachineRequest;
    }
}
