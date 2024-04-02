package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@Component
public class OrderDto {

    private long id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private String created;

    @JsonProperty("products")
    private List<ProductDto> products;

    @JsonProperty("order_status")
    private String orderStatus;
}
