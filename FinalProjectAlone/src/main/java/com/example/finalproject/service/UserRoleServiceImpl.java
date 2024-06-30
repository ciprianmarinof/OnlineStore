package com.example.finalproject.service;


import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.response.user.UserResponse;
import com.example.finalproject.entity.Role;
import com.example.finalproject.entity.User;
import com.example.finalproject.exception.user.UserNotFoundException;
import com.example.finalproject.mapper.UserRoleMapper;
import com.example.finalproject.repository.ProductRepository;
import com.example.finalproject.repository.RoleRepository;
import com.example.finalproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;
    private final ProductRepository productRepository;

    public UserRoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleMapper userRoleMapper, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMapper = userRoleMapper;
        this.productRepository = productRepository;
    }


    @Override
    public void addRole(AddRoleRequest addRoleRequest) {

        Role role =userRoleMapper.fromAddRoleRequest(addRoleRequest);

        roleRepository.save(role);
    }

    @Override
    public void deleteUser(Integer id) {

        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponse> userResponseList = users.stream().map(userRoleMapper::toUserResponse).collect(Collectors.toList());


        return userResponseList;
    }
}
