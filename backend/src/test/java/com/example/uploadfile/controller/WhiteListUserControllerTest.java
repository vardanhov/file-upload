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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles({"test","dev"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class WhiteListUserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void adminTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/whitelist")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    //Fixme
    //TODO Проверить что пользователь имеет уникальный id
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void createWhiteListUserByUserNameTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/whitelist/add-by-username")
                .param("userName", "anton")
                .param("groupName", "user"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void limitAccessTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/whitelist/limit-access/{guid}", 2))
                .andExpect(status().isOk()).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void grantAccessByIdTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/whitelist/grant-access/{guid}", 6)
                .param("dateFrom", "2020-03-14")
                .param("timeFrom", "16:26:33")
                .param("dateTo", "2021-03-14")
                .param("timeTo", "16:26:33"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void delete() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/groups/delete/3"))
                .andExpect(status().isOk()).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void create() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/groups/create").param("groupName","sdfsfs"))

                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}
