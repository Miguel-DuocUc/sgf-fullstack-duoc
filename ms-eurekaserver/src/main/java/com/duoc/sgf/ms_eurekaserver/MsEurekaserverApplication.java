package com.duoc.sgf.ms_eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MsEurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEurekaserverApplication.class, args);
	}

}
