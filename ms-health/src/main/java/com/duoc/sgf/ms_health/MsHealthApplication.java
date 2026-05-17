package com.duoc.sgf.ms_health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.duoc.sgf.ms_health.client")
public class MsHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHealthApplication.class, args);
	}
}