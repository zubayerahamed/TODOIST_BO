package com.zayaanit.module.documents;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDocumentReqDto {

	private String title;
	private String description;
	@NotNull(message = "File required")
	private MultipartFile file;
}
