package com.example.uploadfile.service.iface;

import com.example.uploadfile.domain.UserGroups;

import java.util.List;
import java.util.Set;


public interface GroupService {
    void createGroupEntity(String groupName);

    void addUserIntoGroup(Set<String> usernames, String groupName);

    void deleteUserFromGroup(String username);

    List<UserGroups> getGroups();

    void deleteGroup(Long id);
}
