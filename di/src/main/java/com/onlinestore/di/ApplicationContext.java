package com.onlinestore.di;

import com.onlinestore.di.config.JavaConfig;
import com.onlinestore.di.injector.BeanInjector;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ApplicationContext {

    private final BeanInjector beanInjector;
    private final JavaConfig config;

    public ApplicationContext(String packageToScan) throws ReflectiveOperationException {
        Reflections scanner = new Reflections(packageToScan);
        beanInjector = new BeanInjector(scanner);
        config = new JavaConfig(scanner);
    }

    public <T> T getObject(Class<T> type) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Map<Class, Object> beans = beanInjector.getInitializationBeans();
        Class<T> impl = config.resolveImpl(type);
        if (beans.containsKey(impl)) {
            return (T) beans.get(impl);
        }

        return (T) type;
    }
}
