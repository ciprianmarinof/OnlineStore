package com.example.finalproject.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}