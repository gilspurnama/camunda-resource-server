package com.wistkey.md.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permissions",indexes = @Index(columnList = "name"))
public class Permissions extends AuditEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "module")
    private String module;

    @Column(name = "method")
    private String method;

    @Column(name = "url", columnDefinition = "text")
    private String url;
}
