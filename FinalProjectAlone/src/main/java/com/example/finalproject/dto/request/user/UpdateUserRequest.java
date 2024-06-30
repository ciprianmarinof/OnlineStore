package com.example.finalproject.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateUserRequest {

    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Email
    @NotEmpty(message = "email should not be empty")
    private String email;

    private List<String> rolesName;
}
