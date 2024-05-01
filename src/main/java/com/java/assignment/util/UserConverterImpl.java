package com.java.assignment.util;

import com.java.assignment.dto.UserDTO;
import com.java.assignment.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDTO convertToUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getBirthDate(),
                userEntity.getAddress(),
                userEntity.getPhoneNumber());
    }
}
