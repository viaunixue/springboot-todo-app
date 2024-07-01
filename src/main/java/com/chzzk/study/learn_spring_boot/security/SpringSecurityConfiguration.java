package com.chzzk.study.learn_spring_boot.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Function;

@Configuration
public class SpringSecurityConfiguration {
    // LDAP or Database
    // In Memory
    // InMemoryUserDetailsManager
    // InMemoryUserDetailsManager(UserDetails... users);
    // UserDetails 객체를 생성하고 InMemoryUserDetailsManager 클래스를 구축해 그것을 리턴합니다.

    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("chzzk")
//                .password("dummy")
//                .roles("USER", "ADMIN")
//                .build();

        UserDetails userDetails1 = getUserDetails("chzzk", "dummy");
        UserDetails userDetails2 = getUserDetails("toy", "dummydummy");

        // InMemoryUserDetailsManager는 가변인자를 받으므로 원하는 만큼 사용자를 받을 수 있습니다.
        return new InMemoryUserDetailsManager(userDetails1, userDetails2);
    }

    private UserDetails getUserDetails(String username, String password) {
        Function<String, String> passwordEncoder
                = input -> passwordEncoder().encode(input);

        UserDetails userDetails = User.builder()
                .passwordEncoder(passwordEncoder)
                .username(username)
                .password(password)
                .roles("USER", "ADMIN")
                .build();
        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        http.formLogin(withDefaults());

        // http.csrf().disable(); -> http.csrf(csrf -> csrf.disable());
        http.csrf(AbstractHttpConfigurer::disable);
        // http.headers().frameOptions().disable(); -> http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

}
