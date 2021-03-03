package com.example.uploadfile.service;

import com.example.uploadfile.domain.Group;
import com.example.uploadfile.repo.GroupRepository;
import com.example.uploadfile.service.iface.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;


@Service
public class GroupServiceImpl implements GroupService {
    final static private Integer NAME_POSITION = 3;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group findGroupByUserName(String username) {
        Iterable<Group> groups = groupRepository.findAll();
        List<Group> list = new ArrayList<>();
        groups.forEach(list::add);

        return findGroupFromGroupList(list, username);
    }

    public   Group findGroupFromGroupList(List<Group> list, String username) {
        for (Group group:list) {
            for (Name name:group.getMembers()) {
                if (name.get(NAME_POSITION).endsWith(username)) {
                    return group;
                }
            }
        }
        return null;
    }
}

