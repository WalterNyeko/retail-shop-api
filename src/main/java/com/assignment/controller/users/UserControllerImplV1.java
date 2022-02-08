package com.assignment.controller.users;

import com.assignment.dto.users.LoginDto;
import com.assignment.dto.users.UserDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;
import com.assignment.service.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserControllerImplV1 implements IUserController{


    private final IUserService userService;

    @Autowired
    public UserControllerImplV1(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto authenticateUser(LoginDto loginDto) {
        return userService.authenticateUser(loginDto);
    }

    @Override
    public RecordHolder<List<UserDto>> geUsers() {
        return userService.getUsers();
    }

    @Override
    public ActionResponse createUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
