package com.khandras.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {"org.telegram", "com.khandras.bot"})
public class TasksBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksBotApplication.class, args);
	}

}
