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
package com.shulie.instrument.simulator.jdk.impl.module;


import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class JavaModule {

    public static final JavaModule UNSUPPORTED = null;

    private static final ModuleSupport MODULE_SUPPORT = AccessController.doPrivileged(ModuleSupport.Builder.INSTANCE);

    private final AnnotatedElement module;

    protected JavaModule(AnnotatedElement module) {
        this.module = module;
    }

    public static JavaModule ofModule(Class<?> type) {
        return MODULE_SUPPORT.moduleOf(type);
    }

    public static JavaModule of(Object module) {
        if (!JavaType.MODULE.isInstance(module)) {
            throw new IllegalArgumentException("Not a Java module: " + module);
        }
        return new JavaModule((AnnotatedElement) module);
    }

    public static boolean isSupported() {
        return MODULE_SUPPORT.isSupported();
    }

    public boolean isNamed() {
        return MODULE_SUPPORT.isNamed(module);
    }

    public String getActualName() {
        return MODULE_SUPPORT.getName(module);
    }

    public InputStream getResourceAsStream(String name) {
        return MODULE_SUPPORT.getResourceAsStream(module, name);
    }

    public ClassLoader getClassLoader() {
        return MODULE_SUPPORT.getClassLoader(module);
    }

    public Object unwrap() {
        return module;
    }

    public boolean canRead(JavaModule module) {
        return MODULE_SUPPORT.canRead(this.module, module.unwrap());
    }

    public boolean isExported(String packageName, JavaModule module) {
        return packageName == null || MODULE_SUPPORT.isExported(this.module, module.unwrap(), packageName);
    }

    public boolean isOpened(String packageName, JavaModule module) {
        return packageName == null || MODULE_SUPPORT.isOpened(this.module, module.unwrap(), packageName);
    }

    public void modify(Instrumentation instrumentation,
                       Set<JavaModule> reads,
                       Map<String, Set<JavaModule>> exports,
                       Map<String, Set<JavaModule>> opens,
                       Set<Class<?>> uses,
                       Map<Class<?>, List<Class<?>>> provides) {
        Set<Object> unwrappedReads = new HashSet<Object>();
        for (JavaModule read : reads) {
            unwrappedReads.add(read.unwrap());
        }
        Map<String, Set<Object>> unwrappedExports = new HashMap<String, Set<Object>>();
        for (Map.Entry<String, Set<JavaModule>> entry : exports.entrySet()) {
            Set<Object> modules = new HashSet<Object>();
            for (JavaModule module : entry.getValue()) {
                modules.add(module.unwrap());
            }
            unwrappedExports.put(entry.getKey(), modules);
        }
        Map<String, Set<Object>> unwrappedOpens = new HashMap<String, Set<Object>>();
        for (Map.Entry<String, Set<JavaModule>> entry : opens.entrySet()) {
            Set<Object> modules = new HashSet<Object>();
            for (JavaModule module : entry.getValue()) {
                modules.add(module.unwrap());
            }
            unwrappedOpens.put(entry.getKey(), modules);
        }
        MODULE_SUPPORT.modify(instrumentation, module, unwrappedReads, unwrappedExports, unwrappedOpens, uses, provides);
    }

    @Override
    public int hashCode() {
        return module.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof JavaModule)) {
            return false;
        }
        JavaModule javaModule = (JavaModule) other;
        return module.equals(javaModule.module);
    }

    @Override
    public String toString() {
        return module.toString();
    }

    protected interface ModuleSupport {

        boolean isSupported();

        JavaModule moduleOf(Class<?> type);

        boolean isNamed(Object module);

        String getName(Object module);

        InputStream getResourceAsStream(Object module, String name);

        ClassLoader getClassLoader(Object module);

        boolean isExported(Object source, Object target, String aPackage);

        boolean isOpened(Object source, Object target, String aPackage);

        boolean canRead(Object source, Object target);

        void modify(Instrumentation instrumentation,
                    Object module,
                    Set<Object> reads,
                    Map<String, Set<Object>> exports,
                    Map<String, Set<Object>> opens,
                    Set<Class<?>> uses,
                    Map<Class<?>, List<Class<?>>> provides);

        enum Builder implements PrivilegedAction<ModuleSupport> {

            INSTANCE;

            @Override
            public ModuleSupport run() {
                try {
                    Class<?> module = Class.forName("java.lang.Module", false, null); // e.g. Netbeans contains a comilation target proxy
                    try {
                        Class<?> instrumentation = Class.forName("java.lang.instrument.Instrumentation");
                        return new ModuleEnabled.WithInstrumentationSupport(Class.class.getMethod("getModule"),
                                module.getMethod("getClassLoader"),
                                module.getMethod("isNamed"),
                                module.getMethod("getName"),
                                module.getMethod("getResourceAsStream", String.class),
                                module.getMethod("isExported", String.class, module),
                                module.getMethod("isOpen", String.class, module),
                                module.getMethod("canRead", module),
                                instrumentation.getMethod("isModifiableModule", module),
                                instrumentation.getMethod("redefineModule", module, Set.class, Map.class, Map.class, Set.class, Map.class));
                    } catch (ClassNotFoundException ignored) {
                        return new ModuleEnabled.WithoutInstrumentationSupport(Class.class.getMethod("getModule"),
                                module.getMethod("getClassLoader"),
                                module.getMethod("isNamed"),
                                module.getMethod("getName"),
                                module.getMethod("getResourceAsStream", String.class),
                                module.getMethod("isExported", String.class, module),
                                module.getMethod("isOpen", String.class, module),
                                module.getMethod("canRead", module));
                    }
                } catch (ClassNotFoundException ignored) {
                    return ModuleDisabled.INSTANCE;
                } catch (NoSuchMethodException ignored) {
                    return ModuleDisabled.INSTANCE;
                }
            }
        }

        abstract class ModuleEnabled implements ModuleSupport {

            private static final Object[] NO_ARGUMENTS = new Object[0];

            private final Method getModule;
            private final Method getClassLoader;
            private final Method isNamed;
            private final Method getName;
            private final Method getResourceAsStream;
            private final Method isExported;
            private final Method isOpened;
            private final Method canRead;

            protected ModuleEnabled(Method getModule,
                                    Method getClassLoader,
                                    Method isNamed,
                                    Method getName,
                                    Method getResourceAsStream,
                                    Method isExported,
                                    Method isOpened,
                                    Method canRead) {
                this.getModule = getModule;
                this.getClassLoader = getClassLoader;
                this.isNamed = isNamed;
                this.getName = getName;
                this.getResourceAsStream = getResourceAsStream;
                this.isExported = isExported;
                this.isOpened = isOpened;
                this.canRead = canRead;
            }

            @Override
            public boolean isSupported() {
                return true;
            }

            @Override
            public JavaModule moduleOf(Class<?> type) {
                try {
                    return new JavaModule((AnnotatedElement) getModule.invoke(type, NO_ARGUMENTS));
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + getModule, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + getModule, exception.getCause());
                }
            }

            @Override
            public InputStream getResourceAsStream(Object module, String name) {
                try {
                    return (InputStream) getResourceAsStream.invoke(module, name);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + getResourceAsStream, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + getResourceAsStream, exception.getCause());
                }
            }

            @Override
            public ClassLoader getClassLoader(Object module) {
                try {
                    return (ClassLoader) getClassLoader.invoke(module, NO_ARGUMENTS);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + getClassLoader, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + getClassLoader, exception.getCause());
                }
            }

            @Override
            public boolean isNamed(Object module) {
                try {
                    return (Boolean) isNamed.invoke(module, NO_ARGUMENTS);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + isNamed, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + isNamed, exception.getCause());
                }
            }

            @Override
            public String getName(Object module) {
                try {
                    return (String) getName.invoke(module, NO_ARGUMENTS);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + getName, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + getName, exception.getCause());
                }
            }

            @Override
            public boolean isExported(Object source, Object target, String aPackage) {
                try {
                    return (Boolean) isExported.invoke(source, aPackage, target);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + isExported, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + isExported, exception.getCause());
                }
            }

            @Override
            public boolean isOpened(Object source, Object target, String aPackage) {
                try {
                    return (Boolean) isOpened.invoke(source, aPackage, target);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + isOpened, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + isOpened, exception.getCause());
                }
            }

            @Override
            public boolean canRead(Object source, Object target) {
                try {
                    return (Boolean) canRead.invoke(source, target);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + canRead, exception);
                } catch (InvocationTargetException exception) {
                    throw new IllegalStateException("Cannot invoke " + canRead, exception.getCause());
                }
            }

            protected static class WithoutInstrumentationSupport extends ModuleEnabled {

                protected WithoutInstrumentationSupport(Method getModule,
                                                        Method getClassLoader,
                                                        Method isNamed,
                                                        Method getName,
                                                        Method getResourceAsStream,
                                                        Method isExported,
                                                        Method isOpened,
                                                        Method canRead) {
                    super(getModule,
                            getClassLoader,
                            isNamed,
                            getName,
                            getResourceAsStream,
                            isExported,
                            isOpened,
                            canRead);
                }

                @Override
                public void modify(Instrumentation instrumentation,
                                   Object source,
                                   Set<Object> reads,
                                   Map<String, Set<Object>> exports,
                                   Map<String, Set<Object>> opens,
                                   Set<Class<?>> uses,
                                   Map<Class<?>, List<Class<?>>> provides) {
                    throw new IllegalStateException("Did not expect use of instrumentation");
                }
            }

            protected static class WithInstrumentationSupport extends ModuleEnabled {

                private final Method isModifiableModule;

                private final Method redefineModule;

                protected WithInstrumentationSupport(Method getModule,
                                                     Method getClassLoader,
                                                     Method isNamed,
                                                     Method getName,
                                                     Method getResourceAsStream,
                                                     Method isExported,
                                                     Method isOpened,
                                                     Method canRead,
                                                     Method isModifiableModule,
                                                     Method redefineModule) {
                    super(getModule,
                            getClassLoader,
                            isNamed,
                            getName,
                            getResourceAsStream,
                            isExported,
                            isOpened,
                            canRead);
                    this.isModifiableModule = isModifiableModule;
                    this.redefineModule = redefineModule;
                }

                @Override
                public void modify(Instrumentation instrumentation,
                                   Object source,
                                   Set<Object> reads,
                                   Map<String, Set<Object>> exports,
                                   Map<String, Set<Object>> opens,
                                   Set<Class<?>> uses,
                                   Map<Class<?>, List<Class<?>>> provides) {
                    try {
                        if (!(Boolean) isModifiableModule.invoke(instrumentation, source)) {
                            throw new IllegalStateException(source + " is not modifiable");
                        }
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access " + redefineModule, exception);
                    } catch (InvocationTargetException exception) {
                        throw new IllegalStateException("Cannot invoke " + redefineModule, exception.getCause());
                    }
                    try {
                        redefineModule.invoke(instrumentation, source, reads, exports, opens, uses, provides);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access " + redefineModule, exception);
                    } catch (InvocationTargetException exception) {
                        throw new IllegalStateException("Cannot invoke " + redefineModule, exception.getCause());
                    }
                }
            }
        }

        enum ModuleDisabled implements ModuleSupport {

            INSTANCE;

            @Override
            public boolean isSupported() {
                return false;
            }

            @Override
            public JavaModule moduleOf(Class<?> type) {
                return UNSUPPORTED;
            }

            @Override
            public ClassLoader getClassLoader(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public boolean isNamed(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public String getName(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public InputStream getResourceAsStream(Object module, String name) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public boolean isExported(Object source, Object target, String aPackage) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public boolean isOpened(Object source, Object target, String aPackage) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public boolean canRead(Object source, Object target) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override
            public void modify(Instrumentation instrumentation,
                               Object module,
                               Set<Object> reads,
                               Map<String, Set<Object>> exports,
                               Map<String, Set<Object>> opens,
                               Set<Class<?>> uses,
                               Map<Class<?>, List<Class<?>>> provides) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }
        }
    }
}
