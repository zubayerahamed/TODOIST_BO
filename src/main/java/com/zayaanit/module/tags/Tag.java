package com.zayaanit.module.tags;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tag extends AbstractModel<Long>{

    private static final long serialVersionUID = 2572842083641054917L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 25)
    private String name;

    @Column(name = "color", length = 10)
    private String color;

    @Column(name = "workspace_id")
    private Long workspaceId;

}
