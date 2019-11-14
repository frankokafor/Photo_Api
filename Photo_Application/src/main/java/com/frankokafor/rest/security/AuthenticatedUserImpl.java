/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frankokafor.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;

/**
 *
 * @author mac
 */
@Service
public class AuthenticatedUserImpl implements AuthenticatedUserFacade {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getName() {
        return this.getAuthentication().getName();
    }

    @Override
    public UserEntity getUser() {
        return this.getName() != null ? userRepo.findByEmail(this.getName()) : null;
    }
}
