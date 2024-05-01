package com.java.assignment.service;

import com.java.assignment.dto.UserUpdateRequestDTO;
import com.java.assignment.dto.UserDTO;
import com.java.assignment.exception.DateRangeNotValidException;
import com.java.assignment.exception.InappropriateAgeException;
import com.java.assignment.exception.UserNotFoundException;

import java.util.Date;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO) throws InappropriateAgeException;

    UserDTO updateUserFields(String email, UserUpdateRequestDTO userUpdateRequest) throws UserNotFoundException;

    UserDTO updateAllUserFields(String email, UserDTO userDTO) throws UserNotFoundException;

    void deleteUser(String email) throws UserNotFoundException;

    List<UserDTO> getUsersByDateRange(Date from, Date to, int pageNumber) throws DateRangeNotValidException;
}
