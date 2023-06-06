package com.onlinebookshop.Inventory.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
		System.out.println("------------Inventory Service Running------------");
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
