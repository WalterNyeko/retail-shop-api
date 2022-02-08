package com.assignment.controller.users;

import com.assignment.dto.users.LoginDto;
import com.assignment.dto.users.UserDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IUserController {
    @PostMapping(value = "/signin")
    @ResponseStatus(HttpStatus.OK)
    UserDto authenticateUser(@RequestBody LoginDto loginDto);

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    RecordHolder<List<UserDto>> geUsers();

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    ActionResponse createUser(@RequestBody UserDto userDto);

}
