package com.wistkey.md.controller;

import com.wistkey.md.dto.ResponseDto;
import com.wistkey.md.dto.UserRoleDto;
import com.wistkey.md.service.UserRoleService;
import com.wistkey.md.util.JWTDecoder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-role")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Operation(description = "Create new User and Role relation")
    @PostMapping
    public ResponseEntity<ResponseDto<UserRoleDto.GetResponseCreate>> create()  {

        UserRoleDto.GetResponseCreate userRole = userRoleService.create(JWTDecoder.getCredentialFromJWT());
        return ResponseEntity.ok(new ResponseDto<>(userRole));
    }

    @Operation(description = "Delete User and Role relation")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto<UserRoleDto.GetResponse>> delete(@PathVariable String id) {
        UserRoleDto.GetResponse userRole = userRoleService.delete(id);
        return ResponseEntity.ok(new ResponseDto<>(userRole));
    }

}

