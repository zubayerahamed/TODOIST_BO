package com.zayaanit.module.tags;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;


@Service
public class TagService extends BaseService {

    @Autowired private TagRepo tagRepo;

    public CreateTagResDto createTag(CreateTagReqDto reqDto) throws CustomException {
        Tag tag = reqDto.getBean();
        tag.setWorkspaceId(loggedinUser().getWorkspace().getId());

        tag = tagRepo.save(tag);
        return new CreateTagResDto(tag);
    }

    public List<CreateTagResDto> getAllTags() {
        List<Tag> tag = tagRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());
        if(tag.isEmpty()) return Collections.emptyList();

         List<CreateTagResDto> responseData = new ArrayList<>();
		tag.stream().forEach(p -> {
			responseData.add(new CreateTagResDto(p));
		});
        return responseData;
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
            .color(existobj.getColor())
            .workspaceId(existobj.getWorkspaceId())
            .build();
    }

}
