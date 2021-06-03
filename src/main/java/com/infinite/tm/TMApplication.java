package com.infinite.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories

public class TMApplication {

	public static void main(String[] args) {
		SpringApplication.run(TMApplication.class, args);
	}

}

@EnableScheduling
@Configuration
@ConditionalOnProperty(name="scheduling.enabled",matchIfMissing = true)
class Scheduling{
	
}
