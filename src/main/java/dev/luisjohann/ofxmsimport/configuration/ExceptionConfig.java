package dev.luisjohann.ofxmsimport.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.luisjohann.ofxmsimport.exceptions.config.CustomErrorDecoder;
import feign.codec.ErrorDecoder;

@Configuration
public class ExceptionConfig {

   @Bean
   public ErrorDecoder errorDecoder() {
      return new CustomErrorDecoder();
   }
}
