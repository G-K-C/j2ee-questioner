package com.questioner.service.impl;

import com.questioner.entity.Role;
import com.questioner.repository.RoleRepository;
import com.questioner.service.abs.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;
    // Field to help get User and Admin role
    @Value("${role.userID}")
    private Long userID;

    @Value("${role.adminID}")
    private Long adminID;

    @Override
    public Role getUserRole() {
        return roleRepository.findOne(userID);
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.findOne(adminID);
    }
}
