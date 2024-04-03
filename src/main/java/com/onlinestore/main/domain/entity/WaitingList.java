package com.onlinestore.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class WaitingList {

	private long id;

	private User user;

	private Product product;

	private int counter;
}
