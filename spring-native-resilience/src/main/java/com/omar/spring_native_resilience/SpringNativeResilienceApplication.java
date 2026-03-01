package com.omar.spring_native_resilience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods // Activates @Retryable and @ConcurrencyLimit processing
public class SpringNativeResilienceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNativeResilienceApplication.class, args);
	}

}
