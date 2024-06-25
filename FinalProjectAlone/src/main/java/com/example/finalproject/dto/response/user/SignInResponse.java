package com.example.finalproject.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

    private String username;
    private String email;
    private List<String> roles;
    private String token;
}
