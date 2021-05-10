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
package com.shulie.instrument.simulator.core;

import com.shulie.instrument.simulator.core.util.SimulatorStringUtils;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.lang.StringUtils;

/**
 * 仿真器内核启动器
 */
public class CoreLauncher {

    public CoreLauncher(final String descriptor,
                        final String agentJarPath,
                        final String token) throws Exception {
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        String targetJvmPid = descriptor;

        if (!isDigits(descriptor)) {
            for (VirtualMachineDescriptor vmDescriptor : VirtualMachine.list()) {
                if (vmDescriptor.displayName().contains(descriptor)) {
                    virtualMachineDescriptor = vmDescriptor;
                    break;
                }
            }
        } else {
            for (VirtualMachineDescriptor vmDescriptor : VirtualMachine.list()) {
                if (vmDescriptor.id().equals(descriptor)) {
                    virtualMachineDescriptor = vmDescriptor;
                    break;
                }
            }
        }
        // 加载agent
        attachAgent(virtualMachineDescriptor, targetJvmPid, agentJarPath, token);
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    private static boolean isDigits(final String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 内核启动程序
     *
     * @param args 参数
     *             [0] : PID
     *             [1] : agent.jar's value
     *             [2] : token
     */
    public static void main(String[] args) {
        try {

            // check args
            if (args.length != 3
                    || StringUtils.isBlank(args[0])
                    || StringUtils.isBlank(args[1])
                    || StringUtils.isBlank(args[2])) {
                throw new IllegalArgumentException("illegal args");
            }

            new CoreLauncher(args[0], args[1], args[2]);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            System.err.println("simulator load jvm failed : " + SimulatorStringUtils.getCauseMessage(t));
            System.exit(-1);
        }
    }

    // 加载Agent
    private void attachAgent(final VirtualMachineDescriptor virtualMachineDescriptor,
                             final String targetJvmPid,
                             final String agentJarPath,
                             final String config) throws Exception {

        VirtualMachine vmObj = null;
        try {
            if (virtualMachineDescriptor != null) {
                vmObj = VirtualMachine.attach(virtualMachineDescriptor);
            } else {
                if (!isDigits(targetJvmPid)) {
                    throw new IllegalArgumentException("illegal args[0], can't found a vm instance with " + targetJvmPid + " by name. and it is also not a valid digits.");
                }
                vmObj = VirtualMachine.attach(targetJvmPid);
            }
            if (vmObj != null) {
                vmObj.loadAgent(agentJarPath, config);
            }

        } finally {
            if (null != vmObj) {
                vmObj.detach();
            }
        }

    }

}
