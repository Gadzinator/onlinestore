package com.onlinestore.di.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParametersHolder {
    private final Properties properties = new Properties();
    private static ParametersHolder instance;

    private ParametersHolder() {
        readProperties();
    }

    public static ParametersHolder getInstance() {
        if (instance == null) {
            instance = new ParametersHolder();
        }

        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    private void readProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("./di/src/main/resources/application.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
