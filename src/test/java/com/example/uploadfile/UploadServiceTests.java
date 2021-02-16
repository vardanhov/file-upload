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
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

public class UploadServiceTests {
    UploadService uploadService;
    MultipartFile multipartFile;

    @Before
    public void initTest() {
        uploadService = new UploadService();
    }

    @Test(expected = FileNotFoundException.class)
    public void storeNullFile_thenThrowFileNotFoundException(){
        uploadService.storeFile(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenStoreFileThrowFileNotFoundException_checkText(){
        uploadService.storeFile(null);
        ExpectedException.none().expectMessage("Cannot find file");
    }

    @Test(expected = FileNotFoundException.class)
    public void whenStoreFileThrowFileNotFoundException_returnNull(){
        Assert.assertNull(uploadService.storeFile(null));
    }

    @Test(expected = FileContentTypeException.class)
    public void storeNotPyFile_thenThrowFileContentTypeException(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt","txt","content".getBytes());
        uploadService.storeFile(multipartFile);
    }

    @Test(expected = FileContentTypeException.class)
    public void whenThrowFileContentTypeException_checkText(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt","txt","content".getBytes());
        uploadService.storeFile(multipartFile);
        ExpectedException.none().expectMessage("invalid content type");
    }


    @Test(expected = FileContentTypeException.class)
    public void whenStoreFileThrowFileContentTypeException_returnNull(){
        multipartFile = new MockMultipartFile("test.txt", "test.txt","txt","content".getBytes());
        Assert.assertNull(uploadService.storeFile(multipartFile));
    }

    @Test(expected = FileNameException.class)
    public void whenFileNameNull_throwFileNameException(){
        multipartFile = new MockMultipartFile("test.py","content".getBytes());
        MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn("py");
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        uploadService.storeFile(mockMultipartFileMock);
    }

    @Test(expected = FileNameException.class)
    public void whenStoreFileThrowFileNameException_checkText(){
        multipartFile = new MockMultipartFile("test.py","content".getBytes());
        MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn("py");
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        uploadService.storeFile(mockMultipartFileMock);
        ExpectedException.none().expectMessage("Invalid file name");
    }

    @Test(expected = FileNameException.class)
    public void whenStoreFileThrowFileNameException_returnNull(){
        multipartFile = new MockMultipartFile("test.py","content".getBytes());
        MockMultipartFile mockMultipartFileMock = Mockito.mock(MockMultipartFile.class);
        when(mockMultipartFileMock.getContentType()).thenReturn("py");
        when(mockMultipartFileMock.getOriginalFilename()).thenReturn(null);
        Assert.assertNull(uploadService.storeFile(mockMultipartFileMock));
    }


    @Test(expected = FileStorageException.class)
    public void whenStoreFileThrowFileStorageException_returnNull(){
        multipartFile = new MockMultipartFile("test.py", "test.py","py","content".getBytes());
        UploadService uploadServiceMock = Mockito.mock(UploadService.class);
        when(uploadServiceMock.storeFile(multipartFile)).thenThrow(FileStorageException.class);
        Assert.assertNull(uploadServiceMock.storeFile(multipartFile));
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
