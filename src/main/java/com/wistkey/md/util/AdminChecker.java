package com.wistkey.md.util;

import com.wistkey.md.exception.ResponseException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static com.wistkey.md.util.ExceptionEnum.USER_NOT_ADMIN_UNAUTHORIZED;

public class AdminChecker {
    public static void checkAdminAuthorization() {
        Jwt jwt  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        if (!roles.contains("administrator")) {
            throw new ResponseException(USER_NOT_ADMIN_UNAUTHORIZED.message(), USER_NOT_ADMIN_UNAUTHORIZED.httpStatus(), USER_NOT_ADMIN_UNAUTHORIZED.code());
        }
    }
}
