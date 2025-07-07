package com.zayaanit.module.tags;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import io.jsonwebtoken.lang.Collections;


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

}
