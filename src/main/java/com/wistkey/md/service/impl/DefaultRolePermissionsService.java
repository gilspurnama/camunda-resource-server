package com.wistkey.md.service.impl;

import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.RolePermissionDto;
import com.wistkey.md.exception.ResponseException;
import com.wistkey.md.model.Permissions;
import com.wistkey.md.model.RoleMetadata;
import com.wistkey.md.model.RolePermission;
import com.wistkey.md.model.UserRole;
import com.wistkey.md.repository.PermissionsRepository;
import com.wistkey.md.repository.RoleMetadataRepository;
import com.wistkey.md.repository.RolePermissionRepository;
import com.wistkey.md.repository.UserRoleRepository;
import com.wistkey.md.service.PermissionsService;
import com.wistkey.md.service.RolePermissionsService;
import com.wistkey.md.util.AdminChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wistkey.md.util.ExceptionEnum.*;
import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
public class DefaultRolePermissionsService implements RolePermissionsService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionsRepository permissionsRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMetadataRepository roleMetadataRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public RolePermissionDto.GetResponse create(RolePermissionDto.SaveRequest request, String email) {
        AdminChecker.checkAdminAuthorization();
        if (!userRoleRepository.existsByRoleName(request.getRoleName())) {
            throw new ResponseException(ROLE_NOT_FOUND.message(), ROLE_NOT_FOUND.httpStatus(), ROLE_NOT_FOUND.code());
        }
        List<Permissions> extPermission = permissionsRepository.findByIdIn(request.getPermissionId());
        if (extPermission.size() != request.getPermissionId().length) {
            throw new ResponseException(PERMISSION_NOT_FOUND.message(), PERMISSION_NOT_FOUND.httpStatus(), PERMISSION_NOT_FOUND.code());
        }

        List<RolePermission> rolePermissionList = new ArrayList<>();
        extPermission.forEach(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permission.getId());
            rolePermission.setRoleName(request.getRoleName());
            rolePermission.setCreatedAt(LocalDateTime.now(UTC));
            rolePermission.setCreatedBy(email);
            rolePermissionList.add(rolePermission);
        });
        rolePermissionRepository.saveAllAndFlush(rolePermissionList);

        RoleMetadata roleMetadata = roleMetadataRepository.findByRoleName(request.getRoleName());
        if (roleMetadata == null) {
            roleMetadata = new RoleMetadata();
            roleMetadata.setRoleName(request.getRoleName());
        }
        roleMetadata.setLatestUpdate(LocalDateTime.now(UTC));
        roleMetadataRepository.save(roleMetadata);
        redisTemplate.delete(request.getRoleName());

        return RolePermissionDto.GetResponse.builder()
                .roleName(request.getRoleName())
                .permissionIds(request.getPermissionId())
                .build();
    }

    @Override
    public RolePermissionDto.GetResponse update(RolePermissionDto.UpdateRequest request, String id) {
        AdminChecker.checkAdminAuthorization();
        RolePermission rolePermission = rolePermissionRepository.findById(id).orElseThrow(() -> new ResponseException(ROLE_NOT_FOUND.message(), ROLE_NOT_FOUND.httpStatus(), ROLE_NOT_FOUND.code()));

        rolePermission.setPermissionId(request.getPermissionId());
        rolePermission.setRoleName(request.getRoleName());
        rolePermission.setUpdatedAt(LocalDateTime.now(UTC));

        rolePermissionRepository.saveAndFlush(rolePermission);

        RoleMetadata roleMetadata = roleMetadataRepository.findByRoleName(request.getRoleName());
        if (roleMetadata == null) {
            roleMetadata = new RoleMetadata();
            roleMetadata.setRoleName(request.getRoleName());
        }
        roleMetadata.setLatestUpdate(LocalDateTime.now(UTC));
        roleMetadataRepository.save(roleMetadata);
        redisTemplate.delete(request.getRoleName());

        return RolePermissionDto.GetResponse.builder()
                .roleName(request.getRoleName())
                .permissionId(request.getPermissionId())
                .build();
    }

    @Override
    public RolePermissionDto.GetResponse delete(String id) {
        AdminChecker.checkAdminAuthorization();
        RolePermission extRolePermission = rolePermissionRepository.getRolePermissionById(id).orElseThrow(() -> new ResponseException(ROLE_PERMISSION_NOT_FOUND.message(), ROLE_PERMISSION_NOT_FOUND.httpStatus(), ROLE_PERMISSION_NOT_FOUND.code()));
        rolePermissionRepository.delete(extRolePermission);

        RoleMetadata roleMetadata = roleMetadataRepository.findByRoleName(extRolePermission.getRoleName());
        if (roleMetadata == null) {
            roleMetadata = new RoleMetadata();
            roleMetadata.setRoleName(extRolePermission.getRoleName());
        }
        roleMetadata.setLatestUpdate(LocalDateTime.now(UTC));
        roleMetadataRepository.save(roleMetadata);
        redisTemplate.delete(extRolePermission.getRoleName());

        return RolePermissionDto.GetResponse.builder()
                .roleName(extRolePermission.getRoleName())
                .permissionId(extRolePermission.getPermissionId())
                .build();
    }

    @Override
    public List<RolePermissionDto.GetResponse> get(String roleName) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleName(roleName);
        return rolePermissions.stream().map(rp -> RolePermissionDto.GetResponse.builder()
                .id(rp.getId())
                .roleName(rp.getRoleName())
                .permissionId(rp.getPermissionId())
                .build()).collect(Collectors.toList());
    }

}
