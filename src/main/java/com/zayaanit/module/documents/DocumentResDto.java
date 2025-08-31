package com.zayaanit.module.documents;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResDto {
	private Long id;
	private Long referenceId;
	private String title;
	private String description;
	private String docName;
	private String docExt;
	private String docSize;
	private String docType;

	public DocumentResDto(Document obj) {
		BeanUtils.copyProperties(obj, this);
	}
}