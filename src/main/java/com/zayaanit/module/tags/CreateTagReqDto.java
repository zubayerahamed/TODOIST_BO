package com.zayaanit.module.tags;

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
public class CreateTagReqDto {

	@NotBlank(message = "Tag name required.")
	@Size(min = 1, max = 25, message = "Tag name must be 1 to 25 characters long")
	private String name;

	public Tag getBean() {
		return Tag.builder().name(name).build();
	}
}
