package com.java.assignment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.assignment.controller.UserController;
import com.java.assignment.dto.UserUpdateRequestDTO;
import com.java.assignment.dto.UserDTO;
import com.java.assignment.exception.DateRangeNotValidException;
import com.java.assignment.exception.InappropriateAgeException;
import com.java.assignment.exception.UserNotFoundException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import static com.java.assignment.util.constant.ConstantsList.*;
import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Value(value = "${age}")
    private Integer age;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser_Created() throws Exception {
        var birthDate = LocalDate.now().minusYears(age);
        var eighteenYears = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        var newUser = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, eighteenYears,
                null, PHONE_NUMBER);

        given(userService.createUser(any(UserDTO.class))).willReturn(newUser);

        mockMvc.perform(post(USERS_URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testCreateUser_BadRequest() throws Exception {
        var birthDate = Date.from(LocalDate.now().minusYears(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        var newUser = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, birthDate, ADDRESS, PHONE_NUMBER);
        given(userService.createUser(any(UserDTO.class))).willThrow(new InappropriateAgeException(age));

        mockMvc.perform(post(USERS_URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(matchesRegex("Your age must be more than configured!.*")));
    }

    @Test
    public void testDeleteUser_NoContent() throws Exception {
        var email = EMAIL;

        mockMvc.perform(delete("/users/{email}", email))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(email);
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {

        doThrow(UserNotFoundException.class).when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/users/{email}", EMAIL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersByRange_OK() throws Exception {
        var now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        var expectedUsers = Arrays.asList(
                new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, now, ADDRESS, PHONE_NUMBER),
                new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, now, ADDRESS, PHONE_NUMBER));
        given(userService.getUsersByDateRange(any(Date.class),any(Date.class), any(Integer.class))).willReturn(expectedUsers);

        var result = mockMvc.perform(get(USERS_URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", "2000-01-01")
                        .param("toDate", "2000-01-01")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = result.getResponse().getContentAsString();
        List<UserDTO> actualUsers = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});

        Assertions.assertEquals(expectedUsers.size(), actualUsers.size());
        Assertions.assertTrue(expectedUsers.containsAll(actualUsers));
    }

    @Test
    public void testGetUsersByRange_FromDateAfterToDate_BadRequest() throws Exception {

        given(userService.getUsersByDateRange(any(Date.class),any(Date.class), any(Integer.class))).willThrow(new DateRangeNotValidException());


        mockMvc.perform(get(USERS_URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", "2000-01-01")
                        .param("toDate", "2000-01-01")
                        .param("pageNumber", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserFields_OK() throws Exception {
        var userUpdateRequest = new UserUpdateRequestDTO(null, null, null,
                null, null, null);

        var updatedUserDTO = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, new Date(), ADDRESS, PHONE_NUMBER);
        given(userService.updateUserFields(any(String.class), any(UserUpdateRequestDTO.class))).willReturn(updatedUserDTO);

        mockMvc.perform(patch("/users/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateNonExistentUser_Notfound() throws Exception {
        var userUpdateRequest = new UserUpdateRequestDTO(null, null, null,
                null, null, null);

        given(userService.updateUserFields(any(String.class), any(UserUpdateRequestDTO.class)))
                .willThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(patch("/users/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPathRequestWithWrongEmail_BadRequest() throws Exception {
        var userUpdateRequest = new UserUpdateRequestDTO(null, null, null,
                null, null, null);

        var updatedUserDTO = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, new Date(), ADDRESS, PHONE_NUMBER);
        given(userService.updateUserFields(any(String.class), any(UserUpdateRequestDTO.class))).willReturn(updatedUserDTO);

        mockMvc.perform(patch("/users/test@examplecom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("update.email: Email should be valid"));
    }


    @Test
    public void testUpdateAllUserFields_OK() throws Exception {
        var birthDate = LocalDate.now().minusYears(age);
        var eighteenYears = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var updatedUserDTO = new UserDTO(EMAIL, FIRST_NAME, LAST_NAME, eighteenYears, ADDRESS, PHONE_NUMBER);

        given(userService.updateAllUserFields(any(String.class), any(UserDTO.class))).willReturn(updatedUserDTO);

        mockMvc.perform(put("/users/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk());
    }
}
