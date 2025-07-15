package com.zayaanit.todoist.testController;

import com.zayaanit.module.tags.CreateTagReqDto;
import com.zayaanit.module.tags.TagService;
import com.zayaanit.module.tags.UpdateTagReqDto;
import com.zayaanit.module.tags.UpdateTagResDto;
import com.zayaanit.todoist.BaseAuthenticatedControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TagControllerTest extends BaseAuthenticatedControllerTest {

    @MockBean
    private TagService tagService;


    @Test
    void testGetAllTags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_TAG_URL)
                        .headers(authorizedHeaders()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTag() throws Exception {
        CreateTagReqDto reqDto = new CreateTagReqDto();
        reqDto.setName("Important");

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_TAG_URL)
                        .headers(authorizedHeaders())
                        .content(toJson(reqDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateTag() throws Exception {
        UpdateTagReqDto reqDto = new UpdateTagReqDto();
        reqDto.setId(1L);
        reqDto.setName("Updated Tag");

        UpdateTagResDto resDto = new UpdateTagResDto();
        resDto.setId(1L);
        resDto.setName("Updated Tag");

        when(tagService.updateTag(any(UpdateTagReqDto.class))).thenReturn(resDto);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_TAG_URL)
                        .headers(authorizedHeaders())
                        .content(toJson(reqDto)))
                .andExpect(status().isOk());
    }


    @Test
    void testDeleteTag() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_TAG_URL + "/{id}", 1L)
                        .headers(authorizedHeaders()))
                .andExpect(status().isOk());
    }
}
