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

import java.util.concurrent.atomic.AtomicReference;

import io.shulie.tro.utils.linux.LinuxHelper;
import org.junit.Test;

/**
 * @author 无涯
 * @Package PACKAGE_NAME
 * @date 2021/5/8 10:58 上午
 */
public class LocalThreadServiceImplTest {
    @Test
    public void createJob() {
        AtomicReference<Process> shellProcess = new AtomicReference<>();
        String installDir = "/Users/hezhongqi/shulie/engine/pressure-engine";
        String taskDir = "/Users/hezhongqi/shulie/engine";
        String job = "engine-config-84-920-9725.json";
        StringBuilder sb = new StringBuilder();
        sb.append("cd ").append(installDir).append("/bin");
        sb.append(" && ");
        sb.append("sh start.sh -t \"jmeter\" -c");
        sb.append(" \"").append(taskDir).append("/").append(job).append(" \"");
        sb.append(" -f y -d true ");
        int state = LinuxHelper.runShell(sb.toString(), 60L,
            process -> shellProcess.set(process),
            message -> {
                System.out.println("执行返回结果:" +message);
            }
        );
    }
}
