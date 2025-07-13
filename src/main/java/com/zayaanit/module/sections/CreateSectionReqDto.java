package com.zayaanit.module.sections;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSectionReqDto {

    private Long workflowId;
    private Long projectId;
    @NotBlank(message = "Section name is required.")
    @Size(min = 1, max = 25, message = "Section name must be 1 to 25 characters long")
    private String name;

    public Section getBean() {
        return Section.builder()
                .workflowId(workflowId)
                .projectId(projectId)
                .name(name)
                .build();
    }
}
