package com.example.uploadfile;

import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.excepion.FileContentTypeException;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;
import com.example.uploadfile.service.UploadService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UploadServiceTests {

    @TestConfiguration
    static class UploadServiceTestContextConfiguration {
        @Bean
        public UploadService uploadService() {
            return new UploadService();
        }
    }

    @Autowired
    private UploadService uploadService;

    @MockBean
    MockMultipartFile mockMultipartFileMock;

    MultipartFile multipartFile;

    @Test(expected = FileNotFoundException.class)
    public void storeNullFile_thenThrowFileNotFoundException(){
        uploadService.storeFile(null,false);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenStoreFileThrowFileNotFoundException_checkText(){
        uploadService.storeFile(null,false);
        ExpectedException.none().expectMessage("Cannot find file");
    }

    @Test(expected = FileNotFoundException.class)
    public void whenStoreFileThrowFileNotFoundException_returnNull(){
        Assert.assertNull(uploadService.storeFile(null,false));
    }

    @Test(expected = FileNameException.class)
    public void storeNotPyFile_thenThrowFileNameException(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt", MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "content".getBytes());
        uploadService.storeFile(multipartFile,false);
    }

    @Test(expected = FileContentTypeException.class)
    public void whenThrowFileContentTypeException_checkText(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt", MediaType.ALL_VALUE,
                "content".getBytes());
        uploadService.storeFile(multipartFile,false);
        ExpectedException.none().expectMessage("Invalid content type");
    }


    @Test(expected = FileContentTypeException.class)
    public void whenStoreFileThrowFileContentTypeException_returnNull(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt", MediaType.ALL_VALUE,
                "content".getBytes());
        Assert.assertNull(uploadService.storeFile(multipartFile,false));
    }

    @Test(expected = FileNameException.class)
    public void whenFileNameNull_throwFileNameException(){
        multipartFile = new MockMultipartFile("test.py", "content".getBytes());
       // MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        uploadService.storeFile(mockMultipartFileMock,false);
    }

    @Test(expected = FileNameException.class)
    public void whenStoreFileThrowFileNameException_checkText(){
        multipartFile = new MockMultipartFile("test.txt","content".getBytes());
      //  MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        uploadService.storeFile(mockMultipartFileMock,false);
        ExpectedException.none().expectMessage("Invalid file name");
    }

    @Test(expected = FileNameException.class)
    public void whenStoreFileThrowFileNameException_returnNull(){
        multipartFile = new MockMultipartFile("test.py","content".getBytes());
        //MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        Assert.assertNull(uploadService.storeFile(mockMultipartFileMock,false));
    }


    @Test(expected = FileStorageException.class)
    public void whenStoreFileThrowFileStorageException_returnNull(){
        multipartFile = new MockMultipartFile("test.py", "test.py","py","content".getBytes());
        UploadService uploadServiceMock = Mockito.mock(UploadService.class);
        when(uploadServiceMock.storeFile(multipartFile,false)).thenThrow(FileStorageException.class);
        Assert.assertNull(uploadServiceMock.storeFile(multipartFile,false));
    }

    @Test(expected = FileStorageException.class)
    public void whenStoreFileThrowIOException_thenThrowFileStorageException() throws IOException {
        when(mockMultipartFileMock.getContentType()).thenReturn(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn("test.py");
        doThrow(IOException.class).when(mockMultipartFileMock).transferTo(any(File.class));
        uploadService.storeFile(mockMultipartFileMock,false);
    }

    @Test
    public void uploadPyFile_positiveTest() {
        multipartFile = new MockMultipartFile(
                "test.py",
                "test.py",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "content".getBytes());

      //  doNothing().when(mockMultipartFileMock).transferTo(any(File.class));

        UploadFileResponse uploadFileResponseMock = Mockito.mock(UploadFileResponse.class);

        when(uploadFileResponseMock.getFileName()).thenReturn("test.py");
        when(uploadFileResponseMock.getFileType()).thenReturn(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        when(uploadFileResponseMock.getSize()).thenReturn(multipartFile.getSize());

        UploadFileResponse uploadFileResponseRes = uploadService.storeFile(multipartFile,false);

        Assert.assertEquals(uploadFileResponseRes.getFileName(),uploadFileResponseMock.getFileName());
        Assert.assertEquals(uploadFileResponseRes.getFileType(),uploadFileResponseMock.getFileType());
        Assert.assertEquals(uploadFileResponseRes.getSize(),uploadFileResponseMock.getSize());

      //  verify(mockMultipartFileMock).transferTo(any(File.class));
    }
}
