package com.example.uploadfile.service.iface;

import com.example.uploadfile.domain.Group;

public interface GroupService {
    Group findGroupByUserName(String username);
}
