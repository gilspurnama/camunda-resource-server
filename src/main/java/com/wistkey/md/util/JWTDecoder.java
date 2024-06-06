package com.wistkey.md.util;


import com.wistkey.md.dto.UserRoleDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public class JWTDecoder {
    public static String getEmailFromJWT() {
        Jwt jwt  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("email");
    }

    public static UserRoleDto.UserCredential getCredentialFromJWT() {
        Jwt jwt  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");
        return UserRoleDto.UserCredential.builder()
                .userId(jwt.getClaimAsString("sub"))
                .roleName(roles)
                .email(jwt.getClaimAsString("email"))
                .build();
    }

}
