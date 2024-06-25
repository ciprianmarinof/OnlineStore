package com.example.finalproject.controller;

import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_role")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    //addRole - administration panel
    @PostMapping("/role")
    public ResponseEntity<Void> addRole(@RequestBody AddRoleRequest addRoleRequest)
    {
        userRoleService.addRole(addRoleRequest);

        return ResponseEntity.ok().build();

    }

    //redundant registerUser + registerAdmin
    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestBody AddUserRequest addUserRequest)
    {
        userRoleService.addUser((addUserRequest));
        return ResponseEntity.ok().build();
    }
}
