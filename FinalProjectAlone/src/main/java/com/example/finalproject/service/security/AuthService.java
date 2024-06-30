package com.example.finalproject.service.security;


import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.dto.request.user.SignInRequest;
import com.example.finalproject.dto.request.user.UpdateUserRequest;
import com.example.finalproject.dto.response.user.SignInResponse;
import com.example.finalproject.entity.Role;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.user.UserNotFoundException;
import com.example.finalproject.mapper.UserRoleMapper;
import com.example.finalproject.repository.RoleRepository;
import com.example.finalproject.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, UserRoleMapper userRoleMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder1;
    }


    public User getUserByEmail(String email)
    {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent())
        {
            throw new RuntimeException("Email is already in use");
        }
        return null;
    }

    public void registerUser(AddUserRequest addUserRequest) throws RoleNotFoundException {


        User user=userRoleMapper.fromAddUserRequest(addUserRequest);


        List<Role> roles = addUserRequest.getRolesName().stream()
                .map(roleName -> roleRepository.findRoleByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role with name " + roleName + " does not exist")))
                .collect(Collectors.toList());

        roles.forEach(user::addRole);
        userRepository.save(user);
    }

    public void updateUser(Integer userId, UpdateUserRequest updateUserRequest) throws RoleNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(updateUserRequest.getUsername() != null){
            user.setUsername(updateUserRequest.getUsername());
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        if(updateUserRequest.getEmail() != null){
            user.setEmail(updateUserRequest.getEmail());
        }

        if(updateUserRequest.getRolesName() != null  && !updateUserRequest.getRolesName().isEmpty()) {

            List<Role> roles = updateUserRequest.getRolesName().stream()
                    .map(roleName -> roleRepository.findRoleByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role with name " + roleName + " does not exist")))
                    .collect(Collectors.toList());

            user.getRoles().clear();
            roles.forEach(user::addRole);
        }

        userRepository.save(user);
    }

    public SignInResponse signIn(SignInRequest signInRequest)
    {
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        System.out.println("Generated JWT: " + jwt);

        return new SignInResponse(
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().stream().
                        map(auth -> auth.getAuthority())
                        .collect(Collectors.toList()), jwt
        );
    }

    public void signOut(HttpServletRequest request, HttpServletResponse response){

        request.getSession().invalidate();

    }
}