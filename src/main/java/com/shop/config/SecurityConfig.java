package com.shop.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig { //시큐리티 설정

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http.csrf().disable(); -> 람다식 변환필요(버전상향으로인해)
        // csrf 공격방지
        http
                .formLogin(config ->
                        config.loginPage("/members/login") //커스텀 로그인
                            .defaultSuccessUrl("/") //로그인 성공시
                            .usernameParameter("email") //로그인화면에서 name=username이면 생략가능 / name=eamil이면 필수기입
                            .failureUrl("/members/login/error") //로그인실패시
                )
                .logout(logout ->
                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 처리
                                .logoutSuccessUrl("/") //로그아웃 성공시
                );



        http.csrf(config -> config.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
