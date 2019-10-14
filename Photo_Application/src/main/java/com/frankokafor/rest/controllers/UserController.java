package com.frankokafor.rest.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.rest.model.request.UserDetailsRequestModel;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.service.implimentation.UserServiceImplimentation;
import com.frankokafor.rest.shared.object.UserDataTransferObject;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private UserServiceImplimentation userService;
	
	@PostMapping
	public ResponseEntity<UserDetailsResponseModel> createUser(UserDetailsRequestModel requestModel) {
		UserDetailsResponseModel returnModel = new UserDetailsResponseModel();
		UserDataTransferObject transferObject = new UserDataTransferObject();
		BeanUtils.copyProperties(requestModel, transferObject);
		
		UserDataTransferObject newUser = userService.createUser(transferObject);
		BeanUtils.copyProperties(newUser, returnModel);
		return new ResponseEntity<UserDetailsResponseModel>(returnModel,HttpStatus.CREATED);
	}
}
