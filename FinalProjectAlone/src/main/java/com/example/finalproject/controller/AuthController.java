package com.example.finalproject.controller;


import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.dto.request.user.SignInRequest;
import com.example.finalproject.dto.response.user.SignInResponse;
import com.example.finalproject.service.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

@RestController
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }


    // do something about this. register admin should be different from register user
    // for frontend - different pages for register - administration panel sign in??
    @PostMapping("/api/register")
    public ResponseEntity<Void> registerAdmin(@RequestBody @Valid AddUserRequest userRequest) throws RoleNotFoundException {
        authService.getUserByEmail(userRequest.getEmail());
        authService.registerUser(userRequest);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/api/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest)
    {
        SignInResponse response = authService.signIn(signInRequest);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/signout")
    public ResponseEntity<Void> signOut(HttpServletRequest request, HttpServletResponse response){

        request.getSession().invalidate();

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}