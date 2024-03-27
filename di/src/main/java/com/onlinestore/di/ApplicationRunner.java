package com.onlinestore.di;

public class ApplicationRunner {

    public static ApplicationContext run(String packageToScan) {
        ApplicationContext applicationContext;
        try {
            applicationContext = new ApplicationContext(packageToScan);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return applicationContext;
    }
}
