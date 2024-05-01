package com.java.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.assignment.controller.UserController;
import com.java.assignment.dto.UserDTO;
import com.java.assignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.java.assignment.util.constant.ConstantsList.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class AnnotationTest {
    @Autowired
    private MockMvc mockMvc;
    @Value(value = "${age}")
    private Integer age;
    @MockBean
    private UserService userService;

    @Test
    public void testAnnotation_EmailVerificationFailed() throws Exception {
        var birthDate = LocalDate.now().minusYears(age);
        var eighteenYears = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        var newUser = new UserDTO(EMAIL + "@", FIRST_NAME, LAST_NAME, eighteenYears, ADDRESS, PHONE_NUMBER);
        given(userService.createUser(any(UserDTO.class))).willReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(matchesRegex(".*Email should be valid.*")));
    }

    @Test
    public void testAnnotation_DateVerificationFailed() throws Exception {
        var newUser = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, new Date(), ADDRESS, PHONE_NUMBER);
        given(userService.createUser(any(UserDTO.class))).willReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(matchesRegex(".*Birth date must be in the past.*")));
    }

    @Test
    public void testEmptyEmailField_ReturnsBadRequest() throws Exception {
        var birthDate = LocalDate.now().minusYears(age);
        var eighteenYears = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var userWithEmptyEmail = new UserDTO(null, FIRST_NAME, LAST_NAME, eighteenYears, ADDRESS, PHONE_NUMBER);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userWithEmptyEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(matchesRegex(".*Email must not be null.*")));
    }
}
