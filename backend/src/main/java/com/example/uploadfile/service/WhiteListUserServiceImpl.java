package com.example.uploadfile.service;

import com.example.uploadfile.domain.AccessUserListEntity;
import com.example.uploadfile.domain.ParticipantEntity;
import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.UserGroups;
import com.example.uploadfile.domain.enums.UserProfileType;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.GroupException;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.excepion.WhiteListUserException;
import com.example.uploadfile.logger.Markers;
import com.example.uploadfile.repo.AccessUserListEntityRepository;
import com.example.uploadfile.repo.ParticipantEntityRepository;
import com.example.uploadfile.repo.UserGroupsEntityRepository;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.service.iface.WhiteListUserService;
import com.example.uploadfile.util.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.uploadfile.util.UserMapper.toWhiteListUserDto;


@Service
@Slf4j
public class WhiteListUserServiceImpl implements WhiteListUserService {

    private final UserRepository userRepository;

    // private final GroupEntityRepository groupEntityRepository;
    private final UserGroupsEntityRepository userGroupsRepository;
    private final ParticipantEntityRepository participantEntityRepository;
    private final AccessUserListEntityRepository accessUserListEntityRepository;


    @Autowired
    public WhiteListUserServiceImpl(UserRepository userRepository,
                                    UserGroupsEntityRepository userGroupsRepository,
                                    ParticipantEntityRepository participantEntityRepository,
                                    AccessUserListEntityRepository accessUserListEntityRepository) {
        this.userRepository = userRepository;
        this.userGroupsRepository = userGroupsRepository;
        this.participantEntityRepository = participantEntityRepository;

        this.accessUserListEntityRepository = accessUserListEntityRepository;
    }

    @Transactional
    @Override
    public WhiteListUserDto createWhiteListUserByUserName(String username, String groupName, Authentication authentication) {
        checkIfUserExistInWhiteList(username);

        //FIXME ????
        Optional<UserGroups> userGroup = userGroupsRepository.findUserGroupsByGroupName(groupName);
        if (!userGroup.isPresent()) {
            throw new GroupException("There is now group");
        }
        User user;
        try {
            user = userRepository.findUserByUsername(username);
            //TODO воткнуть лог
        } catch (Exception e) {
            //TODO воткнуть лог
            throw new UserNotFoundException("Проблемы с настройками LDAP");
        }

        if (user == null) {
            //TODO воткнуть лог
            throw new UserNotFoundException("Не найден пользователь");
        }
        ParticipantEntity participantEntity = participantEntityRepository.findParticipantEntityByUsername(username).
                orElseThrow(() -> new UserNotFoundException("Не найден пользователь"));

        AccessUserListEntity whiteListUser = new AccessUserListEntity();
        whiteListUser.setParticipantEntity(participantEntity);
        whiteListUser.setAddedBy(authentication.getName());
        whiteListUser.setRole(UserProfileType.VIEWER.getName());

        AccessUserListEntity accessUserListResponse = accessUserListEntityRepository.saveAndFlush(whiteListUser);
        WhiteListUserDto whiteListUserDto = toWhiteListUserDto(accessUserListResponse);
        whiteListUserDto.setGroup(participantEntity.getUserGroups().getGroupName());
        log.info(Markers.ADMIN.getMarker(), "[Added user] {} | admin={} | message={} ",
                "AdminEvent",
                authentication.getName(),
                "Добавлен пользователь " + user.getUsername());

        return whiteListUserDto;
    }


    @Override
    public List<WhiteListUserDto> getAllUsers(Authentication authentication) {
        //Если в таблице user_entity найден пользователь из вайтлиста и у него есть группа будет указана она
        // в противном случае останется группа из вайтлиста

        List<WhiteListUserDto> whiteListUserDtoList = accessUserListEntityRepository.findAll().stream()
                .map(UserMapper::toWhiteListUserDto)
                .collect(Collectors.toList());

        return whiteListUserDtoList.stream().peek(whiteListUserDto -> {
            Optional<ParticipantEntity> participantEntity = participantEntityRepository.
                    findParticipantEntityByUsername(whiteListUserDto.getUserName());
            if (participantEntity.isPresent()) {
                if (participantEntity.get().getUserGroups()!=null) {
                    whiteListUserDto.setGroup(participantEntity.get().getUserGroups().getGroupName());
                }
            }

        }).collect(Collectors.toList());
    }


    @Override
    public void grantAccessById(String dateFrom,
                                String timeFrom,
                                String dateTo,
                                String timeTo,
                                Long guid,
                                Authentication authentication) {
        //TODO воткнуть лог
        AccessUserListEntity accessUserListEntity = accessUserListEntityRepository.getOne(guid);
        accessUserListEntity.setDateFrom(LocalDateTime.of(LocalDate.parse(dateFrom), LocalTime.parse(timeFrom)).truncatedTo(ChronoUnit.HOURS));
        accessUserListEntity.setId(guid);
        accessUserListEntity.setDateTo(LocalDateTime.of(LocalDate.parse(dateTo), LocalTime.parse(timeTo)).truncatedTo(ChronoUnit.HOURS));
        if (accessUserListEntity.getRole().equals(UserProfileType.VIEWER.getName())) {
            accessUserListEntity.setRole(UserProfileType.USER.getName());
        }
        accessUserListEntityRepository.save(accessUserListEntity);
    }

    @Override
    public void limitAccessById(Long guid, Authentication authentication) {
        //TODO воткнуть лог
        AccessUserListEntity accessUserListEntity = accessUserListEntityRepository.getOne(guid);
        accessUserListEntity.setId(guid);
        accessUserListEntity.setDateTo(null);
        accessUserListEntity.setDateFrom(null);
        if (accessUserListEntity.getRole().equals(UserProfileType.USER.getName())) {
            accessUserListEntity.setRole(UserProfileType.VIEWER.getName());
        }
        accessUserListEntityRepository.save(accessUserListEntity);
    }

    @Override
    public void findByUserName(String username) {
        Optional<ParticipantEntity> participantEntity = participantEntityRepository.findParticipantEntityByUsername(username);
        if (!participantEntity.isPresent()) {
            throw new WhiteListUserException("Нет такого пользователя");
        }
    }


    @Scheduled(cron = "${application.role.schedule.cron}", zone = "UTC")
    public void updateExpiredUser() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int activatedUserCount = accessUserListEntityRepository.activateUser(currentDateTime, UserProfileType.USER.getName());
        log.info("Количество активированных пользователей - " + activatedUserCount);
        int deactivatedUserCount = accessUserListEntityRepository.deactivateUser(currentDateTime, UserProfileType.VIEWER.getName());
        log.info("Количество деактивированных пользователей - " + deactivatedUserCount);
    }

    @Override
    public void deleteWhiteListUserByGuid(Long guid) {
        //TODO логгер
        accessUserListEntityRepository.deleteById(guid);
    }

    @Override
    public void checkUser(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Такого пользователя нет");
        }

        Optional<ParticipantEntity> participantEntity = participantEntityRepository.findParticipantEntityByUsername(username);

        if (participantEntity.get().getUserGroups()!=null){
            throw new RuntimeException("Пользователь уже добавлен в группу");
        }
    }


    private void checkIfUserExistInWhiteList(final String username) {
        if (accessUserListEntityRepository.getWhiteListUserByUserName(username) != null) {
            throw new WhiteListUserException("User already exist in white list ");
        }
    }
}
