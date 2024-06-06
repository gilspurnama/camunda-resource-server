package com.wistkey.md.model;

import com.wistkey.md.service.impl.DefaultAccessControlService;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "role_metadata",indexes = @Index(columnList = "role_name"))
public class RoleMetadata {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "role_name",nullable = false)
    private String roleName;

    @Column(name = "latest_update",nullable = false)
    private LocalDateTime latestUpdate;
}
