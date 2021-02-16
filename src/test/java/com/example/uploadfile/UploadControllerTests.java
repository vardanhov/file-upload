package com.example.uploadfile;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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
        mvc.perform(post("/uploadFile")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(multipartFile.getBytes())
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk());
    }
}
