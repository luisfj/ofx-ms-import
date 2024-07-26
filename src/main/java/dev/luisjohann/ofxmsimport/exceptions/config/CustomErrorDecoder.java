package dev.luisjohann.ofxmsimport.exceptions.config;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luisjohann.ofxmsimport.exceptions.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;
import javassist.NotFoundException;

public class CustomErrorDecoder implements ErrorDecoder {

   @Override
   public Exception decode(String methodKey, Response response) {
      ErrorResponse error = null;
      try (InputStream bodyIs = response.body().asInputStream()) {
         ObjectMapper mapper = new ObjectMapper();
         error = mapper.readValue(bodyIs, ErrorResponse.class);
      } catch (IOException e) {
         return new Exception(e.getMessage());
      }

      return switch (response.status()) {
         case 401 -> new UnauthorizedException(error);
         case 404 -> new NotFoundException(error.message() != null ? error.message() : "Not found");
         case 400 -> new UnsupportedOperationException(error.message());
         // case 503-> new ProductServiceNotAvailableException("Product Api is
         // unavailable");
         default -> new Exception("Exception while getting product details");
      };
   }
}