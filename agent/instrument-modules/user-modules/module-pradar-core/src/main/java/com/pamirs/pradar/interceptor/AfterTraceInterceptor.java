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
package com.pamirs.pradar.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static com.pamirs.pradar.interceptor.TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS;

/**
 * 方法的 trace 埋点的开始埋点，适用于一次 trace 需要两个方法完成的场景
 * 适用于方法开始进行 trace 追踪，方法结果进行 trace 提交的场景
 * <p>
 * 例如:
 * 下面为某一 rpc 框架一次完整的生命周期调用
 * void send(String value) {
 * System.out.println("xxxxx");
 * }
 * void response(Object response) {
 * xxxx
 * }
 * <p>
 * void failure(Throwable t) {
 * xxxx
 * }
 * 则需要在 send 中开始 trace，并且在 response 和 failure 中结束 trace
 * 则在 send 中的拦截器使用{@link BeforeTraceInterceptor}
 * 在 response 和 failure 中则使用 {@link AfterTraceInterceptor}
 * <p>
 * 需要在{@link #afterFirst(Advice)}
 *
 * <p>
 * 需要注意的是如果在 send 中捕获到异常，则表明这次调用已经完成
 *
 * <p>
 * 需要追踪 test 方法的执行耗时、入参、出参等,其他场景并不适用
 *
 * <pre>
 *     实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 */
abstract class AfterTraceInterceptor extends BaseInterceptor {
    protected final static Logger LOGGER = LoggerFactory.getLogger(AfterTraceInterceptor.class);

    @Resource
    protected SimulatorConfig simulatorConfig;

    /**
     * 是否是调用端
     *
     * @return
     */
    protected boolean isClient(Advice advice) {
        return true;
    }

    /**
     * 是否是入口
     *
     * @return
     */
    protected boolean isTrace(Advice advice) {
        return false;
    }

    /**
     * 是否是trace 入口
     *
     * @param advice 切点
     * @return
     */
    private boolean isTrace0(Advice advice) {
        try {
            return isTrace(advice);
        } catch (Throwable e) {
            LOGGER.error("TraceAroundInterceptor: {} isTrace throw a exception, return default result[isTrace=false] instead.", getClass().getName(), e);
            return false;
        }
    }

    /**
     * pradar的Plugin名称，与{@link #getPluginType()} 对应,对应名称
     */
    public abstract String getPluginName();

    /**
     * pradar的Plugin类型,参见 {@link com.pamirs.pradar.MiddlewareType#TYPE_CACHE} {@link
     * com.pamirs.pradar.MiddlewareType#TYPE_DB} {@link com.pamirs.pradar.MiddlewareType#TYPE_WEB_SERVER} {@link
     * com.pamirs.pradar.MiddlewareType#TYPE_LOCAL} {@link com.pamirs.pradar.MiddlewareType#TYPE_FS} {@link
     * com.pamirs.pradar.MiddlewareType#TYPE_SEARCH}
     */
    public abstract int getPluginType();

    /**
     * 后置埋点的前置操作
     * <p>
     * 如果该方法抛出异常不会阻塞流程，异常会以日志形式记录下来，后续流程会继续走下去
     *
     * @param advice 节点对象
     */
    public abstract void afterFirst(Advice advice);

    /**
     * 后置埋点的后置操作
     * <p>
     * 如果该方法抛出异常不会阻塞流程，异常会以日志形式记录下来，后续流程会继续走下去
     * 如果在 {@link #afterTrace(Advice)} 抛出异常后该方法也会执行
     *
     * @param advice 节点对象
     */
    public abstract void afterLast(Advice advice);

    /**
     * 后置埋点
     *
     * @param advice 切点对象
     */
    public abstract SpanRecord afterTrace(Advice advice);

    /**
     * 异常时埋点的前置操作
     * <p>
     * 如果该方法抛出异常不会阻塞流程，异常会以日志形式记录下来，后续流程会继续走下去
     *
     * @param advice 切点对象
     */
    public abstract void exceptionFirst(Advice advice);

    /**
     * 异常时埋点的后置操作
     * <p>
     * 如果该方法抛出异常不会阻塞流程，异常会以日志形式记录下来，后续流程会继续走下去
     * 如果在 {@link #exceptionTrace(Advice)} 抛出异常后该方法也会执行
     *
     * @param advice 切点对象
     */
    public abstract void exceptionLast(Advice advice);

    /**
     * 异常时埋点
     *
     * @param advice 切点对象
     */
    public abstract SpanRecord exceptionTrace(Advice advice);

    /**
     * 获取需要重建的上下文
     *
     * @return 返回上下文
     */
    protected abstract Object getContext(Advice advice);

    /**
     * 获取传输 context 的转换器
     * 注意，一定需要保证该方法不能出现错误，不然可能就会导致上下文数据丢失，导致数据泄露问题
     * 该方法只有在 {@link #isClient(Advice) } 为 true 时才会被执行
     * 当该方法抛出异常时则不会中断流程，后续日志打印仍会进行，但是不会再进行调用链的传递
     *
     * @param advice 切点对象
     * @return
     */
    protected ContextTransfer getContextTransfer(Advice advice) {
        return null;
    }

    @Override
    public final void doBefore(Advice advice) throws Throwable {
        if (!simulatorConfig.getBooleanProperty("plugin." + getPluginName() + ".trace.enabled", true)) {
            return;
        }
        Object ctx = getContext(advice);
        if (ctx != null) {
            /**
             * 重建上下文成功即可调用结束上下文
             */
            Pradar.setInvokeContext(ctx);
            advice.mark(BEFORE_TRACE_SUCCESS);
        }
    }

    /**
     * client invoke 结束
     *
     * @param advice
     * @throws Throwable
     */
    private void endClientInvoke(Advice advice) throws Throwable {
        if (!advice.hasMark(BEFORE_TRACE_SUCCESS)) {
            LOGGER.debug("{} before trace not finished.", getClass().getName());
            return;
        }
        try {
            SpanRecord record = afterTrace(advice);
            if (record == null) {
                Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS, getPluginType());
                return;
            }
            if (record.getResponseSize() != 0) {
                Pradar.responseSize(record.getResponseSize());
            }
            Object response = record.getResponse();
            if (response instanceof Throwable) {
                advice.attach(response);
            }
            if (Pradar.isResponseOn()) {
                Pradar.response(response);
            }
            if (StringUtils.isNotBlank(record.getRemoteIp())) {
                Pradar.remoteIp(record.getRemoteIp());
            }
            if (StringUtils.isNotBlank(record.getPort())) {
                Pradar.remotePort(record.getPort());
            }

            if (record.getMiddlewareName() != null) {
                Pradar.middlewareName(record.getMiddlewareName());
            }

            if (record.getCallbackMsg() != null) {
                Pradar.callBack(record.getCallbackMsg());
            }

            Pradar.endClientInvoke(record.getResultCode(), getPluginType());
        } catch (Throwable e) {
            /**
             * 如果出错了，则强制将上下文提交
             */
            Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            throw e;
        } finally {
            advice.unMark(BEFORE_TRACE_SUCCESS);
        }
    }

    /**
     * 结束服务端调用
     *
     * @param advice
     * @throws Throwable
     */
    private void endServerInvoke(Advice advice) throws Throwable {
        if (!advice.hasMark(BEFORE_TRACE_SUCCESS)) {
            LOGGER.debug("{} before trace not finished.", getClass().getName());
            return;
        }
        boolean isTrace = isTrace0(advice);
        try {
            SpanRecord record = afterTrace(advice);
            if (record == null) {
                if (isTrace) {
                    Pradar.endTrace(ResultCode.INVOKE_RESULT_SUCCESS, getPluginType());
                } else {
                    Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_SUCCESS, getPluginType());
                }
                return;
            }
            if (record.getResponseSize() != 0) {
                Pradar.responseSize(record.getResponseSize());
            }
            Object response = record.getResponse();
            if (response instanceof Throwable) {
                advice.attach(response);
            }
            if (Pradar.isResponseOn()) {
                Pradar.response(response);
            }
            if (StringUtils.isNotBlank(record.getRemoteIp())) {
                Pradar.remoteIp(record.getRemoteIp());
            }
            if (StringUtils.isNotBlank(record.getPort())) {
                Pradar.remotePort(record.getPort());
            }
            if (record.getMiddlewareName() != null) {
                Pradar.middlewareName(record.getMiddlewareName());
            }

            if (record.getCallbackMsg() != null) {
                Pradar.callBack(record.getCallbackMsg());
            }

            if (isTrace) {
                Pradar.endTrace(record.getResultCode(), getPluginType());
            } else {
                Pradar.endServerInvoke(record.getResultCode(), getPluginType());
            }
        } catch (Throwable e) {
            if (isTrace) {
                Pradar.endTrace(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            } else {
                Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            }
            throw e;
        } finally {
            advice.unMark(BEFORE_TRACE_SUCCESS);
        }
    }

    @Override
    public final void doAfter(Advice advice) throws Throwable {
        if (!simulatorConfig.getBooleanProperty("plugin." + getPluginName() + ".trace.enabled", true)) {
            return;
        }
        ClusterTestUtils.validateClusterTest();
        Throwable throwable = null;
        try {
            afterFirst(advice);
        } catch (PradarException e) {
            LOGGER.error("TraceInterceptor afterFirst exec err:{}", this.getClass().getName(), e);
            throwable = e;
        } catch (PressureMeasureError e) {
            LOGGER.error("TraceInterceptor afterFirst exec err:{}", this.getClass().getName(), e);
            throwable = e;
        } catch (Throwable t) {
            LOGGER.error("TraceInterceptor afterFirst exec err:{}", this.getClass().getName(), t);
            throwable = t;
        }
        try {
            if (isClient(advice)) {
                endClientInvoke(advice);
            } else {
                endServerInvoke(advice);
            }
        } catch (PradarException e) {
            LOGGER.error("TraceInterceptor after exec err:{}", this.getClass().getName(), e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable e) {
            LOGGER.error("TraceInterceptor after exec err:{}", this.getClass().getName(), e);
            if (Pradar.isClusterTest()) {
                throw new PradarException(e);
            }
        } finally {
            try {
                afterLast(advice);
            } catch (PradarException e) {
                LOGGER.error("TraceInterceptor afterLast exec err:{}", this.getClass().getName(), e);
                throwable = e;
            } catch (PressureMeasureError e) {
                LOGGER.error("TraceInterceptor afterLast exec err:{}", this.getClass().getName(), e);
                throwable = e;
            } catch (Throwable t) {
                LOGGER.error("TraceInterceptor afterLast exec err:{}", this.getClass().getName(), t);
                throwable = t;
            }
        }
        if (throwable != null && Pradar.isClusterTest()) {
            throw throwable;
        }
    }

    @Override
    public final void doException(Advice advice) throws Throwable {
        if (!simulatorConfig.getBooleanProperty("plugin." + getPluginName() + ".trace.enabled", true)) {
            return;
        }
        Throwable throwable = null;
        try {
            exceptionFirst(advice);
        } catch (PradarException e) {
            LOGGER.error("TraceInterceptor exceptionFirst exec err:{}", this.getClass().getName(), e);
            throwable = e;
        } catch (PressureMeasureError e) {
            LOGGER.error("TraceInterceptor exceptionFirst exec err:{}", this.getClass().getName(), e);
            throwable = e;
        } catch (Throwable t) {
            LOGGER.error("TraceInterceptor exceptionFirst exec err:{}", this.getClass().getName(), t);
            throwable = t;
        }
        try {
            if (isClient(advice)) {
                endClientInvokeException(advice);
            } else {
                endServerInvokeException(advice);
            }
        } catch (PradarException e) {
            LOGGER.error("TraceInterceptor exception exec err:{}", this.getClass().getName(), e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable e) {
            LOGGER.error("TraceInterceptor exception exec err:{}", this.getClass().getName(), e);
            if (Pradar.isClusterTest()) {
                throw new PradarException(e);
            }
        } finally {
            try {
                exceptionLast(advice);
            } catch (PradarException e) {
                LOGGER.error("TraceInterceptor exceptionLast exec err:{}", this.getClass().getName(), e);
                throwable = e;
            } catch (PressureMeasureError e) {
                LOGGER.error("TraceInterceptor exceptionLast exec err:{}", this.getClass().getName(), e);
                throwable = e;
            } catch (Throwable t) {
                LOGGER.error("TraceInterceptor exceptionLast exec err:{}", this.getClass().getName(), t);
                throwable = t;
            }
        }
        if (throwable != null && Pradar.isClusterTest()) {
            throw throwable;
        }
    }

    /**
     * 结束客户端异常调用
     *
     * @param advice
     * @throws Throwable
     */
    private final void endClientInvokeException(Advice advice) throws Throwable {
        if (!advice.hasMark(BEFORE_TRACE_SUCCESS)) {
            LOGGER.debug("{} before trace not finished.", getClass().getName());
            return;
        }
        try {
            SpanRecord record = exceptionTrace(advice);
            if (record == null) {
                Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS, getPluginType());
                return;
            }
            Object response = record.getResponse();
            if (response != null && response instanceof Throwable) {
                advice.attach(response);
            }
            if (Pradar.isResponseOn()) {
                Pradar.response(response);
            }
            if (StringUtils.isNotBlank(record.getRemoteIp())) {
                Pradar.remoteIp(record.getRemoteIp());
            }

            if (StringUtils.isNotBlank(record.getPort())) {
                Pradar.remotePort(record.getPort());
            }

            if (record.getMiddlewareName() != null) {
                Pradar.middlewareName(record.getMiddlewareName());
            }
            if (record.getCallbackMsg() != null) {
                Pradar.callBack(record.getCallbackMsg());
            }
            Pradar.endClientInvoke(record.getResultCode(), getPluginType());
        } catch (Throwable e) {
            Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            throw e;
        } finally {
            advice.unMark(BEFORE_TRACE_SUCCESS);
        }
    }

    private final void endServerInvokeException(Advice advice) throws Throwable {
        if (!advice.hasMark(BEFORE_TRACE_SUCCESS)) {
            LOGGER.debug("{} before trace not finished.", getClass().getName());
            return;
        }
        boolean isTrace = isTrace0(advice);
        try {
            SpanRecord record = exceptionTrace(advice);
            if (record == null) {
                if (isTrace) {
                    Pradar.endTrace(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
                } else {
                    Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
                }
                return;
            }
            Object response = record.getResponse();
            if (response != null && response instanceof Throwable) {
                advice.attach(response);
            }

            if (Pradar.isExceptionOn()) {
                Pradar.response(response);
            }
            if (StringUtils.isNotBlank(record.getRemoteIp())) {
                Pradar.remoteIp(record.getRemoteIp());
            }

            if (StringUtils.isNotBlank(record.getPort())) {
                Pradar.remotePort(record.getPort());
            }

            if (record.getMiddlewareName() != null) {
                Pradar.middlewareName(record.getMiddlewareName());
            }
            if (record.getCallbackMsg() != null) {
                Pradar.callBack(record.getCallbackMsg());
            }
            if (isTrace) {
                Pradar.endTrace(record.getResultCode(), getPluginType());
            } else {
                Pradar.endServerInvoke(record.getResultCode(), getPluginType());
            }
        } catch (Throwable e) {
            if (isTrace) {
                Pradar.endTrace(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            } else {
                Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_UNKNOWN, getPluginType());
            }
            throw e;
        } finally {
            advice.unMark(BEFORE_TRACE_SUCCESS);
        }
    }

}

