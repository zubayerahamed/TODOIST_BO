package com.zayaanit.module.documents;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDownloadResponseDto {

	private String fileName;
	private Resource resource;
}
