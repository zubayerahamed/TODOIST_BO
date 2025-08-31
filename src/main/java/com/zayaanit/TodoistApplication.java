package com.zayaanit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zayaanit.config.DataAuditorAware;

import nl.martijndwars.webpush.Utils;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class TodoistApplication {

	public static void main(String[] args) {
//		var keyPair = Utils.generateVapidKeys();
//	    System.out.println("PUBLIC:  " + keyPair.getPublicKeyBase64());
//	    System.out.println("PRIVATE: " + keyPair.getPrivateKeyBase64());
		SpringApplication.run(TodoistApplication.class, args);
	}

	@Bean
	AuditorAware<Long> auditorAware() {
		return new DataAuditorAware();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
