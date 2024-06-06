package com.wistkey.md.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class PermissionDto {

    @Getter
    public static class SaveRequest {
        @NotBlank(message = "role name id is required")
        private String name;

        private Map<String, Map<String, List<String>>> permissions;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetResponse{
        private String id;
        private String name;
        private Object permissions;
        private Long updatedTimestamp;
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPermission{
        private String id;
        private String name;
        private String method;
        private String module;
        private String url;
    }

}
