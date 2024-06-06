package com.wistkey.md.controller;

import com.wistkey.md.dto.ResponseDto;
import com.wistkey.md.dto.RolePermissionDto;
import com.wistkey.md.service.RolePermissionsService;
import com.wistkey.md.util.JWTDecoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role-permission")
public class RolePermissionController {

    private final RolePermissionsService rolePermissionsService;

    @Operation(description = "Create relation for Role and Permission entity")
    @PostMapping
    public ResponseEntity<ResponseDto<RolePermissionDto.GetResponse>> create(@Valid @RequestBody
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,content = {@Content(schema = @Schema(implementation = RolePermissionDto.SaveRequest.class))})
                                    RolePermissionDto.SaveRequest payload)  {

        RolePermissionDto.GetResponse rolePermission = rolePermissionsService.create(payload, JWTDecoder.getCredentialFromJWT().getEmail());
        return ResponseEntity.ok(new ResponseDto<>(rolePermission));
    }

    @Operation(description = "Delete a relation for Role and Permission entity")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto<RolePermissionDto.GetResponse>> delete(@PathVariable String id) {
        RolePermissionDto.GetResponse permissions = rolePermissionsService.delete(id);
        return ResponseEntity.ok(new ResponseDto<>(permissions));
    }

    @Operation(description = "Update a relation for Role and Permission entity")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto<RolePermissionDto.GetResponse>> update(@PathVariable String id,
                                                                             @Valid @RequestBody
                                                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,content = {@Content(schema = @Schema(implementation = RolePermissionDto.SaveRequest.class))})
                                                                             RolePermissionDto.UpdateRequest payload) {

        RolePermissionDto.GetResponse permission = rolePermissionsService.update(payload, id);
        return ResponseEntity.ok(new ResponseDto<>(permission));
    }

    @Operation(description = "Get all relation of Role and Permission entity with role name param")
    @GetMapping
    public ResponseEntity<ResponseDto<List<RolePermissionDto.GetResponse>>> get(@RequestParam(required = false) String roleName) {
        List<RolePermissionDto.GetResponse> permissions = rolePermissionsService.get(roleName);
        return ResponseEntity.ok(new ResponseDto<>(permissions));
    }

}

