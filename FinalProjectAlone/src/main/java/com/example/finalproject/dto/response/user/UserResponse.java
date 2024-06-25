package com.example.finalproject.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;
    private String email;
    private List<String> products;
    private List<String> roles;

}
