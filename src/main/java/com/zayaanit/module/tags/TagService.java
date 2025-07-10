package com.zayaanit.module.tags;

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
public class TagService extends BaseService {

    @Autowired
    private TagRepo tagRepo;

    @Transactional
    public TagResDto createTag(CreateTagReqDto reqDto) throws CustomException {

        Tag tag = reqDto.getBean();
        tag.setWorkspaceId(loggedinUser().getWorkspace().getId());
        tag = tagRepo.save(tag);

        return new TagResDto(tag);
    }

    public List<TagResDto> getAllTags() {
        List<Tag> tag = tagRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());

        if (tag.isEmpty()) {
            return Collections.emptyList();
        }

        List<TagResDto> responseData = new ArrayList<>();
        tag.stream().forEach(p -> {
            responseData.add(new TagResDto(p));
        });

        return responseData;
    }

    public TagResDto findById(Long id) throws CustomException {

        Optional<Tag> tagOp = tagRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());

        if (!tagOp.isPresent()) {
            throw new CustomException("Tag not exist", HttpStatus.NOT_FOUND);
        }

        return new TagResDto(tagOp.get());
    }

    @Transactional
    public UpdateTagResDto updateTag(UpdateTagReqDto reqDto) throws CustomException {
        if (reqDto.getId() == null) {
            throw new CustomException("Project id required", HttpStatus.BAD_REQUEST);
        }

        Optional<Tag> tagOp = tagRepo.findByIdAndWorkspaceId(reqDto.getId(), loggedinUser().getWorkspace().getId());
        if (!tagOp.isPresent()) {
            throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);
        }

        Tag existobj = tagOp.get();
        BeanUtils.copyProperties(reqDto, existobj);

        existobj = tagRepo.save(existobj);

        return UpdateTagResDto.builder()
                .id(existobj.getId())
                .name(existobj.getName())
                .workspaceId(existobj.getWorkspaceId())
                .build();
    }

    @Transactional
    public void deleteById(Long id) throws CustomException {

        Optional<Tag> tagOp = tagRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());

        if (!tagOp.isPresent()) {
            throw new CustomException("Tag not exist", HttpStatus.NOT_FOUND);
        }

        Tag tag = tagOp.get();
        tagRepo.delete(tag);
    }
}
