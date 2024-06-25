package com.example.finalproject.mapper;

import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.dto.response.user.SignInResponse;
import com.example.finalproject.entity.Role;
import com.example.finalproject.entity.User;
import com.example.finalproject.service.security.UserDetailsImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoleMapper {

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Role fromAddRoleRequest(AddRoleRequest addRoleRequest)
    {
        Role role = new Role();
        role.setName(addRoleRequest.getRoleName());
        role.setUsers(new ArrayList<>());

        return role;
    }

    public User fromAddUserRequest(AddUserRequest addUserRequest)
    {
        User user= new User();
        user.setUsername(addUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        user.setEmail(addUserRequest.getEmail());
        user.setRoles(new ArrayList<>());

        return user;

    }
    public static AddUserRequest fromUserRequest(AddUserRequest userRequest)
    {
        AddUserRequest addUserRequest = new AddUserRequest();

        addUserRequest.setUsername(userRequest.getUsername());
        addUserRequest.setEmail(userRequest.getEmail());
        addUserRequest.setPassword(userRequest.getPassword());
        //addUserRequest.getRolesName().add(userRequest.getRole());
        addUserRequest.setRolesName(new ArrayList<>());

        return addUserRequest;
    }


    public static SignInResponse fromUserDetailsImpl(UserDetailsImpl userDetails)
    {
        SignInResponse response = new SignInResponse();

        response.setUsername(userDetails.getUsername());
        response.setEmail(userDetails.getEmail());
        List<String> roles = userDetails.getAuthorities().stream().map(a->a.getAuthority()).toList();
        response.setRoles(roles);

        return response;
    }

}

