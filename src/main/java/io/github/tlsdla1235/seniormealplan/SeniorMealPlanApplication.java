package io.github.tlsdla1235.seniormealplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SeniorMealPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeniorMealPlanApplication.class, args);
	}

}
