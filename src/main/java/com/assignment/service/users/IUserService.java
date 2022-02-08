package com.assignment.service.users;

import com.assignment.dto.users.LoginDto;
import com.assignment.dto.users.UserDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;

import java.util.List;

public interface IUserService {
    ActionResponse createUser(UserDto userDto);
    UserDto authenticateUser(LoginDto loginDto);
    RecordHolder<List<UserDto>> getUsers();
}
