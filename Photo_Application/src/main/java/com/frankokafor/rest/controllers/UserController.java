package com.frankokafor.rest.controllers;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.rest.exceptions.UserServiceException;
import com.frankokafor.rest.model.request.UserDetailsRequestModel;
import com.frankokafor.rest.model.response.AddressResponse;
import com.frankokafor.rest.model.response.ErrorMessages;
import com.frankokafor.rest.model.response.OperationStatusModel;
import com.frankokafor.rest.model.response.RequestOperationName;
import com.frankokafor.rest.model.response.RequestOperationStatus;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.service.AddressService;
import com.frankokafor.rest.service.UserService;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
import io.swagger.annotations.ApiOperation;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;//this will help us not hardcode our 
//address in the hataoeus link list.
@RestController
@RequestMapping(path = "/users")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService service;

	@ApiOperation(value = "creates a new user...")
	@PostMapping(path = "/create",produces = { MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity createUser(@RequestBody UserDetailsRequestModel requestModel) {
		if (requestModel.getEmail().isEmpty() || requestModel.getFirstName().isEmpty()
				|| requestModel.getLastName().isEmpty() || requestModel.getPassword().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
		}
		UserDataTransferObject transferObject = new ModelMapper().map(requestModel, UserDataTransferObject.class);
		UserDataTransferObject newUser = userService.createUser(transferObject);
		UserDetailsResponseModel returnModel = new ModelMapper().map(newUser, UserDetailsResponseModel.class);
		return new ResponseEntity<>(returnModel, HttpStatus.CREATED);
	}

	@ApiOperation(value = "get a single authenticated user...")
	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getUser(@PathVariable("userId") String userId) {
		UserDetailsResponseModel responseModel = new UserDetailsResponseModel();
		UserDataTransferObject transferObject = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(transferObject, responseModel);
		return new ResponseEntity<>(responseModel, HttpStatus.FOUND);
	}

	@ApiOperation(value = "update a single authenticated user...")
	@PutMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity updateUser(@RequestBody UserDetailsRequestModel requestModel,
			@PathVariable("userId") String userId) {
		UserDetailsResponseModel returnModel = new UserDetailsResponseModel();
		UserDataTransferObject transferObject = new UserDataTransferObject();
		BeanUtils.copyProperties(requestModel, transferObject);
		UserDataTransferObject updatedUser = userService.updateUser(transferObject, userId);
		BeanUtils.copyProperties(updatedUser, returnModel);
		return new ResponseEntity<>(returnModel, HttpStatus.ACCEPTED);
	}
	
	@ApiOperation(value = "delete a single authenticated user...")
	@DeleteMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
		OperationStatusModel returnModel = new OperationStatusModel();
		returnModel.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(userId);
		returnModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return new ResponseEntity<>(returnModel,HttpStatus.GONE);
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getAllUsers(@RequestParam(value = "page",defaultValue = "0") int page,
									@RequestParam(value = "limit",defaultValue = "25") int limit) {
		/*the request param is used for querying pagination especially when we want to get a list from our database
		 * here we want query our ddatabase to give us a list of users from our query param
		 * */
		List<UserDetailsResponseModel> users = userService.getAllUsers(page, limit);
//		List<UserDataTransferObject> tranfer = 
//		tranfer.forEach(transferObjects -> {
//			UserDetailsResponseModel models = new ModelMapper().map(transferObjects, UserDetailsResponseModel.class);
//			users.add(models);
//		});	
		return new ResponseEntity<>(users,HttpStatus.FOUND);
	}
	
	@ApiOperation(value = "creates a new user...")
	@GetMapping(path = "/{id}/addresses",
	produces = { MediaType.APPLICATION_JSON_VALUE, "application/hal+json"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getUserAddresses(@PathVariable("id") String userId) {
		List<AddressResponse> returnValue = service.getUserAddresses(userId);
		//if(address!=null&&!address.isEmpty()) {
//			Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
//			returnValue = new ModelMapper().map(address, listType);
//			for(AddressTransferObject ato : address) {
//				returnValue.add(new ModelMapper().map(ato, AddressResponse.class));
//			}
	//	}
		for(AddressResponse me : returnValue) {
			Link addlink = linkTo(methodOn(UserController.class).getAddress(12L)).withSelfRel();
			Link userlink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("User Addresses");
			me.add(userlink);
			me.add(addlink);
		}
		return new ResponseEntity<>(new Resources<>(returnValue), HttpStatus.FOUND);
	}
	
	@GetMapping(path = "/addresses/{id}",
	produces = { MediaType.APPLICATION_JSON_VALUE,"application/hal+json"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getAddress(@PathVariable("id") Long id) {
		Link addlink = linkTo(methodOn(UserController.class).getAddress(id)).withSelfRel();
		Link userlink = linkTo(methodOn(UserController.class).getUserAddresses("userId")).withRel("User Addresses");
		//adding hatoaeus links
		AddressResponse returnValue = service.findAddress(id);
		returnValue.add(addlink);
		returnValue.add(userlink);
		return new ResponseEntity<>(new Resource<>(returnValue), HttpStatus.FOUND);
		//this enables us to add hal+json properties to our rest end point..
	}
	
	@GetMapping(path = "/email-verification",
			produces = { MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
			public ResponseEntity verifyEmailToken(@RequestParam(value = "token") String token) {
				OperationStatusModel model = new OperationStatusModel();
				model.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
				Boolean isVerified = userService.verifiEmailToken(token);
				if(isVerified) {
					model.setOperationResult(RequestOperationStatus.SUCCESS.name());
				}else {
					model.setOperationResult(RequestOperationStatus.ERROR.name());
				}
				
				return new ResponseEntity<>(model,HttpStatus.FOUND);
			}
}
