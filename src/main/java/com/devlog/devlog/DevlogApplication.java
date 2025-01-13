package com.devlog.devlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DevlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevlogApplication.class, args);
	}
}
