package com.wistkey.md.repository;

import com.wistkey.md.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission,String> {

    List<RolePermission> findAllByRoleNameIn(List<String> roleName);

    List<RolePermission> findAllByRoleName(String roleName);

    Optional<RolePermission> getRolePermissionById(String id);
}
