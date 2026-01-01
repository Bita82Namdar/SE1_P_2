package com.university.library.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testRegisterStudent_Success() throws Exception {
        // تست ثبت‌نام موفق
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"studentId\":\"12345\",\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk());
    }
}
