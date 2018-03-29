package com.jone.smoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class SmokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokeApplication.class, args);
		System.out.println("服务启动完成");
	}
}
