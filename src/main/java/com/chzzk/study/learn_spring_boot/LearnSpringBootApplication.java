package com.chzzk.study.learn_spring_boot;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LearnSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnSpringBootApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOrigins("http://localhost:3000");
			}
		};
	}

	// 이렇게 Bean 에 등록하게 되면 동시성 문제가 발생하지 않나요?
	// JPAQueryFactory 는 스레드 세이프합니다. 따라서 여러 스레드에서 동시에 사용해도 문제가 발생하지 않습니다.
	// JPAQueryFactory 를 생성할 때 EntityManager 를 주입받아서 사용하게 되면, 스프링이 관리하는 EntityManager 를 사용하게 됩니다.
	// 스프링이 관리하는 EntityManager 는 트랜잭션 단위로 동작합니다. 따라서 트랜잭션 단위로 동작하기 때문에 동시성 문제가 발생하지 않습니다.
	// 참고로 JPAQueryFactory 는 내부적으로 EntityManager 를 사용합니다.
	// 따라서 JPAQueryFactory 를 생성할 때 주입받은 EntityManager 는 스프링이 관리하는 EntityManager 와 동일한 EntityManager 입니다.
	// 따라서 스프링이 관리하는 EntityManager 를 사용하면 동시성 문제가 발생하지 않습니다.
	// 물론 JPAQueryFactory 를 직접 생성해서 사용하면 동시성 문제가 발생할 수 있습니다.
	// 이때는 JPAQueryFactory 를 스프링 빈으로 등록해서 사용하면 됩니다.
	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
