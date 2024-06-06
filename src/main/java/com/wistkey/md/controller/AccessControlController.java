package com.wistkey.md.controller;

import com.wistkey.md.dto.AccessControlDto;
import com.wistkey.md.dto.PermissionDto;
import com.wistkey.md.dto.ResponseDto;
import com.wistkey.md.service.AccessControlService;
import com.wistkey.md.util.JWTDecoder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access-control")
public class AccessControlController {

    private final AccessControlService accessControlService;

    @Operation(description = "Get user permission based on JWT")
    @GetMapping
    public ResponseEntity<ResponseDto<AccessControlDto.GetResponse>> get() {
        AccessControlDto.GetResponse payload = accessControlService.get(JWTDecoder.getCredentialFromJWT());
        return ResponseEntity.ok(new ResponseDto<>(payload));
    }

    @Operation(description = "Refresh cache")
    @RequestMapping(method = RequestMethod.HEAD, value = "/{roleName}")
    public ResponseEntity<?> head(@PathVariable String roleName) {
        return accessControlService.head(roleName);
    }
}
