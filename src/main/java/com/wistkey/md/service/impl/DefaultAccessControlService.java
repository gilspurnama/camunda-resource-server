package com.wistkey.md.service.impl;

import com.wistkey.md.dto.AccessControlDto;
import com.wistkey.md.dto.UserRoleDto;
import com.wistkey.md.exception.ResponseException;
import com.wistkey.md.model.RoleMetadata;
import com.wistkey.md.model.RolePermission;
import com.wistkey.md.model.UserRole;
import com.wistkey.md.repository.*;
import com.wistkey.md.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wistkey.md.util.ExceptionEnum.USER_NOT_ASSIGN_TO_ROLE;
import static com.wistkey.md.util.ExceptionEnum.USER_NOT_FOUND;
import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
public class DefaultAccessControlService implements AccessControlService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final RoleMetadataRepository roleMetadataRepository;

    @Override
    public AccessControlDto.GetResponse get(UserRoleDto.UserCredential credential) {

        Optional<UserRole> userRole = userRoleRepository.findByUserId(credential.getUserId());
        if(!userRoleRepository.findByUserId(credential.getUserId()).isPresent()) {
            throw new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.httpStatus(), USER_NOT_FOUND.code());
        }
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleNameIn(credential.getUserId(), credential.getRoleName().toArray(new String[0]));
        if(userRoles.isEmpty()) {
            throw new ResponseException(USER_NOT_ASSIGN_TO_ROLE.message(), USER_NOT_ASSIGN_TO_ROLE.httpStatus(), USER_NOT_ASSIGN_TO_ROLE.code());
        }
        List<String> differences = credential.getRoleName().stream()
                .filter(element -> !(userRoles.stream().map(UserRole::getRoleName).collect(Collectors.toList())).contains(element))
                .collect(Collectors.toList());
        return permissionsToDtoMapper(userRoles, differences);
    }

    @Override
    public ResponseEntity<?> head(String encodeRoleName) {
        List<String> decodeRoleName = Arrays.stream(new String(Base64.getUrlDecoder().decode(encodeRoleName.getBytes())).split(",")).collect(Collectors.toList());
        List<Long> redisValues = redisTemplate.opsForValue().multiGet(decodeRoleName);
        StringBuilder updatedTimestampResponse = new StringBuilder();
        for (int i = 0; i < decodeRoleName.size(); i++) {
            Long updatedTimestamp = redisValues.get(i);
            if (updatedTimestamp == null) {
                RoleMetadata roleMetadata = roleMetadataRepository.findByRoleName(decodeRoleName.get(i));
                updatedTimestamp = (roleMetadata == null ? LocalDateTime.now(UTC) : roleMetadata.getLatestUpdate()).atZone(UTC).toInstant().toEpochMilli();
                redisTemplate.opsForValue().set(decodeRoleName.get(i), updatedTimestamp);
            }
            if (updatedTimestampResponse.length() > 0) {
                updatedTimestampResponse.append(",");
            }
            updatedTimestampResponse.append(updatedTimestamp.toString());
        }
        return ResponseEntity.ok().header(HttpHeaders.CACHE_CONTROL, updatedTimestampResponse.toString()).build();
    }
    private AccessControlDto.GetResponse permissionsToDtoMapper(List<UserRole> userRoles, List<String> differences) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleNameIn(userRoles.stream().map(UserRole::getRoleName).collect(Collectors.toList()));

        Map<String, AccessControlDto.GetPermission> response = new HashMap<>();
        rolePermissions.forEach(rolePermission -> {
            AccessControlDto.GetPermission getPermission = response.computeIfAbsent(rolePermission.getRoleName(), treeSet -> new AccessControlDto.GetPermission());
            if (getPermission.getPermission() == null) {
                getPermission.setPermission(new HashMap<>());
            }
            Set<String> getUrls = getPermission.getPermission().computeIfAbsent(rolePermission.getPermissions().getMethod(), treeSet -> new TreeSet<>(antPathMatcher.getPatternComparator(null)));
            getUrls.addAll(Arrays.asList(rolePermission.getPermissions().getUrl().split(",")));
        });

        Map<String, RoleMetadata> roleMetadatas = roleMetadataRepository.findByRoleNameIn(response.keySet()).stream().collect(Collectors.toMap(RoleMetadata::getRoleName, Function.identity()));
        response.keySet().forEach(roleMetadata -> {
            Long updatedTimestamp = (roleMetadatas.isEmpty() ? LocalDateTime.now(UTC) : roleMetadatas.get(roleMetadata).getLatestUpdate()).atZone(UTC).toInstant().toEpochMilli();
            response.get(roleMetadata).setUpdatedTimestamp(updatedTimestamp);
            redisTemplate.opsForValue().set(roleMetadata, updatedTimestamp);
        });
        differences.forEach(roleName -> {
            Long updatedTimestamp = LocalDateTime.now(UTC).atZone(UTC).toInstant().toEpochMilli();
            AccessControlDto.GetPermission unassignedPermission = new AccessControlDto.GetPermission();
            unassignedPermission.setUpdatedTimestamp(updatedTimestamp);
            response.put(roleName, unassignedPermission);
            redisTemplate.opsForValue().set(roleName, updatedTimestamp);
        });
        return AccessControlDto.GetResponse.builder()
                .permissions(response)
                .build();
    }
}
