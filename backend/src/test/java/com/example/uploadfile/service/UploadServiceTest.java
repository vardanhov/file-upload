package com.example.uploadfile.service;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles({"test","dev"})
@AutoConfigureMockMvc
public class UploadServiceTest {
}
