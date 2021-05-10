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
package com.shulie.instrument.simulator.api.util;

import com.shulie.instrument.simulator.api.filter.Filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 行为描述
 * <p>
 * 这个类用于将{@link Constructor}和{@link Method}提取对应的描述信息。
 * 行为描述用于定位一个类中的方法
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class BehaviorDescriptor {

    private final Type eventType;

    /**
     * 构造函数的行为描述
     *
     * @param constructor 构造函数
     */
    public BehaviorDescriptor(final Constructor<?> constructor) {
        this.eventType = Type.getType(constructor);
    }

    /**
     * 普通方法的行为描述
     *
     * @param method 方法
     */
    public BehaviorDescriptor(final Method method) {
        this.eventType = Type.getType(method);
    }

    /**
     * 获取行为描述
     *
     * @return 行为描述
     */
    public String getDescriptor() {
        return eventType.getDescriptor();
    }


    /**
     * A Java field or method eventType. This class can be used to make it easier to
     * manipulate eventType and method descriptors.
     * <p>
     * 这段代码来自ASM框架的{@code org.objectweb.asm.Type}，
     * 这里直接引用ASM的代码是因为之前{@link Filter}接口把方法的DESC信息直接放出了，
     * 没有这个类无法完成方法描述信息的解析，但又不想因为这个字段而让API引入一个庞大的ASM，
     * 所以只好将Type的相关代码扣出，善哉善哉，南无阿弥陀佛...API设计不好害死人...
     * </p>
     */
    private static class Type {

        /**
         * The sort of the <tt>void</tt> eventType.
         */
        // public
        private static final int VOID = 0;

        /**
         * The sort of the <tt>boolean</tt> eventType.
         */
        // public
        private static final int BOOLEAN = 1;

        /**
         * The sort of the <tt>char</tt> eventType.
         */
        // public
        private static final int CHAR = 2;

        /**
         * The sort of the <tt>byte</tt> eventType.
         */
        // public
        private static final int BYTE = 3;

        /**
         * The sort of the <tt>short</tt> eventType.
         */
        // public
        private static final int SHORT = 4;

        /**
         * The sort of the <tt>int</tt> eventType.
         */
        // public
        private static final int INT = 5;

        /**
         * The sort of the <tt>float</tt> eventType.
         */
        // public
        private static final int FLOAT = 6;

        /**
         * The sort of the <tt>long</tt> eventType.
         */
        // public
        private static final int LONG = 7;

        /**
         * The sort of the <tt>double</tt> eventType.
         */
        // public
        private static final int DOUBLE = 8;

        /**
         * The sort of array reference types.
         */
        // public
        private static final int ARRAY = 9;

        /**
         * The sort of object reference types.
         */
        // public
        private static final int OBJECT = 10;

        /**
         * The sort of method types.
         */
        // public
        private static final int METHOD = 11;

        /**
         * The <tt>void</tt> eventType.
         */
        // public
        private static final Type VOID_TYPE = new Type(VOID, null, ('V' << 24)
                | (5 << 16) | (0 << 8) | 0, 1);

        /**
         * The <tt>boolean</tt> eventType.
         */
        // public
        private static final Type BOOLEAN_TYPE = new Type(BOOLEAN, null, ('Z' << 24)
                | (0 << 16) | (5 << 8) | 1, 1);

        /**
         * The <tt>char</tt> eventType.
         */
        // public
        private static final Type CHAR_TYPE = new Type(CHAR, null, ('C' << 24)
                | (0 << 16) | (6 << 8) | 1, 1);

        /**
         * The <tt>byte</tt> eventType.
         */
        // public
        private static final Type BYTE_TYPE = new Type(BYTE, null, ('B' << 24)
                | (0 << 16) | (5 << 8) | 1, 1);

        /**
         * The <tt>short</tt> eventType.
         */
        // public
        private static final Type SHORT_TYPE = new Type(SHORT, null, ('S' << 24)
                | (0 << 16) | (7 << 8) | 1, 1);

        /**
         * The <tt>int</tt> eventType.
         */
        // public
        private static final Type INT_TYPE = new Type(INT, null, ('I' << 24)
                | (0 << 16) | (0 << 8) | 1, 1);

        /**
         * The <tt>float</tt> eventType.
         */
        // public
        private static final Type FLOAT_TYPE = new Type(FLOAT, null, ('F' << 24)
                | (2 << 16) | (2 << 8) | 1, 1);

        /**
         * The <tt>long</tt> eventType.
         */
        // public
        private static final Type LONG_TYPE = new Type(LONG, null, ('J' << 24)
                | (1 << 16) | (1 << 8) | 2, 1);

        /**
         * The <tt>double</tt> eventType.
         */
        // public
        private static final Type DOUBLE_TYPE = new Type(DOUBLE, null, ('D' << 24)
                | (3 << 16) | (3 << 8) | 2, 1);

        // ------------------------------------------------------------------------
        // Fields
        // ------------------------------------------------------------------------

        /**
         * The sort of this Java eventType.
         */
        private final int sort;

        /**
         * A buffer containing the internal name of this Java eventType. This field is
         * only used for reference types.
         */
        private final char[] buf;

        /**
         * The offset of the internal name of this Java eventType in {@link #buf buf} or,
         * for primitive types, the size, descriptor and getOpcode offsets for this
         * eventType (byte 0 contains the size, byte 1 the descriptor, byte 2 the offset
         * for IALOAD or IASTORE, byte 3 the offset for all other instructions).
         */
        private final int off;

        /**
         * The length of the internal name of this Java eventType.
         */
        private final int len;

        // ------------------------------------------------------------------------
        // Constructors
        // ------------------------------------------------------------------------

        /**
         * Constructs a reference eventType.
         *
         * @param sort the sort of the reference eventType to be constructed.
         * @param buf  a buffer containing the descriptor of the previous eventType.
         * @param off  the offset of this descriptor in the previous buffer.
         * @param len  the length of this descriptor.
         */
        private Type(final int sort, final char[] buf, final int off, final int len) {
            this.sort = sort;
            this.buf = buf;
            this.off = off;
            this.len = len;
        }

        /**
         * Returns the Java eventType corresponding to the given eventType descriptor.
         *
         * @param typeDescriptor a field or method eventType descriptor.
         * @return the Java eventType corresponding to the given eventType descriptor.
         */
        // public
        private static Type getType(final String typeDescriptor) {
            return getType(typeDescriptor.toCharArray(), 0);
        }

        /**
         * Returns the Java method eventType corresponding to the given constructor.
         *
         * @param c a {@link Constructor Constructor} object.
         * @return the Java method eventType corresponding to the given constructor.
         */
        public static Type getType(final Constructor<?> c) {
            return getType(getConstructorDescriptor(c));
        }

        /**
         * Returns the Java method eventType corresponding to the given method.
         *
         * @param m a {@link Method Method} object.
         * @return the Java method eventType corresponding to the given method.
         */
        public static Type getType(final Method m) {
            return getType(getMethodDescriptor(m));
        }

        /**
         * Returns the Java eventType corresponding to the given eventType descriptor. For
         * method descriptors, buf is supposed to contain nothing more than the
         * descriptor itself.
         *
         * @param buf a buffer containing a eventType descriptor.
         * @param off the offset of this descriptor in the previous buffer.
         * @return the Java eventType corresponding to the given eventType descriptor.
         */
        private static Type getType(final char[] buf, final int off) {
            int len;
            switch (buf[off]) {
                case 'V':
                    return VOID_TYPE;
                case 'Z':
                    return BOOLEAN_TYPE;
                case 'C':
                    return CHAR_TYPE;
                case 'B':
                    return BYTE_TYPE;
                case 'S':
                    return SHORT_TYPE;
                case 'I':
                    return INT_TYPE;
                case 'F':
                    return FLOAT_TYPE;
                case 'J':
                    return LONG_TYPE;
                case 'D':
                    return DOUBLE_TYPE;
                case '[':
                    len = 1;
                    while (buf[off + len] == '[') {
                        ++len;
                    }
                    if (buf[off + len] == 'L') {
                        ++len;
                        while (buf[off + len] != ';') {
                            ++len;
                        }
                    }
                    return new Type(ARRAY, buf, off, len + 1);
                case 'L':
                    len = 1;
                    while (buf[off + len] != ';') {
                        ++len;
                    }
                    return new Type(OBJECT, buf, off + 1, len - 1);
                // case '(':
                default:
                    return new Type(METHOD, buf, off, buf.length - off);
            }
        }

        // ------------------------------------------------------------------------
        // Conversion to eventType descriptors
        // ------------------------------------------------------------------------

        /**
         * Returns the descriptor corresponding to this Java eventType.
         *
         * @return the descriptor corresponding to this Java eventType.
         */
        public String getDescriptor() {
            StringBuilder buf = new StringBuilder();
            getDescriptor(buf);
            return buf.toString();
        }

        /**
         * Appends the descriptor corresponding to this Java eventType to the given
         * string buffer.
         *
         * @param buf the string buffer to which the descriptor must be appended.
         */
        private void getDescriptor(final StringBuilder buf) {
            if (this.buf == null) {
                // descriptor is in byte 3 of 'off' for primitive types (buf ==
                // null)
                buf.append((char) ((off & 0xFF000000) >>> 24));
            } else if (sort == OBJECT) {
                buf.append('L');
                buf.append(this.buf, off, len);
                buf.append(';');
            } else { // sort == ARRAY || sort == METHOD
                buf.append(this.buf, off, len);
            }
        }

        // ------------------------------------------------------------------------
        // Direct conversion from classes to eventType descriptors,
        // without intermediate Type objects
        // ------------------------------------------------------------------------

        /**
         * Returns the descriptor corresponding to the given constructor.
         *
         * @param c a {@link Constructor Constructor} object.
         * @return the descriptor of the given constructor.
         */
        // public
        private static String getConstructorDescriptor(final Constructor<?> c) {
            Class<?>[] parameters = c.getParameterTypes();
            StringBuilder buf = new StringBuilder();
            buf.append('(');
            for (final Class<?> parameter : parameters) {
                getDescriptor(buf, parameter);
            }
            return buf.append(")V").toString();
        }

        /**
         * Returns the descriptor corresponding to the given method.
         *
         * @param m a {@link Method Method} object.
         * @return the descriptor of the given method.
         */
        // public
        private static String getMethodDescriptor(final Method m) {
            Class<?>[] parameters = m.getParameterTypes();
            StringBuilder buf = new StringBuilder();
            buf.append('(');
            for (final Class<?> parameter : parameters) {
                getDescriptor(buf, parameter);
            }
            buf.append(')');
            getDescriptor(buf, m.getReturnType());
            return buf.toString();
        }

        /**
         * Appends the descriptor of the given class to the given string buffer.
         *
         * @param buf the string buffer to which the descriptor must be appended.
         * @param c   the class whose descriptor must be computed.
         */
        private static void getDescriptor(final StringBuilder buf, final Class<?> c) {
            Class<?> d = c;
            while (true) {
                if (d.isPrimitive()) {
                    char car;
                    if (d == Integer.TYPE) {
                        car = 'I';
                    } else if (d == Void.TYPE) {
                        car = 'V';
                    } else if (d == Boolean.TYPE) {
                        car = 'Z';
                    } else if (d == Byte.TYPE) {
                        car = 'B';
                    } else if (d == Character.TYPE) {
                        car = 'C';
                    } else if (d == Short.TYPE) {
                        car = 'S';
                    } else if (d == Double.TYPE) {
                        car = 'D';
                    } else if (d == Float.TYPE) {
                        car = 'F';
                    } else /* if (d == Long.TYPE) */ {
                        car = 'J';
                    }
                    buf.append(car);
                    return;
                } else if (d.isArray()) {
                    buf.append('[');
                    d = d.getComponentType();
                } else {
                    buf.append('L');
                    String name = d.getName();
                    int len = name.length();
                    for (int i = 0; i < len; ++i) {
                        char car = name.charAt(i);
                        buf.append(car == '.' ? '/' : car);
                    }
                    buf.append(';');
                    return;
                }
            }
        }

    }

}
