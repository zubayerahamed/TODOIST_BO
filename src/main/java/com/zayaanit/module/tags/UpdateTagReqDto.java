package com.zayaanit.module.tags;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UpdateTagReqDto {

    @NotNull(message = "Tag id required")
    private Long id;

    @NotBlank(message = "Tag name required.")
    @Size(min = 1, max = 25, message = "Tag name must be 1 to 25 characters long")
    private String name;

}
