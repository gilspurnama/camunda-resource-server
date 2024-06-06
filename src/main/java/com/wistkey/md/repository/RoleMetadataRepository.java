package com.wistkey.md.repository;

import com.wistkey.md.model.RoleMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public interface RoleMetadataRepository extends JpaRepository<RoleMetadata,String> {

    RoleMetadata findByRoleName(String roleName);

    List<RoleMetadata> findByRoleNameIn(Collection<String> roleName);
}
