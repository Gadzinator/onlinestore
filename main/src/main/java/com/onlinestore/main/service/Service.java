package com.onlinestore.main.service;

import com.onlinestore.di.annotation.Autowire;
import com.onlinestore.di.annotation.Component;
import com.onlinestore.main.repository.IRepository;

@Component
public class Service implements IService {

    private IRepository repository;

    @Autowire
    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        System.out.println("Execute method in Service");
        repository.execute();
    }
}
