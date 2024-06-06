package com.wistkey.md.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_role",indexes = @Index(columnList = "user_id"))
public class UserRole {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Column(name = "role_name",nullable = false)
    private String roleName;
}
