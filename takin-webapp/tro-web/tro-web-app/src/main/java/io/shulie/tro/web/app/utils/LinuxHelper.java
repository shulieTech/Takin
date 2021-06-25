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

package io.shulie.tro.web.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LinuxUtil
 * @Description
 * @Author qianshui
 * @Date 2020/4/18 下午4:14
 */
public class LinuxHelper {

    public static Boolean executeLinuxCmd(String cmd) {
        BufferedReader read = null;
        try {
            Process pro = Runtime.getRuntime().exec(cmd);
            pro.waitFor();
            read = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            if(read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 执行改变shell脚本权限命令
     *
     * @param shellPath shell脚本路径
     * @return
     */
    public static boolean runChmod(String shellPath) throws Exception {
        // 添加shell的执行权限
        String chmod = "chmod +x " + shellPath;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(chmod);
            int waitFor = process.waitFor();
            if (waitFor != 0) {
                // 执行出现异常
                System.out.println("改变Shell脚本执行权限发生异常");
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return true;
    }



    public static int runShell(String commands, Long timeout, final Callback callback, final Comm comm) {
        return run(timeout,callback,comm,commands);
    }

    private static int run(Long timeout, final Callback callback, final Comm comm,String commands) {
        int status = -1;
        Process process = null;
        BufferedReader in = null;
        try {
            process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", commands});
            if (callback != null) {
                callback.created(process);
            }
            in  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String lineTxt;
            while ((lineTxt = in.readLine()) != null) {
                comm.onLine(lineTxt + "\n");
                System.out.println(3);
            }
            if (timeout == null || timeout <= 0) {
                status = process.waitFor();
            } else {
                if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
                    throw new RuntimeException(String.format("Command run timeout, timeout: %s, command: %s", timeout, commands));
                } else {
                    status = process.exitValue();
                }
            }
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (IOException | InterruptedException e) {
            comm.onLine(e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("关闭输出流发生异常");
                }
            }
        }
        return status;
    }
    public interface Comm {
        void onLine(String message);
    }

    public interface Callback {
        void created(Process process);
    }
}
