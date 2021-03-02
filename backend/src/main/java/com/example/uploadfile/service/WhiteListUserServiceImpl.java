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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Override
    public WhiteListUserDto createWhiteListUserByUserName(String username, Authentication authentication) {
        checkUserAdminRights(authentication);
        //TODO обработать проблемы лдап и ексепшены
        User user = null;
        try {
            user = userRepository.findUserByUsername(username);
        } catch (Exception e) {
            throw new UserNotFoundException("Проблемы с настройками LDAP");
        }
        if (user == null) {
            throw new UserNotFoundException("Не найден пользователь");
        }
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUserName(user.getUsername());
        WhiteListUser whiteListUserResponse = whiteListUserRepository.saveAndFlush(whiteListUser);
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserResponse);
        return whiteListUserDto;
    }

    @Override
    public List<WhiteListUserDto> getAllUsers(Authentication authentication) {
        checkUserAdminRights(authentication);
        return whiteListUserRepository.findAll()
                .stream().map(UserMapper::toWhiteListUserDto).collect(Collectors.toList());
    }

    //Fixme не работает с дто
    @Override
    public void grantAccessById(String dateFrom, String timeFrom, String dateTo, String timeTo, Integer guid, Authentication authentication) {

        checkUserAdminRights(authentication);
        WhiteListUser whiteListUser = whiteListUserRepository.getOne(guid);
        whiteListUser.setDateFrom(LocalDateTime.of(LocalDate.parse(dateFrom), LocalTime.parse(timeFrom)));
        whiteListUser.setId(guid);
        whiteListUser.setDateTo(LocalDateTime.of(LocalDate.parse(dateTo), LocalTime.parse(timeTo)));
        whiteListUserRepository.save(whiteListUser);
    }

    //Fixme не работает с дто
    @Override
    public void limitAccessById(Integer guid, Authentication authentication) {
        checkUserAdminRights(authentication);
        WhiteListUser whiteListUser = whiteListUserRepository.getOne(guid);
        whiteListUser.setId(guid);
        whiteListUser.setDateTo(null);
        whiteListUser.setDateFrom(null);
        whiteListUserRepository.save(whiteListUser);
    }

    //TODO переделать с использованием authorities
    @Override
    public void checkUserAdminRights(Authentication authentication) {
                WhiteListUserDto whiteListUserDto = toWhiteListUserDto(whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
        if (!whiteListUserDto.getAdmin()) throw new RuntimeException("Недостаточно прав");
    }
}
