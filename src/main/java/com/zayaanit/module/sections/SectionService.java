package com.zayaanit.module.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Monaum
 *
 * @since Jul 9, 2025
 */
@Service
public class SectionService extends BaseService {

    @Autowired
    private SectionRepo sectionRepo;

    @Transactional
    public SectionResDto createSection(CreateSectionReqDto reqDto) throws CustomException {

        Section section = reqDto.getBean();
        section.setSeqn(1);
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

    public SectionResDto findSectionById(Long id) throws CustomException {
        Optional<Section> sectionOp = sectionRepo.findById(id);
        if (!sectionOp.isPresent()) {
            throw new CustomException("Section not exist", HttpStatus.NOT_FOUND);
        }

        return new SectionResDto(sectionOp.get());
    }

    @Transactional
    public SectionResDto updateSection(UpdateSectionReqDto reqDto) throws CustomException {
        if (reqDto.getId() == null) {
            throw new CustomException("Section id required", HttpStatus.BAD_REQUEST);
        }

        Optional<Section> sectionOp = sectionRepo.findById(reqDto.getId());
        if (!sectionOp.isPresent()) {
            throw new CustomException("Section not exist", HttpStatus.NOT_FOUND);
        }

        Section section = sectionOp.get();
        BeanUtils.copyProperties(reqDto, section);

        section = sectionRepo.save(section);
        return new SectionResDto(section);
    }

    @Transactional
    public void deleteSection(Long id) throws CustomException {
        Optional<Section> sectionOp = sectionRepo.findById(id);
        if (!sectionOp.isPresent()) {
            throw new CustomException("Section not exist", HttpStatus.NOT_FOUND);
        }

        sectionRepo.delete(sectionOp.get());
    }

}
