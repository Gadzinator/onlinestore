package com.onlinestore.main.repository;

import com.onlinestore.di.annotation.Autowire;
import com.onlinestore.di.annotation.Component;
import com.onlinestore.di.annotation.Value;
import com.onlinestore.main.domain.ValueAnnotationExample;


@Component
public class Repository implements IRepository {

    @Value("my.param.db")
    public String repositoryValue;
    @Autowire
    private ValueAnnotationExample entity;

    @Override
    public void execute() {
        System.out.println("Execute method in Repository. name - " + repositoryValue);
    }
}
