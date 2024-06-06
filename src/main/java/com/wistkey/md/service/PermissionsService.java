package com.wistkey.md.service;

import com.wistkey.md.dto.PermissionDto;

import java.util.List;

public interface PermissionsService {

    PermissionDto.GetResponse create(PermissionDto.SaveRequest request);

    PermissionDto.GetResponse update(PermissionDto.SaveRequest request, String id);

    PermissionDto.GetResponse delete(String id);

    List<PermissionDto.GetPermission> get(String name);
}
