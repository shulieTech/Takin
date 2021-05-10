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
package com.pamirs.pradar;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


public class ObjectTree {
    public static void dump(PrintWriter pw, Object root) {
        Node nodeTree = Node.create(root);
        printTree(new StringBuilder(), new StringBuilder(), pw, nodeTree);
    }

    public static String dump(Object root) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        dump(pw, root);
        pw.flush();
        return sw.toString();
    }

    private static class Node {
        private String name;
        private List<Node> children;

        private long shallowSize;
        private long deepSize;

        public Node(String name, Object delegate) {
            this.name = name;

            if (delegate != null) {
                shallowSize = RamUsageEstimator.shallowSizeOf(delegate);
                deepSize = shallowSize;
            }
        }

        private void addChild(Node node) {
            if (children == null) {
                children = new ArrayList<Node>();
            }
            children.add(node);
            deepSize += node.deepSize;
        }

        public static Node create(Object delegate) {
            return create("root", delegate, new IdentityHashMap<Object, Integer>());
        }

        public static Node create(String prefix, Object delegate,
                                  IdentityHashMap<Object, Integer> seen) {
            if (delegate == null) throw new IllegalArgumentException();

            if (seen.containsKey(delegate)) {
                return new Node("[seen " + uniqueName(delegate, seen) + "]", null);
            }
            seen.put(delegate, seen.size());

            Class<?> clazz = delegate.getClass();
            if (clazz.isArray()) {
                Node parent = new Node(prefix + " => " + clazz.getSimpleName(),
                        delegate);
                if (clazz.getComponentType().isPrimitive()) {
                    return parent;
                } else {
                    final int length = Array.getLength(delegate);
                    for (int i = 0; i < length; i++) {
                        Object value = Array.get(delegate, i);
                        if (value != null) {
                            parent.addChild(create("[" + i + "]", value, seen));
                        }
                    }
                    return parent;
                }
            } else {
                List<Field> declaredFields = new ArrayList<Field>();
                for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
                    Field[] fields = c.getDeclaredFields();
                    AccessibleObject.setAccessible(fields, true);
                    declaredFields.addAll(Arrays.asList(fields));
                }
                Collections.sort(declaredFields, new Comparator<Field>() {
                    @Override
                    public int compare(Field o1, Field o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                Node parent = new Node(prefix + " => " + uniqueName(delegate, seen),
                        delegate);
                for (Field f : declaredFields) {
                    try {
                        if (!Modifier.isStatic(f.getModifiers())
                                && !f.getType().isPrimitive()) {
                            Object fValue = f.get(delegate);
                            if (fValue != null) {
                                parent.addChild(create(
                                        f.getType().getSimpleName() + " " + f.getName(), fValue,
                                        seen));
                            } else {
                                parent.addChild(new Node(f.getType().getSimpleName() + " "
                                        + f.getName() + " => null", null));
                            }
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                return parent;
            }
        }

        private static String uniqueName(Object t,
                                         IdentityHashMap<Object, Integer> seen) {
            return "<" + t.getClass().getSimpleName() + "#" + seen.get(t) + ">";
        }

        public String getName() {
            return name;
        }

        public boolean hasChildren() {
            return children != null && !children.isEmpty();
        }

        public List<Node> getChildren() {
            return children;
        }
    }

    private static void printTree(StringBuilder prefix, StringBuilder line,
                                  PrintWriter pw, Node node) {
        line.append(node.getName());
        pw.println(String.format(Locale.ENGLISH, "%,8d %,8d  %s", node.deepSize,
                node.shallowSize, line.toString()));
        line.setLength(0);

        if (node.hasChildren()) {
            int pLen = prefix.length();
            for (Iterator<Node> i = node.getChildren().iterator(); i.hasNext(); ) {
                Node next = i.next();
                line.append(prefix.toString());
                line.append("+- ");
                prefix.append(i.hasNext() ? "|  " : "   ");
                printTree(prefix, line, pw, next);
                prefix.setLength(pLen);
            }
        }
    }
}
