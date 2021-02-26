package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.iface.WhiteListUserService;
import com.example.uploadfile.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.uploadfile.util.UserMapper.toWhiteListUser;
import static com.example.uploadfile.util.UserMapper.toWhiteListUserDto;

@Service
public class WhiteListUserServiceImpl implements WhiteListUserService {

    private final UserRepository userRepository;
    private final WhiteListUserRepository whiteListUserRepository;

    @Autowired
    public WhiteListUserServiceImpl(UserRepository userRepository, WhiteListUserRepository whiteListUserRepository) {
        this.userRepository = userRepository;
        this.whiteListUserRepository = whiteListUserRepository;
    }
    

    @Transactional
    public WhiteListUserDto createWhiteListUserByUserName(String username, Authentication authentication) {
        checkUserAdminRights(authentication);
        //TODO обработать проблемы лдап и ексепшены
        User user = null;
        try{
            user=userRepository.findUserByUsername(username);
        }catch (Exception e){
            throw new UserNotFoundException("Проблемы с настройками LDAP");
        }
        if (user==null){
            throw new UserNotFoundException("Не найден пользователь");
        }
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUserName(user.getUsername());
        WhiteListUser whiteListUserResponse = whiteListUserRepository.saveAndFlush(whiteListUser);
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserResponse);
        return whiteListUserDto;
    }

    public List<WhiteListUserDto> getAllUsers(Authentication authentication) {
        checkUserAdminRights(authentication);
        return whiteListUserRepository.findAll()
                .stream().map(UserMapper::toWhiteListUserDto).collect(Collectors.toList());
    }


    public void grantAccessById(String dateTimeFrom, String dateTimeTo, Integer guid, Authentication authentication) {
        checkUserAdminRights(authentication);
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserRepository.getOne(guid));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        whiteListUserDto.setFrom(LocalDateTime.parse(dateTimeFrom, formatter));
        whiteListUserDto.setId(guid);
        whiteListUserDto.setTo(LocalDateTime.parse(dateTimeTo));
        whiteListUserRepository.save(toWhiteListUser(whiteListUserDto));
    }

//TODO поправить работу со временем

    public void limitAccessById(Integer guid, Authentication authentication) {
        checkUserAdminRights(authentication);
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserRepository.getOne(guid));
        whiteListUserDto.setTo(LocalDateTime.now());
        whiteListUserRepository.save(toWhiteListUser(whiteListUserDto));
    }

//TODO переделать с использованием authorities
    public void checkUserAdminRights(Authentication authentication){
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
        if (!whiteListUserDto.getAdmin()) throw new RuntimeException("Недостаточно прав");
    }
}
