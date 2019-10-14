package com.frankokafor.rest.service.implimentation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

}
