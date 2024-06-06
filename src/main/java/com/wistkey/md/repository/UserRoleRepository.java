package com.wistkey.md.repository;

import com.wistkey.md.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,String> {

    Optional<UserRole> findByUserId(String userId);
    List<UserRole> findByUserIdAndRoleNameIn(String userId, String[] roleName);

    Optional<UserRole> findByRoleName(String roleName);
    Boolean existsByRoleName(String roleName);
;}
