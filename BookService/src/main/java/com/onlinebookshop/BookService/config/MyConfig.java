package com.onlinebookshop.BookService.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {


//        RestTemplate restTemplate = new RestTemplate();
//
//        List<ClientHttpRequestInterceptor> interceptors=new ArrayList<>();
//
//        restTemplate.setInterceptors(interceptors);
//
//
//        return restTemplate;

        return new RestTemplate();


    }



}
