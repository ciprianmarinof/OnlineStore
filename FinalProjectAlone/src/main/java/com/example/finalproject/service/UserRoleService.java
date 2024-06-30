package com.example.finalproject.service;

import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.dto.response.user.UserResponse;
import com.example.finalproject.entity.User;

import java.util.List;

public interface UserRoleService {

    public void addRole (AddRoleRequest addRoleRequest);


    void deleteUser(Integer id);

    List<UserResponse> getAllUsers();
}
