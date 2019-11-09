package com.frankokafor.rest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.model.request.PasswordResetModel;
import com.frankokafor.rest.model.request.PasswordResetRequestModel;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
@Service
public interface UserService extends UserDetailsService {
	UserDataTransferObject createUser(UserDataTransferObject transferObject);
	UserDataTransferObject getUser(String email);
	UserDataTransferObject getUserByUserId(String userId);
	UserDataTransferObject updateUser(UserDataTransferObject transferObject,String userId);
	void deleteUser(String userId);
	List<UserDetailsResponseModel> getAllUsers(int page,int limit);
	Boolean verifyEmailToken(String token);
	Boolean requestPasswordResetToken(PasswordResetRequestModel requestModel);
	Boolean passwordReset(PasswordResetModel requestModel);
}
