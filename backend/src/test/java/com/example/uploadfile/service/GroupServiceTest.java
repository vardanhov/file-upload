package com.example.uploadfile.service;

import com.example.uploadfile.domain.ParticipantEntity;
import com.example.uploadfile.domain.UserGroups;
import com.example.uploadfile.repo.ParticipantEntityRepository;
import com.example.uploadfile.repo.UserGroupsEntityRepository;
import com.example.uploadfile.service.iface.GroupService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles({"test", "dev"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupServiceTest {


    @Autowired
    private GroupService groupService;

    @Autowired
    private UserGroupsEntityRepository groupEntityRepository;

    @Autowired
    private ParticipantEntityRepository userEntityRepository;


    //TODO Fix test
  //  @Test
    @Order(1)
    public void createGroupEntity() {
        String groupName = "User Group";
        groupService.createGroupEntity(groupName);
        Optional<UserGroups> userGroupsByGroupName = groupEntityRepository.findUserGroupsByGroupName(groupName);
        assertTrue(userGroupsByGroupName.isPresent());
        assertEquals(userGroupsByGroupName.get().getGroupName(), groupName);
    }

/*    @Test
    public void createExistedGroupEntityThrowException() {
        String groupName = "user";
        assertThrows(GroupException.class,() -> groupService.createGroupEntity(groupName));

    }*/

    /*@Test
    @Order(2)
    public void addUserIntoGroupTest() {
        String groupName = "User Group";
        Set<String> users = Stream.of("anton", "oleg", "Sergey").collect(Collectors.toSet());
        groupService.addUserIntoGroup(users, groupName);
        List<ParticipantEntity> entities = userEntityRepository.findAll().stream()
                .filter(user -> users.contains(user.getUsername())).peek(System.out::println)
                .collect(Collectors.toList());
        assertNotNull(entities);
        assertEquals(entities.size(), users.size());
    }

    @Test
    @Order(3)
    public void deleteUserFromGroupTest() {
        String username = "anton";
        groupService.deleteUserFromGroup(username);
        Optional<ParticipantEntity> participantEntityByUsername = userEntityRepository.findParticipantEntityByUsername(username);
        assertFalse(participantEntityByUsername.isPresent());
    }*/
    //TODO Fix test
      @Test
    @Order(2)
    public void deleteGroupEntity() {
        String groupName = "User Group";
        groupService.deleteGroup(1L);

    }
}
