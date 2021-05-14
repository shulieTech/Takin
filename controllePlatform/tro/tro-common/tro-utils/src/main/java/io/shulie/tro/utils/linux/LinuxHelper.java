package io.shulie.tro.utils.linux;

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

    /**
     *执行curl命令
     * @param cmds
     * @return
     */
    public static String execCurl(String[] cmds) {
        ProcessBuilder process = new ProcessBuilder(cmds);

        try {
            Process p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            return builder.toString();
        } catch (IOException var6) {
            System.out.print("error");
            var6.printStackTrace();
            return null;
        }
    }
    
    
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
