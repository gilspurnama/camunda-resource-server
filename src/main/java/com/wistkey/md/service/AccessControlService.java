package com.wistkey.md.service;

import com.wistkey.md.dto.AccessControlDto;
import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.UserRoleDto;
import com.wistkey.md.model.UserRole;
import org.springframework.http.ResponseEntity;

public interface AccessControlService {

    AccessControlDto.GetResponse get(UserRoleDto.UserCredential credential);

    ResponseEntity<?> head(String id);

}
