package com.zayaanit.module.documents;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

@RestApiController
@RequestMapping("/api/v1/documents")
public class DocumentController {

	@Autowired private DocumentService documentService;

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable Long id){
		DocumentDownloadResponseDto resDto = documentService.download(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resDto.getFileName());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resDto.getResource());
	}

	@GetMapping("/all/{referenceid}")
	public ResponseEntity<SuccessResponse<List<DocumentResDto>>> getAll(@PathVariable Long referenceid){
		List<DocumentResDto> resData = documentService.getAll(referenceid);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@PostMapping("/upload")
	public ResponseEntity<SuccessResponse<DocumentResDto>> upload(@Valid @ModelAttribute CreateDocumentReqDto reqDto){
		DocumentResDto resData = documentService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<DocumentResDto>> delete(@PathVariable Long id){
		documentService.delete(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_NO_CONTENT, null);
	} 
}
