package com.example.uploadfile.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@ActiveProfiles({"test","dev"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UploadControllerTest {

    @Autowired
    private MockMvc mvc;

    //TODO Fix test
    //@Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void uploadFileTest() throws Exception {
        mvc.perform(post("/api/whitelist/grant-access/{guid}", 1)
                .param("dateFrom", "2020-03-14")
                .param("timeFrom", "16:26:33")
                .param("dateTo", "2021-06-14")
                .param("timeTo", "16:26:33"))
                .andReturn();

        String text = "Text to be uploaded.";
        MockMultipartFile file = new MockMultipartFile("files", "unit.py", "text/plain", text.getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(file)
                .characterEncoding("UTF-8")
                .param("isDag", "true")
                .param("folder", "")
                .param("toOverride", "false"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void checkAccessTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/upload/check-access")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

}
