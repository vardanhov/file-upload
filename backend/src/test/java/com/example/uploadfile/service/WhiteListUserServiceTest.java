package com.example.uploadfile.service;

import com.example.uploadfile.domain.AccessUserListEntity;
import com.example.uploadfile.repo.AccessUserListEntityRepository;
import com.example.uploadfile.service.iface.WhiteListUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles({"test","dev"})
@AutoConfigureMockMvc
public class WhiteListUserServiceTest {

    @Autowired
    private WhiteListUserService whiteListUserService;

    @Autowired
    private AccessUserListEntityRepository accessUserListEntityRepository;

    @Test
    public void updateExpiredUserTest() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String roleUser = "ROLE_USER";
        String roleViewer = "ROLE_VIEWER";
        List<AccessUserListEntity> accessUserListBeforeDeactivate = accessUserListEntityRepository.findWhiteListUsersToDeactivate(currentDateTime, roleUser);
        List<AccessUserListEntity> accessUserListBeforeActivate = accessUserListEntityRepository.findWhiteListUsersToActivate(currentDateTime, roleViewer);

        whiteListUserService.updateExpiredUser();

        List<AccessUserListEntity> accessUserListAfterDeactivate = accessUserListEntityRepository.findWhiteListUsersToDeactivate(currentDateTime, roleUser);
        List<AccessUserListEntity> accessUserListAfterActivate = accessUserListEntityRepository.findWhiteListUsersToActivate(currentDateTime, roleViewer);

        assertNotNull(accessUserListBeforeDeactivate);
        assertNotNull(accessUserListBeforeActivate);
        assertFalse(accessUserListBeforeActivate.isEmpty());
        assertFalse(accessUserListBeforeDeactivate.isEmpty());
        assertTrue(accessUserListAfterDeactivate.isEmpty());
        assertTrue(accessUserListAfterActivate.isEmpty());
    }
}
