//package com.example.uploadfile;
//
//import com.example.uploadfile.controller.UploadController;
//import com.example.uploadfile.domain.User;
//import com.example.uploadfile.domain.WhiteListUser;
//import com.example.uploadfile.dto.WhiteListUserDto;
//import com.example.uploadfile.excepion.FileNotFoundException;
//import com.example.uploadfile.excepion.UserNotFoundException;
////import com.example.uploadfile.repo.UserRepository;
//import com.example.uploadfile.excepion.WhiteListUserException;
//import com.example.uploadfile.repo.WhiteListUserRepository;
//import com.example.uploadfile.service.UploadService;
//import com.example.uploadfile.service.WhiteListUserService;
//import com.example.uploadfile.util.UserMapper;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//
///*
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {WhiteListUserService.class})
//public class WhiteListUserServiceTests {
//
//    @Autowired
//    private WhiteListUserService whiteListUserService;
//
//    @MockBean
//    private WhiteListUserRepository whiteListUserRepository;
//
////    @MockBean
////    private UserRepository userRepository;
//
//    @Test(expected = NullPointerException.class)
//    public void ifWhiteListNull_thenThrowException(){
//        Mockito.when(whiteListUserRepository.findAll()).thenReturn(null);
//        whiteListUserService.getAllWhiteListUsers();
//    }
//
//    @Test
//    public void ifWhiteListEmpty_thenResultListSizeZero(){
//        Mockito.when(whiteListUserRepository.findAll()).thenReturn(new ArrayList<>());
//        Assert.assertEquals(whiteListUserService.getAllWhiteListUsers().size(),0);
//    }
//
//    @Test
//    public void getAllWhiteListUsers_positiveTest(){
//        WhiteListUser whiteListUser1 = new WhiteListUser();
//        WhiteListUser whiteListUser2 = new WhiteListUser();
//        WhiteListUser whiteListUser3 = new WhiteListUser();
//
//        whiteListUser1.setId(1);
//        whiteListUser1.setUsername("Alex");
//        whiteListUser1.setCreateDate(1L);
//        whiteListUser1.setTrigger(2L);
//
//        whiteListUser2.setId(2);
//        whiteListUser2.setUsername("Bob");
//        whiteListUser2.setCreateDate(2L);
//        whiteListUser2.setTrigger(3L);
//
//        whiteListUser3.setId(3);
//        whiteListUser3.setUsername("Mike");
//        whiteListUser3.setCreateDate(3L);
//        whiteListUser3.setTrigger(4L);
//
//        List<WhiteListUser> whiteListUsersExpected = new ArrayList<>();
//
//        whiteListUsersExpected.add(whiteListUser1);
//        whiteListUsersExpected.add(whiteListUser2);
//        whiteListUsersExpected.add(whiteListUser3);
//
//        Mockito.when(whiteListUserRepository.findAll()).thenReturn(whiteListUsersExpected);
//
//        List<WhiteListUserDto> whiteListUserDtos = whiteListUserService.getAllWhiteListUsers();
//
//        assertThat(whiteListUserDtos).hasSize(3);
//        assertThat(whiteListUserDtos).extracting(WhiteListUserDto::getUsername).containsOnly("Alex","Bob","Mike");
//        assertThat(whiteListUserDtos).extracting(WhiteListUserDto::getId).containsOnly(1,2,3);
//
//        verify(whiteListUserRepository).findAll();
//    }
//
//    @Test(expected = WhiteListUserException.class)
//    public void createWhiteList_withWhiteListUserException(){
//        WhiteListUserDto whiteListUserDtoMock = Mockito.mock(WhiteListUserDto.class);
//        Mockito.when(whiteListUserDtoMock.getUsername()).thenReturn(null);
//        whiteListUserService.createOrUpdateWhiteList(whiteListUserDtoMock);
//
//        verify(whiteListUserDtoMock).getUsername();
//    }
//
//    @Test(expected = WhiteListUserException.class)
//    public void createWhiteList_withWhiteListUserException_checkText(){
//        WhiteListUserDto whiteListUserDtoMock = Mockito.mock(WhiteListUserDto.class);
//        Mockito.when(whiteListUserDtoMock.getUsername()).thenReturn(null);
//        whiteListUserService.createOrUpdateWhiteList(whiteListUserDtoMock);
//        ExpectedException.none().expectMessage("Can not find White List");
//
//        verify(whiteListUserDtoMock).getUsername();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void createWhiteList_withWhiteListUserDtoNull(){
//        whiteListUserService.createOrUpdateWhiteList(null);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void createWhiteList_withWhiteListUserNull(){
//        whiteListUserService.createOrUpdateWhiteList(null);
//    }
//
//}*/
