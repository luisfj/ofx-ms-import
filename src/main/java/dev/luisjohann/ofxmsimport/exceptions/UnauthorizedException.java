package dev.luisjohann.ofxmsimport.exceptions;

import dev.luisjohann.ofxmsimport.exceptions.config.ErrorResponse;
import lombok.Getter;

public class UnauthorizedException extends RuntimeException {

   @Getter
   ErrorResponse response;

   public UnauthorizedException(ErrorResponse response) {
      super(response.message());
      this.response = response;
   }

}