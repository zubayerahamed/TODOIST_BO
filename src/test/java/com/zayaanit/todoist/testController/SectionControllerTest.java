package com.zayaanit.todoist.testController;

import com.zayaanit.module.sections.CreateSectionReqDto;
import com.zayaanit.module.sections.UpdateSectionReqDto;
import com.zayaanit.todoist.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class SectionControllerTest extends BaseControllerTest {

    @Test
    void testGetAllSections() throws Exception {
        mockMvc.perform(get(BASE_SECTION_URL).param("projectId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testFindSectionById() throws Exception {
        mockMvc.perform(get(BASE_SECTION_URL + "/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateSection() throws Exception {
        CreateSectionReqDto reqDto = new CreateSectionReqDto();
        reqDto.setName("Planning");
        reqDto.setProjectId(1L);

        mockMvc.perform(post(BASE_SECTION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(reqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateSection() throws Exception {
        UpdateSectionReqDto reqDto = new UpdateSectionReqDto();
        reqDto.setId(1L);
        reqDto.setName("Execution");

        mockMvc.perform(put(BASE_SECTION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(reqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteSection() throws Exception {
        mockMvc.perform(delete(BASE_SECTION_URL + "/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
