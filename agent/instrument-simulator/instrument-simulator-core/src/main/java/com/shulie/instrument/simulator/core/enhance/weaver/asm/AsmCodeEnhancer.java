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
package com.shulie.instrument.simulator.core.enhance.weaver.asm;

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.core.enhance.weaver.CodeLock;
import com.shulie.instrument.simulator.core.util.SimulatorStringUtils;
import com.shulie.instrument.simulator.message.Result;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.ArrayUtils.contains;


/**
 * Asm 代码增强器
 * 必须有 BEFORE/RETURN/THROWS
 * 其他事件则是可选的，不管事件的指定，在代码织入时强制执行此逻辑
 */
public class AsmCodeEnhancer extends ClassVisitor implements Opcodes, AsmTypes, AsmMethods {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String namespace;
    private final String targetJavaClassName;
    private final Map<String/*BehaviorStructure#getSignCode()*/, Set<BuildingForListeners>> signCodes;

    public AsmCodeEnhancer(final int api,
                           final ClassVisitor cv,
                           final String namespace,
                           final String targetClassInternalName,
                           final Map<String/*BehaviorStructure#getSignCode()*/, Set<BuildingForListeners>> signCodes) {
        super(api, cv);
        this.namespace = namespace;
        this.targetJavaClassName = SimulatorStringUtils.toJavaClassName(targetClassInternalName);
        this.signCodes = signCodes;

    }

    /**
     * 判断监听器是否开启 call 事件
     *
     * @param listeners
     * @return
     */
    private boolean isCallEnable(BuildingForListeners listeners) {
        return hasCallBefore(listeners) || hasCallReturn(listeners) || hasCallThrows(listeners);
    }

    /**
     * 判断监听器是否开启 call throws 事件
     *
     * @param listeners
     * @return
     */
    private boolean hasCallThrows(BuildingForListeners listeners) {
        return contains(listeners.getEventTypes(), EventType.CALL_THROWS);
    }

    /**
     * 判断监听器是否开启 call return 事件
     *
     * @param listeners
     * @return
     */
    private boolean hasCallReturn(BuildingForListeners listeners) {
        return contains(listeners.getEventTypes(), EventType.CALL_RETURN);
    }

    /**
     * 判断监听器是否开启 call before 事件
     *
     * @param listeners
     * @return
     */
    private boolean hasCallBefore(BuildingForListeners listeners) {
        return contains(listeners.getEventTypes(), EventType.CALL_BEFORE);
    }

    /**
     * 判断监听器是否开启行调用事件
     *
     * @param listeners
     * @return
     */
    private boolean isLineEnable(BuildingForListeners listeners) {
        return contains(listeners.getEventTypes(), EventType.LINE);
    }

    /**
     * 判断当前的行为标识是否匹配需要增强的标识
     *
     * @param signCode
     * @return
     */
    private boolean isMatchedBehavior(final String signCode) {
        return signCodes.containsKey(signCode);
    }

    /**
     * 获取当前行为标识对应的监听器列表
     *
     * @param signCode 方法标识
     * @return 监听器列表
     */
    private Set<BuildingForListeners> getBuildingForListeners(final String signCode) {
        return signCodes.get(signCode);
    }

    /**
     * 根据行为名称和方法描述获取方法的标识符
     *
     * @param name 行为名称
     * @param desc 行为描述
     * @return
     */
    private String getBehaviorSignCode(final String name,
                                       final String desc) {
        final StringBuilder sb = new StringBuilder(256).append(targetJavaClassName).append("#").append(name).append("(");

        final Type[] methodTypes = Type.getMethodType(desc).getArgumentTypes();
        if (methodTypes.length != 0) {
            sb.append(methodTypes[0].getClassName());
            for (int i = 1; i < methodTypes.length; i++) {
                sb.append(",").append(methodTypes[i].getClassName());
            }
        }

        return sb.append(")").toString();
    }

    /**
     * 是否抛出异常返回(通过字节码判断)
     *
     * @param opcode 操作码
     * @return true:以抛异常形式返回 / false:非抛异常形式返回(return)
     */
    private boolean isThrow(int opcode) {
        return opcode == ATHROW;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        final String signCode = getBehaviorSignCode(name, desc);
        final boolean matchedBehavior = isMatchedBehavior(signCode);
        /**
         * 没匹配上或者是非构造方法
         */
        if (!matchedBehavior) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: non-rewrite method {} ;",
                        signCode
                );
            }
            return mv;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: rewrite method {};",
                    signCode
            );
        }

        MethodVisitor methodVisitor = mv;
        for (BuildingForListeners buildingForListeners : getBuildingForListeners(signCode)) {
            final int listenerId = buildingForListeners.getListenerId();
            final String listenerClassName = buildingForListeners.getListeners().getClassName();
            final boolean isCallEnable = isCallEnable(buildingForListeners);
            final boolean isLineEnable = isLineEnable(buildingForListeners);
            final boolean hasCallBefore = hasCallBefore(buildingForListeners);
            final boolean hasCallThrows = hasCallThrows(buildingForListeners);
            final boolean hasCallReturn = hasCallReturn(buildingForListeners);
            methodVisitor = new ReWriteMethod(api, new JSRInlinerAdapter(methodVisitor, access, name, desc, signature, exceptions), access, name, desc) {

                private final Label beginLabel = new Label();
                private final Label endLabel = new Label();
                private final Label startCatchBlock = new Label();
                private final Label endCatchBlock = new Label();
                private int newlocal = -1;

                // 用来标记一个方法是否已经进入
                // JVM中的构造函数非常特殊，super();this();是在构造函数方法体执行之外进行，如果在这个之前进行了任何的流程改变操作
                // 将会被JVM加载类的时候判定校验失败，导致类加载出错
                // 所以这里需要用一个标记为告知后续的代码编织，绕开super()和this()
                private boolean isMethodEnter = false;

                // 代码锁
                private final CodeLock codeLockForTracing = new AsmCallCodeLock(this);

                /**
                 * 流程控制
                 */
                private void processControl() {
                    final Label finishLabel = new Label();
                    final Label returnLabel = new Label();
                    final Label throwsLabel = new Label();
                    dup();
                    visitFieldInsn(GETFIELD, ASM_TYPE_MESSAGER_RESULT, "state", ASM_TYPE_INT);
                    dup();
                    push(Result.RESULT_STATE_RETURN);
                    ifICmp(EQ, returnLabel);
                    push(Result.RESULT_STATE_THROWS);
                    ifICmp(EQ, throwsLabel);
                    goTo(finishLabel);
                    mark(returnLabel);
                    pop();
                    visitFieldInsn(GETFIELD, ASM_TYPE_MESSAGER_RESULT, "result", ASM_TYPE_OBJECT);
                    checkCastReturn(Type.getReturnType(desc));
                    goTo(finishLabel);
                    mark(throwsLabel);
                    visitFieldInsn(GETFIELD, ASM_TYPE_MESSAGER_RESULT, "result", ASM_TYPE_OBJECT);
                    checkCast(ASM_TYPE_THROWABLE);
                    throwException();
                    mark(finishLabel);
                    pop();
                }

                @Override
                protected void onMethodEnter() {
                    codeLockForTracing.lock(new CodeLock.Block() {
                        @Override
                        public void code() {
                            mark(beginLabel);
                            loadArgArray();
                            dup();
                            push(namespace);
                            push(listenerId);
                            push(listenerClassName);
                            push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                            push(name);
                            push(desc);
                            loadThisOrPushNullIsStatic();
                            invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_BEFORE);
                            swap();
                            storeArgArray();
                            pop();
                            processControl();
                            isMethodEnter = true;
                        }
                    });
                }

                /**
                 * 加载返回值
                 * @param opcode 操作吗
                 */
                private void loadReturn(int opcode) {
                    switch (opcode) {

                        case RETURN: {
                            pushNull();
                            break;
                        }

                        case ARETURN: {
                            dup();
                            break;
                        }

                        case LRETURN:
                        case DRETURN: {
                            dup2();
                            box(Type.getReturnType(methodDesc));
                            break;
                        }

                        default: {
                            dup();
                            box(Type.getReturnType(methodDesc));
                            break;
                        }

                    }
                }

                @Override
                protected void onMethodExit(final int opcode) {
                    if (!isThrow(opcode)) {
                        codeLockForTracing.lock(new CodeLock.Block() {
                            @Override
                            public void code() {
                                loadReturn(opcode);
                                push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                                push(namespace);
                                push(listenerId);
                                invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_RETURN);
                                processControl();
                            }
                        });
                    }
                }

                @Override
                public void visitMaxs(int maxStack, int maxLocals) {
                    mark(endLabel);
                    mv.visitLabel(startCatchBlock);
                    visitTryCatchBlock(beginLabel, endLabel, startCatchBlock, ASM_TYPE_THROWABLE.getInternalName());

                    codeLockForTracing.lock(new CodeLock.Block() {
                        @Override
                        public void code() {
                            newlocal = newLocal(ASM_TYPE_THROWABLE);
                            storeLocal(newlocal);
                            loadLocal(newlocal);
                            push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                            push(namespace);
                            push(listenerId);
                            invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_THROWS);
                            processControl();
                            loadLocal(newlocal);
                        }
                    });

                    throwException();
                    mv.visitLabel(endCatchBlock);
                    super.visitMaxs(maxStack, maxLocals);
                }

                // 用于tracing的当前行号
                private int tracingCurrentLineNumber = -1;

                @Override
                public void visitLineNumber(final int lineNumber, Label label) {
                    if (isMethodEnter && isLineEnable) {
                        codeLockForTracing.lock(new CodeLock.Block() {
                            @Override
                            public void code() {
                                push(lineNumber);
                                push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                                push(namespace);
                                push(listenerId);
                                invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_LINE);
                            }
                        });
                    }
                    super.visitLineNumber(lineNumber, label);
                    this.tracingCurrentLineNumber = lineNumber;
                }

                @Override
                public void visitInsn(int opcode) {
                    super.visitInsn(opcode);
                    codeLockForTracing.code(opcode);
                }

                @Override
                public void visitMethodInsn(final int opcode,
                                            final String owner,
                                            final String name,
                                            final String desc,
                                            final boolean isInterface) {

                    // 如果CALL事件没有启用，则不需要对CALL进行增强
                    // 如果正在CALL的方法来自于Simulator本身，则不需要进行追踪
                    if (!isMethodEnter || !isCallEnable || codeLockForTracing.isLock()) {
                        super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                        return;
                    }

                    if (hasCallBefore) {
                        // 方法调用前通知
                        codeLockForTracing.lock(new CodeLock.Block() {
                            @Override
                            public void code() {
                                push(tracingCurrentLineNumber);
                                push(isInterface);
                                push(SimulatorStringUtils.toJavaClassName(owner));
                                push(name);
                                push(desc);
                                push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                                push(namespace);
                                push(listenerId);
                                invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_CALL_BEFORE);
                            }
                        });
                    }

                    // 如果没有CALL_THROWS事件,其实是可以不用对方法调用进行try...catch
                    // 这样可以节省大量的字节码
                    if (!hasCallThrows) {
                        super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                        codeLockForTracing.lock(new CodeLock.Block() {
                            @Override
                            public void code() {
                                push(isInterface);
                                push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                                push(namespace);
                                push(listenerId);
                                invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_CALL_RETURN);
                            }
                        });
                        return;
                    }


                    // 这里是需要处理拥有CALL_THROWS事件的场景
                    final Label tracingBeginLabel = new Label();
                    final Label tracingEndLabel = new Label();
                    final Label tracingFinallyLabel = new Label();

                    // try
                    // {

                    mark(tracingBeginLabel);
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                    mark(tracingEndLabel);

                    if (hasCallReturn) {
                        // 方法调用后通知
                        codeLockForTracing.lock(new CodeLock.Block() {
                            @Override
                            public void code() {
                                push(isInterface);
                                push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                                push(namespace);
                                push(listenerId);
                                invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_CALL_RETURN);
                            }
                        });
                    }
                    goTo(tracingFinallyLabel);

                    // }
                    // catch
                    // {

                    catchException(tracingBeginLabel, tracingEndLabel, ASM_TYPE_THROWABLE);
                    codeLockForTracing.lock(new CodeLock.Block() {
                        @Override
                        public void code() {
                            dup();
                            push(isInterface);
                            push(Type.getObjectType(targetJavaClassName.replace('.', '/')));
                            push(namespace);
                            push(listenerId);
                            invokeStatic(ASM_TYPE_MESSAGER, MESSAGER_INVOKE_ON_CALL_THROWS);
                        }
                    });

                    throwException();

                    // }
                    // finally
                    // {
                    mark(tracingFinallyLabel);
                    // }

                }

                // 用于try-catch的重排序
                // 目的是让call的try...catch能在exceptions tables排在前边
                private final ArrayList<AsmTryCatchBlock> asmTryCatchBlocks = new ArrayList<AsmTryCatchBlock>();

                @Override
                public void visitTryCatchBlock(Label start, Label end, Label handler, String eventType) {
                    asmTryCatchBlocks.add(new AsmTryCatchBlock(start, end, handler, eventType));
                }


                @Override
                public void visitEnd() {
                    for (AsmTryCatchBlock tcb : asmTryCatchBlocks) {
                        super.visitTryCatchBlock(tcb.start, tcb.end, tcb.handler, tcb.eventType);
                    }
                    super.visitLocalVariable("t", ASM_TYPE_THROWABLE.getDescriptor(), null, startCatchBlock, endCatchBlock, newlocal);
                    super.visitEnd();
                }

            };
        }
        return methodVisitor;

    }
}
