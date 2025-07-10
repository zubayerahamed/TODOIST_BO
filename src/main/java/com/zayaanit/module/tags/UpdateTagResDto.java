package com.zayaanit.module.tags;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monaum
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagResDto {
    private Long id;
    private String name;
    private String color;
    private Long workspaceId;
}
