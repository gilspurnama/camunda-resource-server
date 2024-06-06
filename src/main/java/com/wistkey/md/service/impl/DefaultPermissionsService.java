package com.wistkey.md.service.impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.UserRoleDto;
import com.wistkey.md.exception.ResponseException;
import com.wistkey.md.model.Permissions;
import com.wistkey.md.repository.PermissionsRepository;
import com.wistkey.md.service.PermissionsService;
import com.wistkey.md.util.AdminChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.wistkey.md.util.ExceptionEnum.PERMISSION_NOT_FOUND;
import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
public class DefaultPermissionsService implements PermissionsService {

    private final PermissionsRepository permissionsRepository;

    @Override
    public PermissionDto.GetResponse create(PermissionDto.SaveRequest request) {
        AdminChecker.checkAdminAuthorization();
        List<Permissions> permissionsList = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now(UTC);

        for (Map.Entry<String, Map<String, List<String>>> moduleEntry: request.getPermissions().entrySet()) {
            for (Map.Entry<String, List<String>> methodEntry : moduleEntry.getValue().entrySet()) {
                Permissions permissions = new Permissions();
                permissions.setModule(moduleEntry.getKey());
                permissions.setMethod(methodEntry.getKey());
                permissions.setName(request.getName());
                permissions.setCreatedAt(createdAt);
                permissions.setUrl(String.join(",", methodEntry.getValue()));
                permissionsList.add(permissions);
            }
        }
        permissionsRepository.saveAllAndFlush(permissionsList);
        return PermissionDto.GetResponse.builder()
                .name(request.getName())
                .permissions(request.getPermissions())
                .build();
    }

    @Override
    public PermissionDto.GetResponse update(PermissionDto.SaveRequest request, String id) {
        AdminChecker.checkAdminAuthorization();
        Permissions extPermissions = permissionsRepository.getPermissionsById(id).orElseThrow(() -> new ResponseException(PERMISSION_NOT_FOUND.message(), PERMISSION_NOT_FOUND.httpStatus(), PERMISSION_NOT_FOUND.code()));
        LocalDateTime createdAt = LocalDateTime.now(UTC);

        for (Map.Entry<String, Map<String, List<String>>> moduleEntry: request.getPermissions().entrySet()) {
            extPermissions.setModule(moduleEntry.getKey());
            for (Map.Entry<String, List<String>> methodEntry : moduleEntry.getValue().entrySet()) {
                extPermissions.setMethod(methodEntry.getKey());
                extPermissions.setName(request.getName());
                extPermissions.setUpdatedAt(createdAt);
                extPermissions.setUrl(String.join(",", methodEntry.getValue()));
            }
        }
        permissionsRepository.saveAndFlush(extPermissions);
        return PermissionDto.GetResponse.builder()
                .name(request.getName())
                .permissions(request.getPermissions())
                .build();
    }

    @Override
    public PermissionDto.GetResponse delete(String id) {
        AdminChecker.checkAdminAuthorization();
        Permissions extPermission = permissionsRepository.getPermissionsById(id).orElseThrow(() -> new ResponseException(PERMISSION_NOT_FOUND.message(), PERMISSION_NOT_FOUND.httpStatus(), PERMISSION_NOT_FOUND.code()));
        permissionsRepository.delete(extPermission);
        return PermissionDto.GetResponse.builder()
                .name(extPermission.getName())
                .build();
    }

    @Override
    public List<PermissionDto.GetPermission> get(String name) {
        List<Permissions> permissions = new ArrayList<>();
        if (name == null || name.isEmpty()) {
            permissions = permissionsRepository.findAll();
        } else {
            permissions = permissionsRepository.findAllByName(name);
        }

        return permissions.stream().map(permission -> PermissionDto.GetPermission.builder()
                .id(permission.getId())
                .name(permission.getName())
                .method(permission.getMethod())
                .module(permission.getModule())
                .url(permission.getUrl()).build()).collect(Collectors.toList());

    }
}
