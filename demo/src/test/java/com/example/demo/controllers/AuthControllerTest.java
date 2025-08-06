
package com.example.demo.controllers;

import com.example.demo.services.UserService;
import com.example.demo.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void login_ShouldReturnValidationError_WhenUsernameIsEmpty() throws Exception {
        String requestBody = """
                    {
                        "username": "",
                        "password": "123456789"
                    }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("username: Username cannot be empty"))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void login_ShouldReturnValidationError_WhenPasswordIsEmpty() throws Exception {
        String requestBody = """
                    {
                        "username": "tungnt@softech.vn",
                        "password": ""
                    }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("password: Password cannot be empty"))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}