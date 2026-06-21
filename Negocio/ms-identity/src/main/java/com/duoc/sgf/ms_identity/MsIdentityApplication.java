package com.duoc.sgf.ms_identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsIdentityApplication.class, args);
	}
}