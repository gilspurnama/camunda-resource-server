package com.wistkey.md.model;

import lombok.Data;

import java.time.LocalDateTime;
import javax.persistence.*;

@Data
public class AuditEntity {

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;
}
