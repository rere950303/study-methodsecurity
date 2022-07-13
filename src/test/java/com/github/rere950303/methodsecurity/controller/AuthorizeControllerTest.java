package com.github.rere950303.methodsecurity.controller;

import com.github.rere950303.methodsecurity.advice.ControllerAdvice;
import com.github.rere950303.methodsecurity.aspect.AccessCheckAspect;
import com.github.rere950303.methodsecurity.entity.Entity1;
import com.github.rere950303.methodsecurity.entity.Entity2;
import com.github.rere950303.methodsecurity.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.github.rere950303.methodsecurity.utils.ApiTestUtils.getJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(AccessCheckAspect.class)
class AuthorizeControllerTest {

    MockMvc mockMvc;

    @Autowired
    AuthorizeController authorizeController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(authorizeController)
                .setControllerAdvice(new ControllerAdvice())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void preAuthorize1_isOk() throws Exception {
        String json = getJson(Entity1.builder().id(1L).build());

        mockMvc.perform(get("/preauthorize1/{memberId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void preAuthorize1_throwAccessDeniedException() throws Exception {
        String json = getJson(Entity1.builder().id(1L).build());

        mockMvc.perform(get("/preauthorize1/{memberId}", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AccessDeniedException.class))
                .andExpect(status().isForbidden());
    }

    @Test
    public void preAuthorize2_isOK() throws Exception {
        String json = getJson(Entity2.builder().id(1L).build());

        mockMvc.perform(get("/preauthorize2/{memberId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void preAuthorize2_throwAccessDeniedException() throws Exception {
        String json = getJson(Entity2.builder().id(1L).build());

        mockMvc.perform(get("/preauthorize2/{memberId}", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AccessDeniedException.class))
                .andExpect(status().isForbidden());
    }
}