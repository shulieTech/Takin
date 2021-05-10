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

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类 <br />
 * <br />
 * 提供了一系列的获取某一个类的信息的方法<br />
 * 包括获取全类名，实现的接口，接口的泛型等<br />
 * 并且提供了根据Class类型获取对应的实例对象的方法，以及修改属性和调用对象的方法等
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class ReflectionUtils {

    /**
     * 获取包名
     *
     * @return 包名【String类型】
     */
    public static String getPackage(Class<?> clazz) {
        Package pck = clazz.getPackage();
        if (null != pck) {
            return pck.getName();
        } else {
            return "没有包！";
        }
    }

    /**
     * 获取继承的父类的全类名
     *
     * @return 继承的父类的全类名【String类型】
     */
    public static String getSuperClassName(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            return superClass.getName();
        } else {
            return "没有父类！";
        }
    }

    /**
     * 获取全类名
     *
     * @return 全类名【String类型】
     */
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 获取实现的接口名
     *
     * @return 所有的接口名【每一个接口名的类型为String，最后保存到一个List集合中】
     */
    public static List<String> getInterfaces(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        int len = interfaces.length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Class<?> itfc = interfaces[i];

            // 接口名
            String interfaceName = itfc.getSimpleName();

            list.add(interfaceName);
        }

        return list;
    }

    /**
     * 获取所有属性
     *
     * @return 所有的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的属性
     *
     * @return 所有公共的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有构造方法
     *
     * @return 所有的构造方法【每一个构造方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        int len = constructors.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Constructor<?> constructor = constructors[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(constructor.getModifiers());
            sb.append(modifier + " ");

            // 方法名（类名）
            String constructorName = clazz.getSimpleName();
            sb.append(constructorName + " (");

            // 形参列表
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            int length = parameterTypes.length;
            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有自身的方法
     *
     * @return 所有自身的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的方法
     *
     * @return 所有公共的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有的注解名
     *
     * @return 所有的注解名【每一个注解名的类型为String，最后保存到一个List集合中】
     */
    public static List<String> getAnnotations(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        int len = annotations.length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];

            String annotationName = annotation.annotationType().getSimpleName();
            list.add(annotationName);
        }

        return list;
    }

    /**
     * 获取父类的泛型
     *
     * @return 父类的泛型【Class类型】
     */
    public static Class<?> getSuperClassGenericParameterizedType(Class<?> clazz) {
        Type genericSuperClass = clazz.getGenericSuperclass();

        Class<?> superClassGenericParameterizedType = null;

        // 判断父类是否有泛型
        if (genericSuperClass instanceof ParameterizedType) {
            // 向下转型，以便调用方法
            ParameterizedType pt = (ParameterizedType) genericSuperClass;
            // 只取第一个，因为一个类只能继承一个父类
            Type superClazz = pt.getActualTypeArguments()[0];
            // 转换为Class类型
            superClassGenericParameterizedType = (Class<?>) superClazz;
        }

        return superClassGenericParameterizedType;
    }

    /**
     * 获取接口的所有泛型
     *
     * @return 所有的泛型接口【每一个泛型接口的类型为Class，最后保存到一个List集合中】
     */
    public static List<Class<?>> getInterfaceGenericParameterizedTypes(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        int len = genericInterfaces.length;

        List<Class<?>> list = new ArrayList<Class<?>>();
        for (int i = 0; i < len; i++) {
            Type genericInterface = genericInterfaces[i];

            // 判断接口是否有泛型
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;

                // 得到所有的泛型【Type类型的数组】
                Type[] interfaceTypes = pt.getActualTypeArguments();

                int length = interfaceTypes.length;

                for (int j = 0; j < length; j++) {
                    // 获取对应的泛型【Type类型】
                    Type interfaceType = interfaceTypes[j];
                    // 转换为Class类型
                    Class<?> interfaceClass = (Class<?>) interfaceType;
                    list.add(interfaceClass);
                }

            }

        }

        return list;
    }

    /**
     * 打印包名
     */
    public static void printPackage(Class<?> clazz) {
        System.out.println(getPackage(clazz));
    }

    /**
     * 打印继承的父类的全类名
     */
    public static void printSuperClassName(Class<?> clazz) {
        System.out.println(getSuperClassName(clazz));
    }

    /**
     * 打印全类名
     */
    public static void printClassName(Class<?> clazz) {
        System.out.println(getClassName(clazz));
    }

    /**
     * 打印实现的接口
     */
    public static void printInterfaces(Class<?> clazz) {
        List<String> list = getInterfaces(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有实现接口！");
        }
    }

    /**
     * 打印所有属性
     */
    public static void printFields(Class<?> clazz) {
        List<StringBuilder> list = getFields(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有属性！");
        }
    }

    /**
     * 打印所有公共的属性
     */
    public static void printPublicFields(Class<?> clazz) {
        List<StringBuilder> list = getPublicFields(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有属性！");
        }
    }

    /**
     * 打印所有构造方法
     */
    public static void printConstructors(Class<?> clazz) {
        List<StringBuilder> list = getConstructors(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有构造方法！");
        }
    }

    /**
     * 打印所有方法
     */
    public static void printMethods(Class<?> clazz) {
        List<StringBuilder> list = getMethods(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有方法！");
        }
    }

    /**
     * 打印所有公共的方法
     */
    public static void printPublicMethods(Class<?> clazz) {
        List<StringBuilder> list = getPublicMethods(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有方法！");
        }
    }

    /**
     * 打印所有的注解
     */
    public static void printAnnotations(Class<?> clazz) {
        List<String> list = getAnnotations(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i));
            }
        } else {
            System.out.println("没有注解！");
        }
    }

    /**
     * 打印父类的泛型名
     */
    public static void printSuperClassGenericParameterizedType(Class<?> clazz) {
        Class<?> superClassGenericParameterizedType = getSuperClassGenericParameterizedType(clazz);
        if (null != superClassGenericParameterizedType) {
            System.out.println(superClassGenericParameterizedType.getSimpleName());
        } else {
            System.out.println("父类没有泛型！");
        }
    }

    /**
     * 打印接口的泛型
     */
    public static void printInterfaceGenericParameterizedTypes(Class<?> clazz) {
        List<Class<?>> list = getInterfaceGenericParameterizedTypes(clazz);
        int size = list.size();
        if (0 < size) {
            for (int i = 0; i < size; i++) {
                System.out.println(list.get(i).getSimpleName());
            }
        } else {
            System.out.println("没有泛型接口！");
        }
    }

    /**
     * 打印一个类的相关信息
     *
     * @param clazz
     */
    public static void printAll(Class<?> clazz) {
        System.out.print("【包名】  ");
        printPackage(clazz);

        System.out.print("【类名】  ");
        System.out.println(clazz.getSimpleName());

        System.out.println("\n【父类全类名】");
        printSuperClassName(clazz);
        System.out.println("【全类名】");
        printClassName(clazz);
        System.out.println("\n【所有已实现的接口】");
        printInterfaces(clazz);

        System.out.println("\n【属性】");
        printFields(clazz);
        System.out.println("\n【构造方法】");
        printConstructors(clazz);
        System.out.println("\n【方法】");
        printMethods(clazz);

        System.out.println("\n【公共的属性】");
        printPublicFields(clazz);
        System.out.println("\n【公共的方法】");
        printPublicMethods(clazz);
    }

    /**
     * 根据Class类型，获取对应的实例【要求必须有无参的构造器】
     *
     * @return 对应的实例【Object类型】
     */
    public static Object getNewInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    /**
     * 根据传入的类的Class对象，以及构造方法的形参的Class对象，获取对应的构造方法对象
     *
     * @param clazz          类的Class对象
     * @param parameterTypes 构造方法的形参的Class对象【可以不写】
     * @return 构造方法对象【Constructor类型】
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    /**
     * 根据传入的构造方法对象，以及，获取对应的实例
     *
     * @param constructor 构造方法对象
     * @param initargs    传入构造方法的实参【可以不写】
     * @return 对应的实例【Object类型】
     */
    public static Object getNewInstance(Constructor<?> constructor, Object... initargs)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        constructor.setAccessible(true);
        return constructor.newInstance(initargs);
    }

    /**
     * 根据传入的属性名字符串，修改对应的属性值
     *
     * @param clazz 类的Class对象
     * @param name  属性名
     * @param obj   要修改的实例对象
     * @param value 修改后的新值
     */
    public static void setField(Class<?> clazz, String name, Object obj, Object value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 根据传入的方法名字符串，获取对应的方法
     *
     * @param clazz          类的Class对象
     * @param name           方法名
     * @param parameterTypes 方法的形参对应的Class类型【可以不写】
     * @return 方法对象【Method类型】
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    /**
     * 根据传入的方法对象，调用对应的方法
     *
     * @param method 方法对象
     * @param obj    要调用的实例对象【如果是调用静态方法，则可以传入null】
     * @param args   传入方法的实参【可以不写】
     * @return 方法的返回值【没有返回值，则返回null】
     */
    public static Object invokeMethod(Method method, Object obj, Object... args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    /**
     * 获取私有对象属性信息
     *
     * @param classType 类元信息
     * @param obj       实例对象
     * @param fieldName 属性名
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getPrivateFieldValue(Class classType, Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field propertiesField = classType.getDeclaredField(fieldName);
        propertiesField.setAccessible(true);
        return propertiesField.get(obj);
    }


    /**
     * 获取对象属性信息
     *
     * @param propertiesField 属性元信息
     * @param obj             实例对象
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Field propertiesField, Object obj) throws IllegalAccessException {
        return propertiesField.get(obj);
    }

    /**
     * 获取注入方法，没有直接写死参数，提高method兼容性，注意：这个方法返回匹配最后一个参数方法
     *
     * @param instance   实例对象
     * @param methodName 匹配的方法名
     * @return
     */
    public static Method methodNameMatch(Object instance, String methodName) {
        Class<?> clazz = instance.getClass();
        Method method = null;
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null) {
            for (Method m : methods) {
                String name = m.getName();
                if (methodName.equals(name)) {
                    method = m;
                }
            }
        }
        return method;
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;

        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("反射获取属性值失败");
        }

        return result;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("反射设置属性值");
        }
    }


    /**
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                //Method 不在当前类定义, 继续向上转型
            }
        }

        return null;
    }

    /**
     * 使 filed 变为可访问
     *
     * @param field
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object
     * @param filedName
     * @return
     */
    public static Field getDeclaredField(Object object, String filedName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                //Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws InvocationTargetException {

        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("反射调用方法失败");
        }

    }

}
