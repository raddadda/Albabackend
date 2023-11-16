package com.jobstore.jobstore.config;

import com.jobstore.jobstore.jwt.JwtTokenFilter;
import com.jobstore.jobstore.service.MemberService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;
    private static String secretKey = "my-secret-key-123123";
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                .csrf(csrf ->  csrf.disable()  // csrf 토큰 비활성화

                )
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig ->
                                frameOptionsConfig.disable()
                        )
                )
                //.addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 특정 URL에 대한 권한 설정.

                .addFilterBefore(new JwtTokenFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers(
                                    new AntPathRequestMatcher("/**"),
                                    new AntPathRequestMatcher("/css/**"),
                                    new AntPathRequestMatcher("/images/**"),
                                    new AntPathRequestMatcher("/js/**"),
                                    new AntPathRequestMatcher("/h2-console/**"),
                                    new AntPathRequestMatcher("/**")
                            ).permitAll().anyRequest().authenticated();
                })

                .formLogin((formLogin) -> {
                    formLogin
//                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginPage("/login") // 인증필요한 주소로 접속하면 이 주소로 이동시킴
                            .loginProcessingUrl("/login") // 스프링 시큐리티가 로그인 자동 진행
                            .defaultSuccessUrl("/"); // 로그인이 정상적이면 "/" 로 이동

                });


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
