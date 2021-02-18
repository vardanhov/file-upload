package com.example.uploadfile;

import com.example.uploadfile.controller.UploadController;
import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.service.UploadService;
import org.apache.catalina.Globals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UploadController.class, UploadService.class })
@SpringBootTest
public class UploadControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    MockMultipartFile multipartFile;

    @Before
    public void setup() {

        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        multipartFile = new MockMultipartFile(
                "file",
                "hello.py",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());
    }

    @Test
    public void uploadFileBadRequest_get400Error() throws Exception {
        mvc.perform(multipart("/uploadFile")
                .content("{}".getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void uploadFileWrongHeaders_get400Error() throws Exception {
        mvc.perform(multipart("/uploadFile")
                .content(multipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Cookie", "someCookie"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void uploadFileAtInvalidURL_get404Error() throws Exception {
        mvc.perform(post("/uploadFileR")
           .content(multipartFile.getBytes())
           .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().is4xxClientError());
    }

    @Test
    public void uploadFileWrongReqType_get405Error() throws Exception {
        mvc.perform(get("/uploadFile")
                .content(multipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void uploadFile_positiveTest() throws Exception {
        //MockMultipartFile fileSpy = Mockito.spy(multipartFile);
        //Mockito.doNothing().when(fileSpy).transferTo(any(File.class));

       // ResultActions resultActions =
        mvc.perform(multipart("/uploadFile")
           .file(multipartFile)
           .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isOk());
    }
}
