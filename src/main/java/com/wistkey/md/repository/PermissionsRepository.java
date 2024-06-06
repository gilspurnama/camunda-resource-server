package com.wistkey.md.repository;

import com.wistkey.md.model.Permissions;
import com.wistkey.md.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions,String> {

    Optional<Permissions> getPermissionsById(String id);
    List<Permissions> findByIdIn(String[] id);

    List<Permissions> findAllByName(String name);
}
