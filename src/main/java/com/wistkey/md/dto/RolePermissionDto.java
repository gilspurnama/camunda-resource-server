package com.wistkey.md.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RolePermissionDto {

    @Getter
    public static class SaveRequest {
        @NotBlank(message = "role name id is required")
        private String roleName;

        @Size(min = 1, message = "permission id is required")
        private String[] permissionId;

        private String createdBy;
    }


    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetResponse{
        private String id;
        private String roleName;
        private String permissionId;
        private String[] permissionIds;
        private String createdBy;
        private String createdAt;
        private String updatedAt;
    }

    @Getter
    public static class UpdateRequest {
        @NotBlank(message = "role name id is required")
        private String roleName;

        @NotBlank(message = "permission id is required")
        private String permissionId;
    }
}
