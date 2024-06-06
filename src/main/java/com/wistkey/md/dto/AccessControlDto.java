package com.wistkey.md.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessControlDto {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetResponse{
        private Map<String, GetPermission> permissions;
    }

    @Data
    public static class GetPermission{
        private Map<String, Set<String>> permission;
        private Long updatedTimestamp;
    }

}
