package com.onlinestore.di.config;

import org.reflections.Reflections;

import java.util.Set;

public class JavaConfig implements Config {

    private final Reflections scanner;

    public JavaConfig(Reflections scanner) {
        this.scanner = scanner;
    }

    public <T> Class<T> resolveImpl(Class<T> type) {
        if (type.isInterface()) {
            type = (Class<T>) getImplClass(type);
        }

        return type;
    }

    private <T> Class<? extends T> getImplClass(Class<T> type) {
        Set<Class<? extends T>> set = scanner.getSubTypesOf(type);
        if (set.size() != 1) {
            throw new IllegalArgumentException(type + " нет такого бина");
        }

        return set.iterator().next();
    }
}
