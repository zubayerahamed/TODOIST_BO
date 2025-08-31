package com.zayaanit.module.documents;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Slf4j
@Service
public class DocumentService extends BaseService {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("MMyyyy");

	@Autowired private DocumentRepo documentRepo;
	@Autowired private Environment env;

	public DocumentDownloadResponseDto download(Long id) throws IOException {
		Optional<Document> documentOp = documentRepo.findById(id);
		if(!documentOp.isPresent()) throw new CustomException("File not found", HttpStatus.BAD_REQUEST);

		Document doc = documentOp.get();
		String fileName = doc.getDocName() + doc.getDocExt();

		String storage = createAndGetStorageLocation();
		Path filePath = Paths.get(storage).resolve(fileName);
		long fileSize = Files.size(filePath);
		Resource resource = new org.springframework.core.io.PathResource(filePath);
		if (!resource.exists()) throw new CustomException("File not exist", HttpStatus.NOT_FOUND);

		return new DocumentDownloadResponseDto(doc.getOldName(), resource, filePath, fileSize);
	}

	public DocumentDownloadResponseDto download2(Long id) throws IOException {
		Optional<Document> documentOp = documentRepo.findById(id);
		if(!documentOp.isPresent()) throw new CustomException("File not found", HttpStatus.BAD_REQUEST);

		Document doc = documentOp.get();
		String fileName = doc.getDocName() + doc.getDocExt();

		String storage = createAndGetStorageLocation();
		Path filePath = Paths.get(storage).resolve(fileName);

		long fileSize = Files.size(filePath);
		byte[] data = Files.readAllBytes(filePath);
		ByteArrayResource resource = new ByteArrayResource(data);
		if (resource == null || !resource.exists()) throw new CustomException("File not exist", HttpStatus.NOT_FOUND);

		return new DocumentDownloadResponseDto(doc.getOldName(), resource, filePath, fileSize);
	}

	public List<DocumentResDto> getAll(Long referenceId){
		List<Document> documentList = documentRepo.findAllByReferenceId(referenceId);

		List<DocumentResDto> resDto = new ArrayList<>();
		documentList.stream().forEach(d -> {
			resDto.add(new DocumentResDto(d));
		});

		return resDto;
	}

	@Transactional
	public DocumentResDto create(CreateDocumentReqDto reqDto) {

		UUID fileName = UUID.randomUUID();
		String fileExt = getFileExtention(reqDto.getFile());

		// validate file size
		validateFileSize(reqDto.getFile());

		// Create storage path 
		String storage = createAndGetStorageLocation();

		// Upload file
		try (InputStream inputStream = reqDto.getFile().getInputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			int chunkNumber = 0;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				saveChunkToDisk(buffer, bytesRead, storage, fileName.toString() + fileExt, chunkNumber);
				chunkNumber++;
			}
		} catch (IOException e) {
			log.error("Error is {}, {}", e.getMessage(), e);
			throw new CustomException("Can't upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Save record to Document
		Document doc = Document.builder()
				.title(reqDto.getTitle())
				.description(reqDto.getDescription())
				.docName(fileName.toString())
				.oldName(reqDto.getFile().getOriginalFilename())
				.docExt(fileExt)
				.xtemp(true)
				.docSize(String.valueOf(reqDto.getFile().getSize()))
				.docType(reqDto.getFile().getContentType())
				.build();

		Document savedDoc = documentRepo.save(doc);

		return new DocumentResDto(savedDoc);
	}

	@Transactional
	public void delete(Long id) throws CustomException {
		Optional<Document> documentOp = documentRepo.findById(id);
		if(!documentOp.isPresent()) throw new CustomException("File not found", HttpStatus.BAD_REQUEST);

		Document doc = documentOp.get();
		String fileName = doc.getDocName() + doc.getDocExt();

		String storage = createAndGetStorageLocation();
		Path filePath = Paths.get(storage).resolve(fileName);

		// Check file exist, then delete the files from storage
		if (!Files.exists(filePath)) {
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new CustomException("Can't delete the file.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// Delete document record
		documentRepo.delete(doc);
	}

	@Transactional
	public void deleteAllByReferenceId(Long referenceId) throws CustomException {
		List<Document> documents = documentRepo.findAllByReferenceId(referenceId);

		for(Document doc : documents) {
			String fileName = doc.getDocName() + doc.getDocExt();

			String storage = createAndGetStorageLocation();
			Path filePath = Paths.get(storage).resolve(fileName);

			// Check file exist, then delete the files from storage
			if (!Files.exists(filePath)) {
				try {
					Files.delete(filePath);
				} catch (IOException e) {
					log.error(e.getMessage());
					throw new CustomException("Can't delete the file.", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

			// Delete document record
			documentRepo.delete(doc);
		}
	}

	private void saveChunkToDisk(byte[] chunk, int bytesRead, String directory, String fileName, int chunkNumber) throws IOException {
		Path targetPath = Paths.get(directory, fileName);
		Files.write(targetPath, chunk, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	private String createAndGetStorageLocation() {
		String storage = new StringBuilder(env.getProperty("app.documents-location"))
					.append(File.separator)
					.append(SDF.format(new Date())).toString();

		// Make Directory if not exist
		Path path = Paths.get(storage);
		if (!(Files.exists(path))) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new CustomException("Can't create directory", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return storage;
	}

	private void validateFileSize(MultipartFile file) {
		String maxFileSize = env.getProperty("app.max-file-size-kb");
		if(file.getSize() > Long.valueOf(maxFileSize)  * 1024) {
			throw new CustomException("File size too big. Max file size support : " + maxFileSize + "Kb", HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("null")
	private String getFileExtention(MultipartFile file) {
		if (file == null || StringUtils.isBlank(file.getOriginalFilename())) return null;
		int indx =  file.getOriginalFilename().lastIndexOf(".");
		return file.getOriginalFilename().substring(indx);
	}
}
