package com.wistkey.md.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserRoleDto {

    @Getter
    public static class SaveRequest {
        @NotBlank(message = "role name is required")
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetResponse{
        private String id;
        private String userId;
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetResponseCreate{
        private String id;
        private String userId;
        private List<String> roleName;
    }

    @Getter
    public static class UpdateUserRoleRequest{
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserCredential{
        private String userId;
        private String email;
        private List<String> roleName;
    }

}
