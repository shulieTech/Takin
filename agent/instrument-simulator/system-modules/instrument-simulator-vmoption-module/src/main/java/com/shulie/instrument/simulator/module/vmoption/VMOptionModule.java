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
package com.shulie.instrument.simulator.module.vmoption;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.vmoption.VmOption;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 5:18 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "vmoption", version = "1.0.0", author = "xiaobin@shulie.io", description = "虚拟机参数模块")
public class VMOptionModule extends ParamSupported implements ExtensionModule {

    @Command(value = "info", description = "查看虚拟机参数")
    public CommandResponse info(final Map<String, String> args) {
        String name = args.get("name");

        try {
            HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                    .getPlatformMXBean(HotSpotDiagnosticMXBean.class);

            if (StringUtils.isBlank(name)) {
                // show all options
                final List<VMOption> diagnosticOptions = hotSpotDiagnosticMXBean.getDiagnosticOptions();
                List<VmOption> vmOptions = new ArrayList<VmOption>();
                for (VMOption vmOption : diagnosticOptions) {
                    vmOptions.add(new VmOption(vmOption));
                }
                return CommandResponse.success(vmOptions);
            } else {
                // view the specified option
                VMOption option = hotSpotDiagnosticMXBean.getVMOption(name);
                if (option == null) {
                    return CommandResponse.failure("In order to change the system properties, you must specify the property value.");
                } else {
                    List<VmOption> vmOptions = new ArrayList<VmOption>();
                    vmOptions.add(new VmOption(option));
                    return CommandResponse.success(vmOptions);
                }
            }
        } catch (Throwable t) {
            return CommandResponse.failure(t);
        }
    }

    @Command(value = "setOption", description = "设置虚拟机参数")
    public CommandResponse setOption(final Map<String, String> args) {
        String name = args.get("name");
        String value = args.get("value");
        try {
            HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                    .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
            VMOption vmOption = hotSpotDiagnosticMXBean.getVMOption(name);
            String originValue = vmOption.getValue();

            // change vm option
            hotSpotDiagnosticMXBean.setVMOption(name, value);
            return CommandResponse.success(true);
        } catch (Throwable t) {
            return CommandResponse.failure(t);
        }
    }
}
