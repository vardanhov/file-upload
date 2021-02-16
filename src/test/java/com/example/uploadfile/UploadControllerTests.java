package com.example.uploadfile;

import com.example.uploadfile.controller.UploadController;
import com.example.uploadfile.service.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UploadController.class, UploadService.class })
@SpringBootTest
public class UploadControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    MultipartFile multipartFile;

    @Before
    public void setup() {

        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        multipartFile = new MockMultipartFile("test.py", "test.py","py","content".getBytes());
    }

    @Test
    public void uploadFile_get404Error() throws Exception {
        mvc.perform(post("/uploadFileR")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(multipartFile.getBytes())
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void uploadFile_positiveTest() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.py",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile fileSpy = Mockito.spy(file);
        Mockito.doNothing().when(fileSpy).transferTo(any(File.class));

        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(context).build();

        ResultActions resultActions = mockMvc.perform(multipart("/uploadFile").file(fileSpy)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        resultActions.andExpect(status().isOk());
    }
}
