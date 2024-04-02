package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class CategoryDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;
}
