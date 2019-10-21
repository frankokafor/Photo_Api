package com.frankokafor.rest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.frankokafor.rest.shared.object.UserDataTransferObject;

public interface UserService extends UserDetailsService {
	UserDataTransferObject createUser(UserDataTransferObject transferObject);
	UserDataTransferObject getUser(String email);
	UserDataTransferObject getUserByUserId(String userId);
	UserDataTransferObject updateUser(UserDataTransferObject transferObject,String userId);
	void deleteUser(String userId);
	List<UserDataTransferObject> getAllUsers(int page,int limit);
}