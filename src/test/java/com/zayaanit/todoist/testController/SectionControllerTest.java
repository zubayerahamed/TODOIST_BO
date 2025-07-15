package com.zayaanit.todoist.testController;

import com.zayaanit.module.sections.CreateSectionReqDto;
import com.zayaanit.module.sections.SectionResDto;
import com.zayaanit.module.sections.SectionService;
import com.zayaanit.module.sections.UpdateSectionReqDto;
import com.zayaanit.module.tags.UpdateTagResDto;
import com.zayaanit.todoist.BaseAuthenticatedControllerTest;
import com.zayaanit.todoist.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class SectionControllerTest extends BaseAuthenticatedControllerTest {

    @MockBean
    SectionService sectionService;

    @Test
    void testGetAllSections() throws Exception {
        mockMvc.perform(get(BASE_SECTION_URL)
                        .param("projectId", "1")
                        .headers(authorizedHeaders()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testFindSectionById() throws Exception {
        mockMvc.perform(get(BASE_SECTION_URL + "/{id}", 1L)
                        .headers(authorizedHeaders()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateSection() throws Exception {
        CreateSectionReqDto reqDto = new CreateSectionReqDto();
        reqDto.setName("Planning");
        reqDto.setProjectId(1L);

        mockMvc.perform(post(BASE_SECTION_URL)
                        .headers(authorizedHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(reqDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testUpdateSection() throws Exception {
        UpdateSectionReqDto reqDto = new UpdateSectionReqDto();
        reqDto.setId(1L);
        reqDto.setName("Execution");

        SectionResDto resDto = new SectionResDto();
        resDto.setId(1L);
        resDto.setName("Execution");
        // Assuming the service is mocked to return the updated section
        when(sectionService.updateSection(any(UpdateSectionReqDto.class))).thenReturn(resDto);

        mockMvc.perform(put(BASE_SECTION_URL)
                        .headers(authorizedHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(reqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
