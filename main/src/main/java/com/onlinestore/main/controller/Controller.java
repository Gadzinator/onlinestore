package com.onlinestore.main.controller;

import com.onlinestore.di.annotation.Autowire;
import com.onlinestore.di.annotation.Component;
import com.onlinestore.main.service.IService;

@Component
public class Controller implements IController {

    private final IService service;

    @Autowire
    public Controller(IService service) {
        this.service = service;
    }

    @Override
    public void executeController() {
        System.out.println("Execute method in Controller");
        service.execute();
    }
}
