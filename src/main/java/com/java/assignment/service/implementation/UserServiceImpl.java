package com.java.assignment.service.implementation;

import com.java.assignment.dto.UserUpdateRequestDTO;
import com.java.assignment.dto.UserDTO;
import com.java.assignment.entity.UserEntity;
import com.java.assignment.exception.DateRangeNotValidException;
import com.java.assignment.exception.InappropriateAgeException;
import com.java.assignment.exception.UserNotFoundException;
import com.java.assignment.repository.UserRepository;
import com.java.assignment.service.UserService;
import com.java.assignment.util.UserConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;

import static com.java.assignment.util.constant.ConstantsList.USER_NOT_FOUND_FOR_DELETION;
import static com.java.assignment.util.constant.ConstantsList.USER_NOT_FOUND_WITH_EMAIL;

@Service
public class UserServiceImpl implements UserService {
    @Value(value = "${age}")
    private Integer age;
    @Value(value = "${pageSize}")
    private Integer pageSize;
    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public UserServiceImpl(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws InappropriateAgeException {
        var age = calculateAge(userDTO.birthDate());
        if (isYoungerThanConfiguredAge(age)) {
            throw new InappropriateAgeException(age);
        }
        var userEntity = new UserEntity();
        userEntity.setAddress(userDTO.address());
        userEntity.setEmail(userDTO.email());
        userEntity.setLastName(userDTO.lastName());
        userEntity.setFirstName(userDTO.firstName());
        userEntity.setPhoneNumber(userDTO.phoneNumber());
        userEntity.setBirthDate(userDTO.birthDate());
        userRepository.save(userEntity);
        return userDTO;
    }

    @Override
    public UserDTO updateUserFields( String email, UserUpdateRequestDTO userUpdateRequest) throws UserNotFoundException {
        var existingUser = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + email));

        existingUser.setFirstName(userUpdateRequest.firstName() != null ? userUpdateRequest.firstName() :
                existingUser.getFirstName());

        existingUser.setLastName(userUpdateRequest.lastName() != null ? userUpdateRequest.lastName() :
                existingUser.getLastName());

        existingUser.setEmail(userUpdateRequest.email() != null ? userUpdateRequest.email() :
                existingUser.getEmail());

        existingUser.setPhoneNumber(userUpdateRequest.phoneNumber() != null ? userUpdateRequest.phoneNumber() :
                existingUser.getPhoneNumber());

        existingUser.setBirthDate(userUpdateRequest.birthDate() != null ? userUpdateRequest.birthDate() :
                existingUser.getBirthDate());

        existingUser.setAddress(userUpdateRequest.address() != null ? userUpdateRequest.address() :
                existingUser.getAddress());

        return userConverter.convertToUserDTO(userRepository.save(existingUser));
    }

    @Override
    public UserDTO updateAllUserFields(String email, UserDTO userDTO) throws UserNotFoundException {
        var existingUser = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_WITH_EMAIL + email));

        existingUser.setFirstName(userDTO.firstName());
        existingUser.setLastName(userDTO.lastName());
        existingUser.setBirthDate(userDTO.birthDate());
        existingUser.setAddress(userDTO.address());
        existingUser.setPhoneNumber(userDTO.phoneNumber());

        return userConverter.convertToUserDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(String email) throws UserNotFoundException {
        var user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_FOR_DELETION));
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> getUsersByDateRange(Date from, Date to, int pageNumber) throws DateRangeNotValidException {
        if (!from.before(to)) {
            throw new DateRangeNotValidException();
        }
        var pageable = PageRequest.of(pageNumber, pageSize);
        var users = userRepository.getUserEntitiesByBirthDateBetween(from, to, pageable);
        return users.map(userConverter::convertToUserDTO).getContent();
    }

    private boolean isYoungerThanConfiguredAge(Integer age) {
        return this.age > age;
    }

    private Integer calculateAge(Date birthDate) {
        var birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var currentDate = LocalDate.now();
        return Period.between(birthLocalDate, currentDate).getYears();
    }
}
