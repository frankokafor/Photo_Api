package com.frankokafor.rest.service.implimentation;

import java.util.ArrayList;
import java.util.List;

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
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.service.UserService;
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
		if (userRepo.findByEmail(transferObject.getEmail()) != null) {
			throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(transferObject, entity);

		String publicUserId = utils.generatedKey(12);
		entity.setEncryptedPassword(passwordEncoder.encode(transferObject.getPassword()));
		entity.setUserId(publicUserId);

		UserEntity storedUser = userRepo.save(entity);
		UserDataTransferObject returnValue = new UserDataTransferObject();
		BeanUtils.copyProperties(storedUser, returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity userEntity = userRepo.findByEmail(username);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
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
	public List<UserDataTransferObject> getAllUsers(int page, int limit) {
		List<UserDataTransferObject> returnUsers = new ArrayList<UserDataTransferObject>();
		if(page>0)page-=1;//we want our page to always start from 1 not zero.....
		Pageable request = PageRequest.of(page, limit);// this will help us add pagination to the spring jpa find all
														// method
		Page<UserEntity> usersPage = userRepo.findAll(request);// the find all method will now be returning the values
																// in pages
		if (usersPage.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		List<UserEntity> allUsers = usersPage.getContent();// this will convert the pages into a list.
		allUsers.forEach(entityUsers -> {
			UserDataTransferObject models = new UserDataTransferObject();
			BeanUtils.copyProperties(entityUsers, models);
			returnUsers.add(models);
		});
		return returnUsers;
	}

}
