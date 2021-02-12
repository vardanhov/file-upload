package com.example.uploadfile;

import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.excepion.FileContentTypeException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;
import com.example.uploadfile.service.UploadService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class UploadServiceTests {
    UploadService uploadService;
    MultipartFile multipartFile;
    UploadFileResponse uploadFileResponseMock;

    @Before
    public void initTest() {
        uploadService = new UploadService();
    }

    @Test(expected = FileNotFoundException.class)
    public void uploadNullFile_thenThrowFileNotFoundException(){
        uploadService.storeFile(null);
    }

    @Test(expected = FileContentTypeException.class)
    public void uploadNotPyFile_thenThrowFileContentTypeException(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt","txt","content".getBytes());
        uploadService.storeFile(multipartFile);
    }

    @Test
    public void uploadPyFile_positiveTest(){
        multipartFile = new MockMultipartFile("test.py", "test.py","py","content".getBytes());
        UploadFileResponse uploadFileResponseMock = Mockito.mock(UploadFileResponse.class);
        when(uploadFileResponseMock.getFileName()).thenReturn("test.py");
        when(uploadFileResponseMock.getFileType()).thenReturn("py");
        when(uploadFileResponseMock.getSize()).thenReturn(multipartFile.getSize());
        UploadFileResponse uploadFileResponseRes = uploadService.storeFile(multipartFile);
        Assert.assertEquals(uploadFileResponseRes.getFileName(),uploadFileResponseMock.getFileName());
        Assert.assertEquals(uploadFileResponseRes.getFileType(),uploadFileResponseMock.getFileType());
        Assert.assertEquals(uploadFileResponseRes.getSize(),uploadFileResponseMock.getSize());

    }
}
