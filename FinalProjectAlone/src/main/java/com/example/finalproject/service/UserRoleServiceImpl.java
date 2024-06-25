package com.example.finalproject.service;


import com.example.finalproject.dto.request.role.AddRoleRequest;
import com.example.finalproject.dto.request.user.AddUserRequest;
import com.example.finalproject.entity.Role;
import com.example.finalproject.entity.User;
import com.example.finalproject.mapper.UserRoleMapper;
import com.example.finalproject.repository.RoleRepository;
import com.example.finalproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleMapper userRoleMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMapper = userRoleMapper;
    }


    @Override
    public void addRole(AddRoleRequest addRoleRequest) {

        Role role =userRoleMapper.fromAddRoleRequest(addRoleRequest);

        roleRepository.save(role);
    }

    //method below + its controller are redundant (operations done in register user)
    @Override
    public void addUser(AddUserRequest addUserRequest) {
        User user = userRoleMapper.fromAddUserRequest(addUserRequest);

        List<String> userRoler = addUserRequest.getRolesName();
        List<Role> roles = roleRepository.findAll().stream()
                .filter(element ->userRoler.contains(element.getName())).toList();
        user.setRoles(roles);

        userRepository.save(user);

    }


    // add methods for remove user, update user(change role)
}
