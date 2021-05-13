package io.shulie.tro.log.operator;

import io.shulie.tro.log.entity.OperationLogContext;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
public final class OperationLogContextHolder {

    private OperationLogContextHolder() { /* no instance */ }

    private final static InheritableThreadLocal<OperationLogContext> HOLDER = new InheritableThreadLocal<>();

    public static void operationType(String type) {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.setOperationType(type);
    }

    public static void addVars(String key, String value) {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.getVars().put(key, value);
    }

    public static void ignoreLog() {
        OperationLogContext operationLogContext = HOLDER.get();
        if (operationLogContext == null) {
            operationLogContext = new OperationLogContext();
            HOLDER.set(operationLogContext);
        }
        operationLogContext.setIgnore(true);
    }

    public static OperationLogContext get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
        HOLDER.set(new OperationLogContext());
    }

    public static void start() {
        OperationLogContext operationLogContext = HOLDER.get();
        operationLogContext.setStartTime(System.currentTimeMillis());
    }

    public static void end(boolean isSuccess) {
        OperationLogContext operationLogContext = HOLDER.get();
        Long endTime = System.currentTimeMillis();
        operationLogContext.setEndTime(endTime);
        operationLogContext.setSuccess(isSuccess);
        operationLogContext.setCostTime(endTime - operationLogContext.getStartTime());
    }
}
