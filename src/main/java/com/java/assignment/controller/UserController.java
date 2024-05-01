package com.java.assignment.controller;

import com.java.assignment.dto.UserDTO;
import com.java.assignment.dto.UserUpdateRequestDTO;
import com.java.assignment.exception.DateRangeNotValidException;
import com.java.assignment.exception.InappropriateAgeException;
import com.java.assignment.exception.UserNotFoundException;
import com.java.assignment.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.java.assignment.util.constant.ConstantsList.EMAIL_EXCEPTION_MESSAGE;
import static com.java.assignment.util.constant.ConstantsList.EMAIL_REGEX;

@RestController()
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = "application/vnd.assignment.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserDTO userDTO) throws InappropriateAgeException {
        return userService.createUser(userDTO);
    }

    @DeleteMapping(value = "/{email}", produces = "application/vnd.assignment.v1+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Email(regexp = EMAIL_REGEX, message = EMAIL_EXCEPTION_MESSAGE)
                           @PathVariable("email") String email) throws UserNotFoundException {
        userService.deleteUser(email);
    }

    @GetMapping(produces = "application/vnd.assignment.v1+json")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  Date toDate,
            @RequestParam Integer pageNumber) throws DateRangeNotValidException {
        return userService.getUsersByDateRange(fromDate, toDate, pageNumber);
    }

    @PatchMapping(value = "/{email}", produces = "application/vnd.assignment.v1+json")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable
                                    @Email(regexp = EMAIL_REGEX, message = EMAIL_EXCEPTION_MESSAGE)
                                    String email, @Valid @RequestBody UserUpdateRequestDTO userUpdateRequest) throws UserNotFoundException {
        return userService.updateUserFields(email, userUpdateRequest);
    }

    @PutMapping(value = "/{email}", produces = "application/vnd.assignment.v1+json")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateAll(
            @PathVariable @Email(regexp = EMAIL_REGEX, message = EMAIL_EXCEPTION_MESSAGE)
            @Valid String email, @RequestBody @Valid UserDTO userDTO) throws UserNotFoundException {
        return userService.updateAllUserFields(email, userDTO);
    }
}
