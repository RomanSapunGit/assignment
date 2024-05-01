package com.java.assignment.util;

import com.java.assignment.dto.UserDTO;
import com.java.assignment.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public interface UserConverter {

    UserDTO convertToUserDTO(UserEntity userEntity);
}
