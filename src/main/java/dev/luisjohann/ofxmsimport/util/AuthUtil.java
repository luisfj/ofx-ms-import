package dev.luisjohann.ofxmsimport.util;

import dev.luisjohann.ofxmsimport.exceptions.UnauthorizedException;
import dev.luisjohann.ofxmsimport.exceptions.config.ErrorResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtil {
    public static AuthUserDetail getUserDetails(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            String name = jwt.getClaimAsString("name"); // Nome de usuário do JWT
            String email = jwt.getClaimAsString("email"); // Email do JWT
//            String roles = jwt.getClaimAsString("realm_access"); // Roles (como exemplo)

            return new AuthUserDetail(authentication.getName(), email, name);
        }
        throw new UnauthorizedException(new ErrorResponse("Unauthorized", "Usuário não possui permissão"));
    }
}
