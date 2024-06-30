package com.example.finalproject.controller;

import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.UpdateUserRequest;
import com.example.finalproject.dto.response.user.UserResponse;
import com.example.finalproject.service.UserRoleService;
import com.example.finalproject.service.security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/user_role")
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final AuthService authService;

    public UserRoleController(UserRoleService userRoleService, AuthService authService) {
        this.userRoleService = userRoleService;
        this.authService = authService;
    }

    //addRole - administration panel
    @PostMapping("/role")
    public ResponseEntity<Void> addRole(@RequestBody AddRoleRequest addRoleRequest)
    {
        userRoleService.addRole(addRoleRequest);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){

        userRoleService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest updateUserRequest) throws RoleNotFoundException {

        authService.updateUser(id, updateUserRequest);

        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> responseBody = userRoleService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
