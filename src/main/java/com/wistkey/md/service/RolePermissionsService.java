package com.wistkey.md.service;

import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.RolePermissionDto;

import java.util.List;

public interface RolePermissionsService {

    List<RolePermissionDto.GetResponse> get(String roleName);

    RolePermissionDto.GetResponse create(RolePermissionDto.SaveRequest request, String email);

    RolePermissionDto.GetResponse update(RolePermissionDto.UpdateRequest request, String id);

    RolePermissionDto.GetResponse delete(String id);
}
