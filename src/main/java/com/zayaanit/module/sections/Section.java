package com.zayaanit.module.sections;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sections")
public class Section {

    static Object builder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(name = "workflow_id")
    private Long workflowId; 

    private Integer seqn;
}
