package com.zayaanit.module.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;


import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Monaum
 * @since Jul 9, 2025
 */

@Service
public class SectionService extends BaseService {

    @Autowired private SectionRepo sectionRepo;

    @Transactional
    public SectionResDto createSection (CreateSectionReqDto reqDto) throws CustomException {

        Section section = reqDto.getBean();
        section = sectionRepo.save(section);

        return new SectionResDto(section);
    }

    public List<SectionResDto> getAllSections(Long projectId) {
        List<Section> section = sectionRepo.findAllByProjectId(projectId);

        if (section.isEmpty()) {
            return Collections.emptyList();
        }

        List<SectionResDto> responseData = new ArrayList<>();
        section.stream().forEach(p -> {
            responseData.add(new SectionResDto(p));
        });

        return responseData;
    }

    public SectionResDto findSectionById(Long id, Long projectId) throws CustomException {

        Optional<Section> sectionOp = sectionRepo.findByIdAndProjectId(id, projectId);

        if (!sectionOp.isPresent()) {
            throw new CustomException("Section not exist", HttpStatus.NOT_FOUND);
        }

        return new SectionResDto(sectionOp.get());
    }


}
