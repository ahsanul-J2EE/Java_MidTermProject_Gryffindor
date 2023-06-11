package com.onlinebookshop.BookService;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class BookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
		System.out.println("-----------Book Service Running-----------");
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
