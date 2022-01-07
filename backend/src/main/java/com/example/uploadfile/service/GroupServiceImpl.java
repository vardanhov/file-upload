package com.example.uploadfile.service;

import com.example.uploadfile.domain.ParticipantEntity;
import com.example.uploadfile.domain.UserGroups;
import com.example.uploadfile.excepion.GroupException;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.repo.ParticipantEntityRepository;
import com.example.uploadfile.repo.UserGroupsEntityRepository;
import com.example.uploadfile.service.iface.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class GroupServiceImpl implements GroupService {
    final static private Integer NAME_POSITION = 3;
    private final UserGroupsEntityRepository groupsEntityRepository;
    private final ParticipantEntityRepository participantEntityRepository;

    @Autowired
    public GroupServiceImpl(UserGroupsEntityRepository groupsEntityRepository,
                            ParticipantEntityRepository participantEntityRepository) {
        this.groupsEntityRepository = groupsEntityRepository;
        this.participantEntityRepository = participantEntityRepository;
    }


    //Fixme если группа уже существует
    //TODO воткнуть лог
    @Transactional
    public void createGroupEntity(String groupName) {
        checkIfExistsByGroupName(groupName);
        UserGroups userGroups = new UserGroups();
        userGroups.setGroupName(groupName);
        groupsEntityRepository.save(userGroups);
    }

    //Fixme - надо убрать создание группы, если группы нет, то пользаку показывать сообщение, чтоб сначала создал
    //TODO воткнуть лог
    @Transactional
    public void addUserIntoGroup(Set<String> usernames, String groupName) {
        UserGroups userGroups = groupsEntityRepository.findUserGroupsByGroupName(groupName).
                orElseThrow(() -> new GroupException("Группа не найдена"));

        Set<ParticipantEntity> collect = usernames.stream().map(name -> {
            Optional<ParticipantEntity> entity = participantEntityRepository.findParticipantEntityByUsername(name);
            return entity.orElseGet(() -> new ParticipantEntity(name));
        }).peek(participantEntity -> participantEntity.setUserGroups(userGroups)).collect(Collectors.toSet());

        participantEntityRepository.saveAll(collect);
    }

    @Transactional
    public void deleteUserFromGroup(String username) {
        //TODO воткнуть лог
        int status = participantEntityRepository.deleteByUsername(username);
        if (status != 1) {
            //TODO воткнуть лог
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<UserGroups> getGroups() {
        return groupsEntityRepository.findAll();
    }

    @Override
    public void deleteGroup(Long id) {
        checkUserExistInGroup(id);
        int status = groupsEntityRepository.removeById(id);
        if (status <= 0) {
            throw new GroupException("Can not find a group");
        }
    }

    private void checkIfExistsByGroupName(String groupName) {
        if (groupsEntityRepository.findUserGroupsByGroupName(groupName).isPresent()) {
            new GroupException("There is group with such name");
        }
    }

    private void checkUserExistInGroup(Long id) {
        List<ParticipantEntity> participantEntities = participantEntityRepository.findParticipantEntitiesByGroupId(id);
        if (!participantEntities.isEmpty()) {
            throw new RuntimeException("There are users in group");
        }
    }
}

