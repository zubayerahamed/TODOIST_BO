package com.zayaanit.module.tags;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagResDto {

    private Long id;
    private String name;
    private Long workspaceId;

    public TagResDto(Tag tag) {
        BeanUtils.copyProperties(tag, this);
    }
}
