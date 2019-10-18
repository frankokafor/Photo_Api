package com.frankokafor.rest.service.implimentation;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.services.UserService;
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
		if(userRepo.findByEmail(transferObject.getEmail())!=null) {throw new RuntimeException("user already exixts");}
		
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
		if(userEntity==null) {throw new UsernameNotFoundException(username); }
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDataTransferObject getUser(String email) {
		UserEntity entity = userRepo.findByEmail(email);
		if(entity==null) {throw new UsernameNotFoundException("user " +email + " does not exixts");}
		UserDataTransferObject returnValue = new UserDataTransferObject();
		BeanUtils.copyProperties(entity, returnValue);
		return returnValue;
	}

	@Override
	public UserDataTransferObject getUserByUserId(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if(userEntity==null) {throw new UsernameNotFoundException("user with " +userId+ " does not exixts");}
		UserDataTransferObject value = new UserDataTransferObject();
		BeanUtils.copyProperties(userEntity, value);
		return value;
	}

}
