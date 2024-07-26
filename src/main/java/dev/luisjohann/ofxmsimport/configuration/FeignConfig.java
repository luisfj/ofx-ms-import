package dev.luisjohann.ofxmsimport.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "dev.luisjohann.ofxmsimport.clients")
public class FeignConfig {

}
