package com.frankokafor.rest.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.frankokafor.rest.utils.AppProperties;

@Configuration
public class BeanConfiguration {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "AppProperties")
	public AppProperties appProperties() {
		return new AppProperties();
	}

	@Bean
	public JavaMailSenderImpl getJavaMail() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("smtp.gmail.com");
		sender.setPort(587);
		sender.setPassword("bophwcjrnuudcylf");
		sender.setUsername("frankebukaokafor@gmail.com");
		Properties props = sender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.starttls.required", true);
		props.put("mail.smtp.ssl.trust", "*");
		// props.put("mail.debug", true);
		return sender;
	}
}
