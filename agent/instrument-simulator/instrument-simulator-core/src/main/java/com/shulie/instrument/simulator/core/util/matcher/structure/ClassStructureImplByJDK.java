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
package com.shulie.instrument.simulator.core.util.matcher.structure;

import com.shulie.instrument.simulator.api.util.LazyGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class ModifierAccess implements Access {

    private final int modifiers;

    ModifierAccess(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }

    @Override
    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    @Override
    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    @Override
    public boolean isInterface() {
        return Modifier.isInterface(modifiers);
    }

    @Override
    public boolean isNative() {
        return Modifier.isNative(modifiers);
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

}

class AccessImplByJDKClass extends ModifierAccess {

    private final Class<?> clazz;

    AccessImplByJDKClass(Class<?> clazz) {
        super(clazz.getModifiers());
        this.clazz = clazz;
    }

    @Override
    public boolean isEnum() {
        return clazz.isEnum();
    }

    @Override
    public boolean isAnnotation() {
        return clazz.isAnnotation();
    }

}

class AccessImplByJDKBehavior extends ModifierAccess {

    AccessImplByJDKBehavior(Method method) {
        super(method.getModifiers());
    }

    AccessImplByJDKBehavior(Constructor constructor) {
        super(constructor.getModifiers());
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }
}

/**
 * 用JDK的反射实现的类结构
 */
public class ClassStructureImplByJDK extends FamilyClassStructure {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClassStructureImplByJDK.class);
    private final static Annotation[] EMPTY_ANNOTATION = new Annotation[0];
    private final Class<?> clazz;
    private String javaClassName;

    public ClassStructureImplByJDK(final Class<?> clazz) {
        this.clazz = clazz;
    }

    private ClassStructure newInstance(final Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        return new ClassStructureImplByJDK(clazz);
    }

    private List<ClassStructure> newInstances(final Class[] classArray) {
        final List<ClassStructure> classStructures = new ArrayList<ClassStructure>();
        if (null != classArray) {
            for (final Class<?> clazz : classArray) {
                final ClassStructure classStructure = newInstance(clazz);
                if (null != classStructure) {
                    classStructures.add(classStructure);
                }
            }
        }
        return classStructures;
    }

    @Override
    public String getJavaClassName() {
        return null != javaClassName
                ? javaClassName
                : (javaClassName = getJavaClassName(clazz));
    }

    private String getJavaClassName(Class<?> clazz) {
        if (clazz.isArray()) {
            return getJavaClassName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }


    @Override
    public ClassLoader getClassLoader() {
        return clazz.getClassLoader();
    }

    @Override
    public ClassStructure getSuperClassStructure() {
        // 过滤掉Object.class
        return Object.class.equals(clazz.getSuperclass())
                ? null
                : newInstance(clazz.getSuperclass());
    }

    @Override
    public List<ClassStructure> getInterfaceClassStructures() {
        return newInstances(clazz.getInterfaces());
    }

    private Class[] getAnnotationTypeArray(final Annotation[] annotationArray) {
        final Collection<Class> annotationTypes = new ArrayList<Class>();
        for (final Annotation annotation : annotationArray) {
            if (annotation.getClass().isAnnotation()) {
                annotationTypes.add(annotation.getClass());
            }
            for (final Class annotationInterfaceClass : annotation.getClass().getInterfaces()) {
                if (annotationInterfaceClass.isAnnotation()) {
                    annotationTypes.add(annotationInterfaceClass);
                }
            }
        }
        return annotationTypes.toArray(new Class[0]);
    }

    private final LazyGet<List<ClassStructure>> annotationTypeClassStructuresLazyGet
            = new LazyGet<List<ClassStructure>>() {
        @Override
        protected List<ClassStructure> initialValue() {
            return Collections.unmodifiableList(newInstances(getAnnotationTypeArray(getDeclaredAnnotations(clazz))));
        }
    };

    @Override
    public List<ClassStructure> getAnnotationTypeClassStructures() {
        return annotationTypeClassStructuresLazyGet.get();
    }

    /**
     * 获取声明的注解可能会因为应用版本不兼容导致出现ArrayStoreException的问题，
     * 发生这种情况则直接忽略, 返回空的注解数组
     *
     * @param clazz 目标类
     * @return
     */
    private Annotation[] getDeclaredAnnotations(Class clazz) {
        try {
            return clazz.getDeclaredAnnotations();
        } catch (ArrayStoreException e) {
            LOGGER.warn("SIMULATOR: class {}.getDeclaredAnnotations() occured err!", clazz.getName(), e);
            return EMPTY_ANNOTATION;
        }
    }

    /**
     * 获取声明的注解可能会因为应用版本不兼容导致出现ArrayStoreException的问题，
     * 发生这种情况则直接忽略, 返回空的注解数组
     *
     * @param method 目标方法
     * @return
     */
    private Annotation[] getDeclaredAnnotations(Method method) {
        try {
            return method.getDeclaredAnnotations();
        } catch (ArrayStoreException e) {
            LOGGER.warn("SIMULATOR: method {}.{}.getDeclaredAnnotations() occured err!", method.getDeclaringClass().getName(), method.getName(), e);
            return EMPTY_ANNOTATION;
        }
    }

    /**
     * 获取声明的注解可能会因为应用版本不兼容导致出现ArrayStoreException的问题，
     * 发生这种情况则直接忽略, 返回空的注解数组
     *
     * @param constructor 目标构造方法
     * @return
     */
    private Annotation[] getDeclaredAnnotations(Constructor constructor) {
        try {
            return constructor.getDeclaredAnnotations();
        } catch (ArrayStoreException e) {
            LOGGER.warn("SIMULATOR: constructor {}.{}.getDeclaredAnnotations() occured err!", constructor.getDeclaringClass().getName(), constructor.getName(), e);
            return EMPTY_ANNOTATION;
        }
    }

    private BehaviorStructure newBehaviorStructure(final Method method) {
        return new BehaviorStructure(
                new AccessImplByJDKBehavior(method),
                method.getName(),
                this,
                newInstance(method.getReturnType()),
                newInstances(method.getParameterTypes()),
                newInstances(method.getExceptionTypes()),
                newInstances(getAnnotationTypeArray(getDeclaredAnnotations(method)))
        );
    }

    private BehaviorStructure newBehaviorStructure(final Constructor constructor) {
        return new BehaviorStructure(
                new AccessImplByJDKBehavior(constructor),
                "<init>",
                this,
                this,
                newInstances(constructor.getParameterTypes()),
                newInstances(constructor.getExceptionTypes()),
                newInstances(getAnnotationTypeArray(getDeclaredAnnotations(constructor)))
        );
    }

    private final LazyGet<List<BehaviorStructure>> behaviorStructuresLazyGet
            = new LazyGet<List<BehaviorStructure>>() {
        @Override
        protected List<BehaviorStructure> initialValue() {
            final List<BehaviorStructure> behaviorStructures = new ArrayList<BehaviorStructure>();
            for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                behaviorStructures.add(newBehaviorStructure(constructor));
            }
            for (final Method method : clazz.getDeclaredMethods()) {
                behaviorStructures.add(newBehaviorStructure(method));
            }
            return Collections.unmodifiableList(behaviorStructures);
        }
    };

    @Override
    public List<BehaviorStructure> getBehaviorStructures() {
        return behaviorStructuresLazyGet.get();
    }

    @Override
    public Access getAccess() {
        return new AccessImplByJDKClass(clazz);
    }

    @Override
    public String toString() {
        return "ClassStructureImplByJDK{" + "javaClassName='" + javaClassName + '\'' + '}';
    }
}
