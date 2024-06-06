package com.wistkey.md.service.impl;

import com.wistkey.md.dto.UserRoleDto;
import com.wistkey.md.exception.ResponseException;
import com.wistkey.md.model.UserRole;
import com.wistkey.md.repository.UserRoleRepository;
import com.wistkey.md.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.wistkey.md.util.ExceptionEnum.USER_ROLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DefaultUserRoleService implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRoleDto.GetResponseCreate create(UserRoleDto.UserCredential userCredential) {

        List<UserRole> userRoles = new ArrayList<>();
        userCredential.getRoleName().forEach(roleName -> {
            UserRole userRole = new UserRole();
            userRole.setRoleName(roleName);
            userRole.setUserId(userCredential.getUserId());
            userRoles.add(userRole);
        });
        userRoleRepository.saveAllAndFlush(userRoles);
        return UserRoleDto.GetResponseCreate.builder()
                .roleName(userCredential.getRoleName())
                .userId(userCredential.getUserId())
                .build();
    }

    @Override
    public UserRoleDto.GetResponse delete(String id) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> new ResponseException(
                USER_ROLE_NOT_FOUND.message(),
                USER_ROLE_NOT_FOUND.httpStatus(),
                USER_ROLE_NOT_FOUND.code()));
        userRoleRepository.delete(userRole);
        return UserRoleDto.GetResponse.builder()
                .roleName(userRole.getRoleName())
                .userId(userRole.getUserId())
                .build();
    }

}
