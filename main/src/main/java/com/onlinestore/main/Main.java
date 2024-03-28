package com.onlinestore.main;

import com.onlinestore.di.ApplicationContext;
import com.onlinestore.di.ApplicationRunner;
import com.onlinestore.main.controller.IController;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        ApplicationContext context = ApplicationRunner.run("com.onlinestore");
        context.getObject(IController.class).executeController();
    }
}
