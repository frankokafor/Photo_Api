package com.frankokafor.rest.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frankokafor.rest.model.request.UserDetailsRequestModel;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.service.implimentation.UserServiceImplimentation;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserServiceImplimentation userService;
	
	@ApiOperation(value = "creates a new user...")
	@PostMapping
	public ResponseEntity<UserDetailsResponseModel> createUser(@RequestBody UserDetailsRequestModel requestModel) {
		UserDetailsResponseModel returnModel = new UserDetailsResponseModel();
		UserDataTransferObject transferObject = new UserDataTransferObject();
		BeanUtils.copyProperties(requestModel, transferObject);
		
		UserDataTransferObject newUser = userService.createUser(transferObject);
		BeanUtils.copyProperties(newUser, returnModel);
		return new ResponseEntity<>(returnModel,HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "get a single user...")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDetailsResponseModel> getUser(@PathVariable("userId") String userId){
		UserDetailsResponseModel responseModel = new UserDetailsResponseModel();
		UserDataTransferObject transferObject = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(transferObject, responseModel);
		return new ResponseEntity<>(responseModel,HttpStatus.FOUND);
	}
}
