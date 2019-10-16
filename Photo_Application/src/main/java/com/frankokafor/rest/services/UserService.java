package com.frankokafor.rest.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.frankokafor.rest.shared.object.UserDataTransferObject;

public interface UserService extends UserDetailsService {
	UserDataTransferObject createUser(UserDataTransferObject transferObject);
	UserDataTransferObject getUser(String email);
}
