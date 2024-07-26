package dev.luisjohann.ofxmsimport.exceptions.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.luisjohann.ofxmsimport.exceptions.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(UnauthorizedException.class)
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
   @ResponseBody
   public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
      printLog(ex);
      return ex.getResponse();
   }

   @ExceptionHandler(UnsupportedOperationException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ResponseBody
   public ErrorResponse handleUnsupportedOperationException(UnsupportedOperationException ex) {
      printLog(ex);
      return new ErrorResponse("Ação não suportada", ex.getMessage());
   }

   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   @ResponseBody
   public ErrorResponse handleDefaultException(Exception ex) {
      printLog(ex);
      return new ErrorResponse("Erro interno", ex.getMessage());
   }

   private void printLog(Exception ex) {
      ex.printStackTrace();
   }
}
