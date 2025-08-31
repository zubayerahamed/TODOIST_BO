package com.zayaanit.module.documents;

import java.io.IOException;
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

//	@GetMapping("/download/{id}")
//	public ResponseEntity<Resource> getFile(@PathVariable Long id) throws IOException{
//		DocumentDownloadResponseDto resDto = documentService.download(id);
//
//		// Detect file's content type
//		String contentType = Files.probeContentType(resDto.getFilePath());
//		if (contentType == null) {
//			contentType = "application/octet-stream"; // fallback
//		}
//
//		return ResponseEntity.ok()
//				.contentType(MediaType.parseMediaType(contentType))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resDto.getResource().getFilename() + "\"")
//				.body(resDto.getResource());
//	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> getFile2(@PathVariable Long id) throws IOException{
		DocumentDownloadResponseDto resDto = documentService.download2(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resDto.getResource().getFilename() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(resDto.getFileSize())
				.body(resDto.getResource());
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
