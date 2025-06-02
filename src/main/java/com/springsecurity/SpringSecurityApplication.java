package com.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing //  (for @CreatedDate, @LastModifiedDate to work)
@EnableJpaRepositories(basePackages = {"com.springsecurity.repository"})
@EntityScan(basePackages = {"com.springsecurity.model"}) // Scan for JPA entities in the specified package
@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
