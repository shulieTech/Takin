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
package com.pamirs.pradar.json;

import com.pamirs.pradar.common.ToStringStyle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;

/**
 * @author fabing.zhaofb
 */
public abstract class ResultSerializer {
    private final static String TAB = " ";
    private final static Map<Byte, String> ASCII_MAP = new HashMap<Byte, String>();

    static {
        ASCII_MAP.put((byte) 0, "NUL");
        ASCII_MAP.put((byte) 1, "SOH");
        ASCII_MAP.put((byte) 2, "STX");
        ASCII_MAP.put((byte) 3, "ETX");
        ASCII_MAP.put((byte) 4, "EOT");
        ASCII_MAP.put((byte) 5, "ENQ");
        ASCII_MAP.put((byte) 6, "ACK");
        ASCII_MAP.put((byte) 7, "BEL");
        ASCII_MAP.put((byte) 8, "BS");
        ASCII_MAP.put((byte) 9, "HT");
        ASCII_MAP.put((byte) 10, "LF");
        ASCII_MAP.put((byte) 11, "VT");
        ASCII_MAP.put((byte) 12, "FF");
        ASCII_MAP.put((byte) 13, "CR");
        ASCII_MAP.put((byte) 14, "SO");
        ASCII_MAP.put((byte) 15, "SI");
        ASCII_MAP.put((byte) 16, "DLE");
        ASCII_MAP.put((byte) 17, "DC1");
        ASCII_MAP.put((byte) 18, "DC2");
        ASCII_MAP.put((byte) 19, "DC3");
        ASCII_MAP.put((byte) 20, "DC4");
        ASCII_MAP.put((byte) 21, "NAK");
        ASCII_MAP.put((byte) 22, "SYN");
        ASCII_MAP.put((byte) 23, "ETB");
        ASCII_MAP.put((byte) 24, "CAN");
        ASCII_MAP.put((byte) 25, "EM");
        ASCII_MAP.put((byte) 26, "SUB");
        ASCII_MAP.put((byte) 27, "ESC");
        ASCII_MAP.put((byte) 28, "FS");
        ASCII_MAP.put((byte) 29, "GS");
        ASCII_MAP.put((byte) 30, "RS");
        ASCII_MAP.put((byte) 31, "US");
        ASCII_MAP.put((byte) 127, "DEL");
    }

    private static void renderObject(Object obj, int deep, int expand, final StringBuilder buf) {

        if (null == obj) {
            appendStringBuilder(buf, "null");
        } else {

            final Class<?> clazz = obj.getClass();
            final String className = clazz.getSimpleName();

            // 7种基础类型,直接输出@类型[值]
            if (Integer.class.isInstance(obj)
                    || Long.class.isInstance(obj)
                    || Float.class.isInstance(obj)
                    || Double.class.isInstance(obj)
                    //                    || Character.class.isInstance(obj)
                    || Short.class.isInstance(obj)
                    || Byte.class.isInstance(obj)
                    || Boolean.class.isInstance(obj)) {
                appendStringBuilder(buf, format("@%s[%s]", className, obj));
            }

            // Char要特殊处理,因为有不可见字符的因素
            else if (Character.class.isInstance(obj)) {

                final Character c = (Character) obj;

                // ASCII的可见字符
                if (c >= 32
                        && c <= 126) {
                    appendStringBuilder(buf, format("@%s[%s]", className, c));
                }

                // ASCII的控制字符
                else if (ASCII_MAP.containsKey((byte) c.charValue())) {
                    appendStringBuilder(buf, format("@%s[%s]", className, ASCII_MAP.get((byte) c.charValue())));
                }

                // 超过ASCII的编码范围
                else {
                    appendStringBuilder(buf, format("@%s[%s]", className, c));
                }

            }

            // 字符串类型单独处理
            else if (String.class.isInstance(obj)) {
                appendStringBuilder(buf, "@");
                appendStringBuilder(buf, className);
                appendStringBuilder(buf, "[");
                for (Character c : ((String) obj).toCharArray()) {
                    switch (c) {
                        case '\n':
                            appendStringBuilder(buf, "\\n");
                            break;
                        case '\r':
                            appendStringBuilder(buf, "\\r");
                            break;
                        default:
                            appendStringBuilder(buf, c.toString());
                    }//switch
                }//for
                appendStringBuilder(buf, "]");
            }

            // 集合类输出
            else if (Collection.class.isInstance(obj)) {

                @SuppressWarnings("unchecked") final Collection<Object> collection = (Collection<Object>) obj;

                // 非根节点或空集合只展示摘要信息
                if (!isExpand(deep, expand)
                        || collection.isEmpty()) {

                    appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                            className,
                            collection.isEmpty(),
                            collection.size()));
                }

                // 展开展示
                else {
                    appendStringBuilder(buf, format("@%s[", className));
                    boolean append = false;
                    for (Object e : collection) {
                        for (int i = 0; i < deep + 1; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        renderObject(e, deep + 1, expand, buf);
                        appendStringBuilder(buf, ",");
                        append = true;
                    }
                    if (append) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }

            }


            // Map类输出
            else if (Map.class.isInstance(obj)) {
                @SuppressWarnings("unchecked") final Map<Object, Object> map = (Map<Object, Object>) obj;

                // 非根节点或空集合只展示摘要信息
                if (!isExpand(deep, expand)
                        || map.isEmpty()) {

                    appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                            className,
                            map.isEmpty(),
                            map.size()));

                } else {
                    appendStringBuilder(buf, format("@%s[", className));
                    boolean append = false;
                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        for (int i = 0; i < deep + 1; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        renderObject(entry.getKey(), deep + 1, expand, buf);
                        appendStringBuilder(buf, ":");
                        renderObject(entry.getValue(), deep + 1, expand, buf);
                        appendStringBuilder(buf, ",");
                        append = true;
                    }
                    if (append) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }
            }


            // 数组类输出
            else if (obj.getClass().isArray()) {


                final String typeName = obj.getClass().getSimpleName();

                // int[]
                if (typeName.equals("int[]")) {

                    final int[] arrays = (int[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                typeName,
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        boolean append = false;
                        for (int e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // long[]
                else if (typeName.equals("long[]")) {

                    final long[] arrays = (long[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                typeName,
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        boolean append = false;
                        for (long e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // short[]
                else if (typeName.equals("short[]")) {

                    final short[] arrays = (short[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                typeName,
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        boolean append = false;
                        for (short e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // float[]
                else if (typeName.equals("float[]")) {

                    final float[] arrays = (float[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                typeName,
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (float e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // double[]
                else if (typeName.equals("double[]")) {

                    final double[] arrays = (double[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("[isEmpty=%s;size=%d]",
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (double e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // boolean[]
                else if (typeName.equals("boolean[]")) {

                    final boolean[] arrays = (boolean[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("[isEmpty=%s;size=%d]",
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (boolean e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // char[]
                else if (typeName.equals("char[]")) {

                    final char[] arrays = (char[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("[isEmpty=%s;size=%d]",
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (char e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // byte[]
                else if (typeName.equals("byte[]")) {

                    final byte[] arrays = (byte[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("[isEmpty=%s;size=%d]",
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (byte e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }

                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // Object[]
                else {
                    final Object[] arrays = (Object[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                            || arrays.length == 0) {

                        appendStringBuilder(buf, format("[isEmpty=%s;size=%d]",
                                arrays.length == 0,
                                arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, "[");
                        boolean append = false;
                        for (Object e : arrays) {
                            for (int i = 0; i < deep + 1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                            append = true;
                        }
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }
                }

            }


            // Throwable输出
            else if (Throwable.class.isInstance(obj)) {

                if (!isExpand(deep, expand)) {
                    appendStringBuilder(buf, format("[%s]", obj));
                } else {

                    final Throwable throwable = (Throwable) obj;
                    final StringWriter sw = new StringWriter();
                    final PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    appendStringBuilder(buf, sw.toString());
                }

            }

            // Date输出
            else if (Date.class.isInstance(obj)) {
                appendStringBuilder(buf, format("[%s]", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(obj)));
            } else if (obj instanceof Enum<?>) {
                appendStringBuilder(buf, format("[%s]", obj));
            }

            // 普通Object输出
            else {

                if (!isExpand(deep, expand)) {
                    appendStringBuilder(buf, format("[%s]", obj));
                } else {
                    appendStringBuilder(buf, "[");
                    List<Field> fields = new ArrayList<Field>();
                    Class objClass = obj.getClass();
                    for (Field field : objClass.getDeclaredFields()) {
                        fields.add(field);
                    }

                    if (null != fields) {
                        boolean append = false;
                        for (Field field : fields) {

                            field.setAccessible(true);
                            try {
                                final Object value = field.get(obj);

                                for (int i = 0; i < deep + 1; i++) {
                                    appendStringBuilder(buf, TAB);
                                }
                                appendStringBuilder(buf, field.getName());
                                appendStringBuilder(buf, "=");
                                renderObject(value, deep + 1, expand, buf);
                                appendStringBuilder(buf, ",");
                                append = true;
                            } catch (Throwable t) {
                                // ignore
                            }
                        }//for
                        if (append) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                    }//if
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }

            }
        }
    }

    /**
     * append string to a string builder, with upper limit check
     *
     * @param buf  the StringBuilder buffer
     * @param data the data to be appended
     * @throws ObjectTooLargeException if the size has exceeded the upper limit
     */
    private static void appendStringBuilder(StringBuilder buf, String data) {
        buf.append(data);
    }

    /**
     * 是否展开当前深度的节点
     *
     * @param deep   当前节点的深度
     * @param expand 展开极限
     * @return true:当前节点需要展开 / false:当前节点不需要展开
     */
    private static boolean isExpand(int deep, int expand) {
        return deep < expand;
    }

    public static String serializeObject(Object request, int deep) {
        StringBuilder buf = new StringBuilder();
        renderObject(request, 0, deep, buf);
        return buf.toString();
    }

    public static String serializeRequest(Object request, long maxRequestSize) {
        return request == null ? null : toStr(request, maxRequestSize);
    }

    public static String serializeResponse(Object response, long maxResponseSize) {
        return response == null ? null : toStr(response, maxResponseSize);
    }

    public static String toStr(Object request, long limitedSize) {
        if (null == request) {
            return "";
        }
        try {
            if (Throwable.class.isAssignableFrom(request.getClass())) {
                StringWriter stringWriter = new StringWriter();
                ((Throwable) request).printStackTrace(new PrintWriter(stringWriter));
                String stack = stringWriter.toString();
                if (stack.length() > limitedSize) {
                    stack = stack.substring(0, (int) limitedSize);
                }
                return stack;
            }
            ToStringStyle toStringStyle = new ToStringStyle.ShortPrefixToStringStyle().setLimitSize(limitedSize);
            StringBuffer content = new StringBuffer();
            toStringStyle.append(content, null, request, true);
            if (content.length() < 1) {
                return "";
            }
            return content.deleteCharAt(content.length() - 1).toString();
        } catch (Throwable e) {
            //ignore
        }
        return "";
    }

    private static class ObjectTooLargeException extends Exception {

        public ObjectTooLargeException(String message) {
            super(message);
        }
    }
}


