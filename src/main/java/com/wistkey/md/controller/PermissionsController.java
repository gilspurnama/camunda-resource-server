package com.wistkey.md.controller;

import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.ResponseDto;
import com.wistkey.md.service.PermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionsController {

    private final PermissionsService permissionsService;

    @PreAuthorize("hasRole('rs:permissions:create')")
    @Operation(description = "Create Permission for per module per method")
    @PostMapping
    public ResponseEntity<ResponseDto<PermissionDto.GetResponse>> create(@Valid @RequestBody
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,content = {@Content(schema = @Schema(implementation = PermissionDto.SaveRequest.class))})
                                    PermissionDto.SaveRequest payload)  {

        PermissionDto.GetResponse permission = permissionsService.create(payload);
        return ResponseEntity.ok(new ResponseDto<>(permission));
    }

    @PreAuthorize("hasRole('rs:permissions:delete')")
    @Operation(description = "Delete permission for permission id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto<PermissionDto.GetResponse>> delete(@PathVariable String id) {
        PermissionDto.GetResponse permissions = permissionsService.delete(id);
        return ResponseEntity.ok(new ResponseDto<>(permissions));
    }

    @PreAuthorize("hasRole('rs:permissions:update')")
    @Operation(description = "Update permission for individual permission id")
    @PutMapping(value = "/{permissionId}")
    public ResponseEntity<ResponseDto<PermissionDto.GetResponse>> update(@PathVariable String permissionId,
                                                                   @Valid @RequestBody
                                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,content = {@Content(schema = @Schema(implementation = PermissionDto.SaveRequest.class))})
                                                                   PermissionDto.SaveRequest payload) {

        PermissionDto.GetResponse permission = permissionsService.update(payload, permissionId);
        return ResponseEntity.ok(new ResponseDto<>(permission));
    }

    @PreAuthorize("hasRole('rs:permissions:read')")
    @Operation(description = "Get all permission list with parameter of permission name, if no permission name get all permissions")
    @GetMapping
    public ResponseEntity<ResponseDto<List<PermissionDto.GetPermission>>> get(@RequestParam(required = false) String name) {
        List<PermissionDto.GetPermission> permissions = permissionsService.get(name);
        return ResponseEntity.ok(new ResponseDto<>(permissions));
    }
}

