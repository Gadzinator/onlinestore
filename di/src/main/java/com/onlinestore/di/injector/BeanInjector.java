package com.onlinestore.di.injector;

import com.onlinestore.di.annotation.Autowire;
import com.onlinestore.di.annotation.Component;
import com.onlinestore.di.annotation.Value;
import com.onlinestore.di.config.JavaConfig;
import com.onlinestore.di.utils.ParametersHolder;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanInjector {

    private final ParametersHolder parametersHolder = ParametersHolder.getInstance();
    private final Reflections scanner;
    private final JavaConfig config;
    private final Map<Class, Object> beans = new HashMap<>();

    public BeanInjector(Reflections scanner) {
        this.scanner = scanner;
        config = new JavaConfig(scanner);
    }

    public Map<Class, Object> getInitializationBeans() throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException {
        Set<Class<?>> typesAnnotatedWith = scanner.getTypesAnnotatedWith(Component.class);

        while (beans.size() != typesAnnotatedWith.size()) {
            for (Class<?> aClass : typesAnnotatedWith) {
                boolean b = beans.containsKey(aClass);
                if (b) {
                    continue;
                }

                if (!isAutowireAnnotationPresent(aClass)) {
                    Object instance = aClass.getDeclaredConstructor().newInstance();
                    addToContext(aClass, instance);
                }

                for (Field declaredField : aClass.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(Autowire.class)) {
                        injectByField(aClass, declaredField);
                    }
                }

                for (Constructor<?> declaredConstructor : aClass.getDeclaredConstructors()) {
                    if (declaredConstructor.isAnnotationPresent(Autowire.class)) {
                        injectByConstructor(aClass, declaredConstructor);
                    }
                }

                for (Method method : aClass.getMethods()) {
                    if (method.isAnnotationPresent(Autowire.class)) {
                        injectByMethod(aClass, method);
                    }
                }

                injectValuesIntoFields();
            }
        }

        return beans;
    }

    private void injectByField(Class<?> aClass, Field declaredField) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            InstantiationException {
        declaredField.setAccessible(true);
        Class<?> fieldType = declaredField.getType();
        Class<?> impl = config.resolveImpl(fieldType);
        if (beans.containsKey(impl)) {
            Object instance = aClass.getDeclaredConstructor().newInstance();
            declaredField.setAccessible(true);
            declaredField.set(instance, beans.get(fieldType));
            addToContext(aClass, instance);
        }
    }

    private void injectByConstructor(Class<?> aClass, Constructor<?> declaredConstructor) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Class<?> impl = config.resolveImpl(parameterType);
            if (beans.containsKey(impl)) {
                parameters[i] = beans.get(impl);
                declaredConstructor.setAccessible(true);
                Object instance = declaredConstructor.newInstance(parameters);
                addToContext(aClass, instance);
            }
        }
    }

    private void injectByMethod(Class<?> aClass, Method method) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
            InstantiationException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            if (parameterType.isInterface()) {
                Class<?> impl = config.resolveImpl(parameterType);
                if (beans.containsKey(impl)) {
                    method.setAccessible(true);
                    Object instance = aClass.getDeclaredConstructor().newInstance();
                    addToContext(aClass, instance);
                    method.invoke(instance, beans.get(impl));
                }
            }
        }
    }

    private void injectValuesIntoFields() {
        for (Map.Entry<Class, Object> entry : beans.entrySet()) {
            Class<?> aClass = entry.getKey();
            Object object = entry.getValue();
            for (Field field : aClass.getDeclaredFields()) {
                Value annotation = field.getAnnotation(Value.class);
                if (annotation != null) {
                    setFieldValue(field, object, annotation);
                }
            }
        }
    }

    private void setFieldValue(Field field, Object object, Value annotation) {
        String value = annotation.value();
        String propertyName = parametersHolder.getProperty(value);
        field.setAccessible(true);
        try {
            field.set(object, propertyName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAutowireAnnotationPresent(Class<?> aClass) {
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowire.class)) {
                return true;
            }
        }
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowire.class)) {
                return true;
            }
        }

        for (Constructor<?> declaredConstructor : aClass.getDeclaredConstructors()) {
            if (declaredConstructor.isAnnotationPresent(Autowire.class)) {
                return true;
            }
        }

        return false;
    }

    private void addToContext(Class<?> aClass, Object object) {
        checkBeanAvailability(aClass);
        beans.put(aClass, object);
    }

    private void checkBeanAvailability(Class<?> aClass) {
        if (beans.containsKey(aClass)) {
            throw new IllegalArgumentException("Бин класса " + aClass + " уже существует");
        }
    }
}
