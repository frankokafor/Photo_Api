package com.frankokafor.rest.service.implimentation;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.exceptions.UserServiceException;
import com.frankokafor.rest.model.response.ErrorMessages;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.service.UserService;
import com.frankokafor.rest.shared.object.AddressTransferObject;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
import com.frankokafor.rest.utils.FunctionUtils;

@Service
public class UserServiceImplimentation implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FunctionUtils utils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDataTransferObject createUser(UserDataTransferObject transferObject) {
		String publicUserId = utils.generatedKey(12);
		if (userRepo.findByEmail(transferObject.getEmail()) != null) {
			throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		for (int i = 0; i < transferObject.getAddresses().size(); i++) {
			AddressTransferObject address = transferObject.getAddresses().get(i);
			address.setUserDetails(transferObject);
			address.setAddressId(publicUserId);
			transferObject.getAddresses().set(i, address);
			/*this will loop through the address list and generate all its public id and set its user to the registered
			 * user and then returned back to the original transfer object.
			 * 
			 */
		}
		UserEntity entity = new ModelMapper().map(transferObject, UserEntity.class);
		entity.setEncryptedPassword(passwordEncoder.encode(transferObject.getPassword()));
		entity.setUserId(publicUserId);
		entity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));//create a method to generate our 
		//email verification token..
		//entity.setEmailVerificationStatus(false);
		UserEntity storedUser = userRepo.save(entity);
		UserDataTransferObject returnValue = new ModelMapper().map(storedUser, UserDataTransferObject.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity entity = userRepo.findByEmail(username);
		if (entity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
		return new User(entity.getEmail(), entity.getEncryptedPassword(), entity.getEmailVerificationStatus(), 
				true, true, true, new ArrayList<>());
		//this user constructor will help us check if the user is verified via email befor he can sign in..
	}

	@Override
	public UserDataTransferObject getUser(String email) {
		UserEntity entity = userRepo.findByEmail(email);
		if (entity == null) {
			throw new UsernameNotFoundException("user " + email + " does not exixts");
		}
		UserDataTransferObject returnValue = new UserDataTransferObject();
		BeanUtils.copyProperties(entity, returnValue);
		return returnValue;
	}

	@Override
	public UserDataTransferObject getUserByUserId(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		UserDataTransferObject value = new UserDataTransferObject();
		BeanUtils.copyProperties(userEntity, value);
		return value;
	}

	@Override
	public UserDataTransferObject updateUser(UserDataTransferObject transferObject, String userId) {
		UserDataTransferObject value = new UserDataTransferObject();
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		userEntity.setFirstName(transferObject.getFirstName());
		userEntity.setLastName(transferObject.getLastName());
		UserEntity storedUser = userRepo.save(userEntity);
		BeanUtils.copyProperties(storedUser, value);
		return value;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		userRepo.delete(userEntity);
	}

	@Override
	public List<UserDetailsResponseModel> getAllUsers(int page, int limit) {
		List<UserDetailsResponseModel> returnUsers = new ArrayList<>();
		if (page > 0)
			page -= 1;// we want our page to always start from 1 not zero.....
		Pageable request = PageRequest.of(page, limit);// this will help us add pagination to the spring jpa find all
														// method
		Page<UserEntity> usersPage = userRepo.findAll(request);// the find all method will now be returning the values
																// in pages
		if (usersPage.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		List<UserEntity> allUsers = usersPage.getContent();// this will convert the pages into a list.
		allUsers.forEach(entityUsers -> {
			UserDetailsResponseModel models = new ModelMapper().map(entityUsers, UserDetailsResponseModel.class);
			returnUsers.add(models);
		});
		return returnUsers;
	}

	@Override
	public Boolean verifiEmailToken(String token) {
		boolean returnValue = false;
		UserEntity user = userRepo.findByEmailVerificationToken(token);
		if(user!=null) {
			boolean isTokenExpired = utils.hasTokenExpired(token);
			if(!isTokenExpired) {
				user.setEmailVerificationToken(null);
				user.setEmailVerificationStatus(Boolean.TRUE);
				userRepo.save(user);
				returnValue = true;
			}
		}
		return returnValue;
	}

}
