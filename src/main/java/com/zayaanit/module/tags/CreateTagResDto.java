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
public class CreateTagResDto {
    private Long id;
    private String name;
    private String color;
    private Long workspaceId;

    public CreateTagResDto(Tag tag) {
        BeanUtils.copyProperties(tag, this);
    }
}
