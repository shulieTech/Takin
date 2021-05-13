package io.shulie.tro.monitor.util;

import java.util.ArrayList;
import java.util.List;

import io.shulie.tro.monitor.content.CpuDTO;
import io.shulie.tro.monitor.content.MemDTO;
import io.shulie.tro.monitor.content.NetworkDTO;
import io.shulie.tro.monitor.content.ServerDTO;
import io.shulie.tro.monitor.content.ServerFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

/**
 * @author zhaoyong
 */
@Slf4j
public class ServerUtils {


    public static CpuDTO getCpuInfo(HardwareAbstractionLayer hal, OperatingSystem os) {
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // Wait a second...
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        CpuDTO cpuDTO = new CpuDTO();
        cpuDTO.setCpuNum(processor.getLogicalProcessorCount());
        cpuDTO.setTotal(totalCpu);
        cpuDTO.setSys(sys);
        cpuDTO.setUsed(user);
        cpuDTO.setWait(iowait);
        cpuDTO.setFree(idle);
        cpuDTO.setCpuAvgLoad(processor.getSystemLoadAverage());
        return cpuDTO;
    }

    public static MemDTO getMemInfo(HardwareAbstractionLayer hal, OperatingSystem os) {
        MemDTO memDTO = new MemDTO();
        GlobalMemory memory = hal.getMemory();
        memDTO.setTotal(memory.getTotal());
        memDTO.setUsed(memory.getTotal() - memory.getAvailable());
        memDTO.setFree(memory.getAvailable());
        memDTO.setSwapTotal(memory.getSwapTotal());
        memDTO.setSwapUsed(memory.getSwapUsed());
        return memDTO;
    }

    public static ServerDTO getServerInfo(HardwareAbstractionLayer hal, OperatingSystem os) {
        ServerDTO serverDTO = new ServerDTO();
        ComputerSystem computerSystem = hal.getComputerSystem();
        NetworkParams networkParams = os.getNetworkParams();
        serverDTO.setComputerName(networkParams.getHostName());
        serverDTO.setOsName(computerSystem.getModel());
        NetworkIF[] networkIFs = hal.getNetworkIFs();
        for (NetworkIF net : networkIFs) {
            if (net.getName() != null && net.getBytesRecv() > 0 && net.getSpeed() > 0 && net.getIPv4addr().length > 0) {
                log.debug("本机获取到的ip:{}", net.getIPv4addr()[0]);
                serverDTO.setComputerIp(net.getIPv4addr()[0]);
                NetworkDTO beforeNetworkDTO = getCurrentNetworkDTO(net.getIPv4addr()[0]);
                Util.sleep(3000);
                NetworkDTO afterNetworkDTO = getCurrentNetworkDTO(net.getIPv4addr()[0]);
                serverDTO.setReceive((afterNetworkDTO.getBytesRecv() - beforeNetworkDTO.getBytesRecv()) * 1000 / (afterNetworkDTO.getTime() - beforeNetworkDTO.getTime()));
                serverDTO.setTransmitted((afterNetworkDTO.getBytesSent() - beforeNetworkDTO.getBytesSent()) * 1000 / (afterNetworkDTO.getTime() - beforeNetworkDTO.getTime()));
            }
        }
        return serverDTO;
    }

    private static NetworkDTO getCurrentNetworkDTO(String ip) {
        NetworkDTO networkDTO = new NetworkDTO();
        networkDTO.setTime(System.currentTimeMillis());
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        NetworkIF[] fs = hal.getNetworkIFs();
        for (NetworkIF net : fs) {
            if (net.getName() != null && net.getBytesRecv() > 0 && net.getSpeed() > 0 && net.getIPv4addr().length > 0 && ip.equals(net.getIPv4addr()[0])) {
                networkDTO.setBytesRecv(net.getBytesRecv());
                networkDTO.setBytesSent(net.getBytesSent());
            }
        }
        log.info("获取网络信息="+ new JSONObject(networkDTO));
        return networkDTO;
    }

    public static ServerFileDTO getServerFileInfo(HardwareAbstractionLayer hal, OperatingSystem os) {
        ServerFileDTO serverFileDTO = new ServerFileDTO();
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        long totalSize = 0L;
        long freeSize = 0L;
        long usedSize = 0L;
        List<ServerFileDTO> children = new ArrayList<>();
        for (OSFileStore fs : fsArray) {
            if (fs.getVolume().startsWith("/dev")) {
                ServerFileDTO child = new ServerFileDTO();
                child.setTotal(fs.getTotalSpace());
                child.setFree(fs.getUsableSpace());
                child.setDirName(fs.getName());
                child.setUsed(fs.getTotalSpace() - fs.getUsableSpace());
                if (fs.getTotalSpace() != 0) {
                    child.setUsage(fs.getUsableSpace() * 1.0 / fs.getTotalSpace());
                }
                children.add(child);
                totalSize = totalSize + child.getTotal();
                freeSize = freeSize + child.getFree();
                usedSize = usedSize + child.getUsed();
            }
        }
        log.info("磁盘" + FormatUtil.formatBytes(totalSize));
        serverFileDTO.setIoUsage(IoUsage.getInstance().get());
        serverFileDTO.setTotal(totalSize);
        serverFileDTO.setFree(freeSize);
        serverFileDTO.setUsed(usedSize);
        if (totalSize != 0) {
            serverFileDTO.setUsage(usedSize * 1.0 / totalSize);
        }
        serverFileDTO.setChildren(children);
        return serverFileDTO;
    }
}
