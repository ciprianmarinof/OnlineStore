package com.example.finalproject.service;

import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.AddUserRequest;

public interface UserRoleService {

    public void addRole (AddRoleRequest addRoleRequest);

    public void addUser (AddUserRequest addUserRequest);
}
