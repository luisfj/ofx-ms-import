package dev.luisjohann.ofxmsimport.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//        var http = httpSecurity
//                .csrf(CsrfConfigurer::disable)
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        .anyRequest().authenticated())
//                .oauth2ResourceServer((oauth) -> oauth
//                        .jwt(Customizer.withDefaults()));
//
//        return http.build();
//    }

}