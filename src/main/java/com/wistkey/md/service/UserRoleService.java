package com.wistkey.md.service;

import com.wistkey.md.dto.UserRoleDto;

public interface UserRoleService {

    UserRoleDto.GetResponseCreate create(UserRoleDto.UserCredential userCredential);

    UserRoleDto.GetResponse delete(String id);
}
