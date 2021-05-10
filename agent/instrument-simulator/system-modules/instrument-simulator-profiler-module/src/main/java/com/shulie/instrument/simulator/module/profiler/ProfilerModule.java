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
package com.shulie.instrument.simulator.module.profiler;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.module.model.profiler.ProfilerModel;
import com.shulie.instrument.simulator.module.profiler.utils.LibUtils;
import com.shulie.instrument.simulator.module.profiler.utils.OSUtils;
import one.profiler.AsyncProfiler;
import one.profiler.Counter;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 添加 async-profiler 相关的命令
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/9 2:33 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "profiler", version = "1.0.0", author = "xiaobin@shulie.io", description = "profiler 模块")
public class ProfilerModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(ProfilerModule.class);

    private static String libPath;
    private static AsyncProfiler profiler = null;

    private ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };

    static {
        libPath = LibUtils.getLibPath();
    }

    private String executeArgs(ProfilerAction action, Map<String, String> args) {
        StringBuilder sb = new StringBuilder();

        // start - start profiling
        // resume - start or resume profiling without resetting collected data
        // stop - stop profiling
        sb.append(action).append(',');

        String event = args.get("event");
        if (event != null) {
            sb.append("event=").append(event).append(',');
        }

        String file = args.get("file");
        if (file != null) {
            sb.append("file=").append(file).append(',');
        }

        Long interval = getLongParameter(args, "interval");
        if (interval != null) {
            sb.append("interval=").append(interval).append(',');
        }

        Long framebuf = getLongParameter(args, "interval");
        if (framebuf != null) {
            sb.append("framebuf=").append(framebuf).append(',');
        }

        boolean threads = getBooleanParameter(args, "threads");
        if (threads) {
            sb.append("threads").append(',');
        }

        boolean allkernel = getBooleanParameter(args, "allkernel");
        if (allkernel) {
            sb.append("allkernel").append(',');
        }

        boolean alluser = getBooleanParameter(args, "alluser");
        if (alluser) {
            sb.append("alluser").append(',');
        }

        List<String> includes = getListParameter(args, "includes", ",");
        if (includes != null) {
            for (String include : includes) {
                sb.append("include=").append(include).append(',');
            }
        }

        List<String> excludes = getListParameter(args, "excludes", ",");
        if (excludes != null) {
            for (String exclude : excludes) {
                sb.append("exclude=").append(exclude).append(',');
            }
        }

        return sb.toString();
    }

    private static String execute(AsyncProfiler asyncProfiler, String arg)
            throws IllegalArgumentException, IOException {
        String result = asyncProfiler.execute(arg);
        if (!result.endsWith("\n")) {
            result += "\n";
        }
        return result;
    }

    private AsyncProfiler profilerInstance(String action, String actionArg) {
        if (profiler != null) {
            return profiler;
        }

        // try to load from special path
        if (ProfilerAction.load.toString().equals(action)) {
            profiler = AsyncProfiler.getInstance(actionArg);
        }

        if (libPath != null) {
            // load from arthas directory
            profiler = AsyncProfiler.getInstance(libPath);
        } else {
            if (OSUtils.isLinux() || OSUtils.isMac()) {
                throw new IllegalStateException("Can not find libasyncProfiler so, please check the arthas directory.");
            } else {
                throw new IllegalStateException("Current OS do not support AsyncProfiler, Only support Linux/Mac.");
            }
        }

        return profiler;
    }


    /**
     * https://github.com/jvm-profiling-tools/async-profiler/blob/v1.8.1/src/arguments.cpp#L50
     */
    public enum ProfilerAction {
        execute, start, stop, resume, list, version, status, load,

        dumpCollapsed, dumpFlat, dumpTraces, getSamples,

        actions
    }

    private Set<String> actions() {
        Set<String> values = new HashSet<String>();
        for (ProfilerAction action : ProfilerAction.values()) {
            values.add(action.toString());
        }
        return values;
    }

    private ProfilerModel createProfilerModel(String result, String action, String actionArg) {
        ProfilerModel profilerModel = new ProfilerModel();
        profilerModel.setAction(action);
        profilerModel.setActionArg(actionArg);
        profilerModel.setExecuteResult(result);
        return profilerModel;
    }

    private String outputFile(String file, String format) throws IOException {
        if (file == null) {
            String date = sdf.get().format(new Date());
            file = File.createTempFile("async-profiler-" + date, "." + format).getAbsolutePath();
        }
        return file;
    }

    private ProfilerModel processStop(AsyncProfiler asyncProfiler, Map<String, String> args, String file, String format, String action, String actionArg) throws IOException {
        String outputFile = outputFile(file, format);
        String executeArgs = executeArgs(ProfilerAction.stop, args);
        String result = execute(asyncProfiler, executeArgs);

        ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
        profilerModel.setOutputFile(outputFile);
        return profilerModel;
    }

    private String getFile(Map<String, String> args) throws IOException {
        String file = getParameter(args, "file");
        final String format = getParameter(args, "format");
        if (file == null && "jfr".equals(format)) {
            file = outputFile(file, format);
        }
        return file;
    }

    @Command(value = "profiler", description = "执行 async-profiler")
    public CommandResponse<ProfilerModel> profiler(final Map<String, String> args) {
        try {
            final String action = getParameter(args, "action");
            ProfilerAction profilerAction = ProfilerAction.valueOf(action);

            if (ProfilerAction.actions.equals(profilerAction)) {
                return CommandResponse.success(new ProfilerModel(actions()));
            }

            final String actionArg = getParameter(args, "actionArg");
            final AsyncProfiler asyncProfiler = this.profilerInstance(action, actionArg);

            if (ProfilerAction.execute.equals(profilerAction)) {
                if (actionArg == null) {
                    return CommandResponse.failure("actionArg can not be empty.");
                }
                String result = execute(asyncProfiler, actionArg);
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.start.equals(profilerAction)) {
                //jfr录制，必须在start的时候就指定文件路径
                final String file = getFile(args);
                final String format = getParameter(args, "format");
                String executeArgs = executeArgs(ProfilerAction.start, args);
                String result = execute(asyncProfiler, executeArgs);
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);

                Long duration = getLongParameter(args, "duration");
                if (duration != null) {
                    final String outputFile = outputFile(file, format);
                    profilerModel.setOutputFile(outputFile);
                    profilerModel.setDuration(duration);

                    // 延时执行stop
                    ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(new Runnable() {
                        @Override
                        public void run() {
                            //在异步线程执行，profiler命令已经结束，不能输出到客户端
                            try {
                                logger.info("stopping profiler ...");
                                ProfilerModel model = processStop(asyncProfiler, args, file, format, action, actionArg);
                                logger.info("profiler output file: " + model.getOutputFile());
                                logger.info("stop profiler successfully.");
                            } catch (Throwable e) {
                                logger.error("stop profiler failure", e);
                            }
                        }
                    }, duration, TimeUnit.SECONDS);
                }
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.stop.equals(profilerAction)) {
                final String file = getFile(args);
                final String format = getParameter(args, "format");
                ProfilerModel profilerModel = processStop(asyncProfiler, args, file, format, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.resume.equals(profilerAction)) {
                String executeArgs = executeArgs(ProfilerAction.resume, args);
                String result = execute(asyncProfiler, executeArgs);
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.list.equals(profilerAction)) {
                String result = asyncProfiler.execute("list");
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.version.equals(profilerAction)) {
                String result = asyncProfiler.execute("version");
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.status.equals(profilerAction)) {
                String result = asyncProfiler.execute("status");
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.dumpCollapsed.equals(profilerAction)) {
                String actionArgs = actionArg;
                if (actionArgs == null) {
                    actionArgs = "TOTAL";
                }
                actionArgs = actionArgs.toUpperCase();
                if ("TOTAL".equals(actionArg) || "SAMPLES".equals(actionArgs)) {
                    String result = asyncProfiler.dumpCollapsed(Counter.valueOf(actionArgs));
                    ProfilerModel profilerModel = createProfilerModel(result, action, actionArgs);
                    return CommandResponse.success(profilerModel);
                } else {
                    return CommandResponse.failure("ERROR: dumpCollapsed argument should be TOTAL or SAMPLES. ");
                }
            } else if (ProfilerAction.dumpFlat.equals(profilerAction)) {
                int maxMethods = 0;
                if (actionArg != null) {
                    maxMethods = Integer.valueOf(actionArg);
                }
                String result = asyncProfiler.dumpFlat(maxMethods);
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.dumpTraces.equals(profilerAction)) {
                int maxTraces = 0;
                if (actionArg != null) {
                    maxTraces = Integer.valueOf(actionArg);
                }
                String result = asyncProfiler.dumpTraces(maxTraces);
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            } else if (ProfilerAction.getSamples.equals(profilerAction)) {
                String result = "" + asyncProfiler.getSamples() + "\n";
                ProfilerModel profilerModel = createProfilerModel(result, action, actionArg);
                return CommandResponse.success(profilerModel);
            }
            return CommandResponse.failure("ERROR: unsupported action :" + profilerAction);
        } catch (Throwable e) {
            logger.error("AsyncProfiler error", e);
            return CommandResponse.failure(e);
        }

    }
}
