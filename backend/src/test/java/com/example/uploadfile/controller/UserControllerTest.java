package com.example.uploadfile.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@ActiveProfiles({"test","dev"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void findUserByUsernameTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/users/find-by-username")
                .param("username", "oleg")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void getUserHistoryTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/history")
                .param("page", "5")
                .param("size", "10"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void getAllUsersHistoryTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/all-histories")
                .param("page", "3")
                .param("size", "4"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    //TODO Fix test
    //@Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void createGroupTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/users/create-group")
                .param("group", "MyGroup"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void addUserTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/users/add-into-group")
                .param("users", "anton")
                .param("users", "oleg")
                .param("users", "sergey")
                .param("groupName", "user"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        assertNotNull(mvcResult);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void deleteUserTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/users/delete-from-group")
                .param("username", "anton"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        assertNotNull(mvcResult);

    }

}
