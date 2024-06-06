package com.wistkey.md.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role_permission")
public class RolePermission extends AuditEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "role_name",nullable = false)
    private String roleName;

    @Column(name = "permission_id")
    private String permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id",updatable = false,insertable = false)
    private Permissions permissions;
}
