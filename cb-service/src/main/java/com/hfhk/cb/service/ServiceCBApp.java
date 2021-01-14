package com.hfhk.cb.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ServiceCBApp {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCBApp.class, args);
	}

}
