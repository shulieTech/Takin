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
package com.shulie.instrument.simulator.module.heapdump;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.util.RuntimeUtils;
import com.sun.management.HotSpotDiagnosticMXBean;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:55 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "heapdump", version = "1.0.0", author = "xiaobin@shulie.io", description = "内存 dump 模块")
public class HeapDumpModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(HeapDumpModule.class);

    @Resource
    private SimulatorConfig simulatorConfig;

    private ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };

    @Command(value = "dump", description = "dump 内存")
    public CommandResponse heapdump(final Map<String, String> args) {
        try {
            String dumpFile = args.get("file");
            boolean live = ParameterUtils.getBoolean(args, "live", true);

            if (dumpFile == null || dumpFile.isEmpty()) {
                String date = sdf.get().format(new Date());
                File file = File.createTempFile("heapdump-" + date + (live ? "-live" : ""), ".hprof");
                dumpFile = file.getAbsolutePath();
                file.delete();
            }
            if (JvmUtils.supportsVersion(JvmVersion.JAVA_7)) {
                run(dumpFile, live);
                return CommandResponse.success(dumpFile);
            } else {
                final String bashCommand = (new StringBuilder("nohup sh ").append(simulatorConfig.getSimulatorHome()).append("/bin/dump.sh ").append(dumpFile).append(" ").append(RuntimeUtils.getPid()).append(" ").append(live)).append(" ").append("&").toString();
                execute(bashCommand);
                if (new File(dumpFile + ".finished").exists()) {
                    new File(dumpFile + ".finished").delete();
                    return CommandResponse.success(dumpFile);
                }
            }
            return CommandResponse.failure("heapdump execute failed. please retry it later!");
        } catch (NoSuchMethodError e) {
            return CommandResponse.failure("dumpheap is not supported in this jdk version");
        } catch (NoClassDefFoundError e) {
            return CommandResponse.failure("dumpheap is not supported in this jdk version");
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }

    private static void execute(final String bashCommand) {
        BufferedReader br = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(bashCommand);
            int status = pro.waitFor();
            if (status != 0) {
                logger.error("Failed to call shell's command ");
            }
        } catch (Exception e) {
            logger.error("error {}", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //TODO ignore
                }
            }
        }
    }

    private static void run(String file, boolean live) throws IOException {
        HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        hotSpotDiagnosticMXBean.dumpHeap(file, live);
    }
}
