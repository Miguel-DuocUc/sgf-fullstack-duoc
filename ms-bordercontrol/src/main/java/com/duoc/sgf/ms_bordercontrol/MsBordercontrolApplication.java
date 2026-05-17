package com.duoc.sgf.ms_bordercontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.duoc.sgf.ms_bordercontrol.client")
public class MsBordercontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBordercontrolApplication.class, args);
	}
}