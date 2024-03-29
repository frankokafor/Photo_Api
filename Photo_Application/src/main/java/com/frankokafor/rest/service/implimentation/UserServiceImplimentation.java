package com.frankokafor.rest.service.implimentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.rest.exceptions.UserServiceException;
import com.frankokafor.rest.model.request.PasswordResetModel;
import com.frankokafor.rest.model.request.PasswordResetRequestModel;
import com.frankokafor.rest.model.response.ErrorMessages;
import com.frankokafor.rest.model.response.UserDetailsResponseModel;
import com.frankokafor.rest.models.PasswordReset;
import com.frankokafor.rest.models.Roles;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.PasswordResetRepository;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.security.AuthenticatedUserFacade;
import com.frankokafor.rest.service.UserService;
import com.frankokafor.rest.shared.object.AddressTransferObject;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
import com.frankokafor.rest.utils.FunctionUtils;
import com.frankokafor.rest.utils.RoleSets;

@Service
public class UserServiceImplimentation implements UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private FunctionUtils utils;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailServiceImpl service;
	@Autowired
	private PasswordResetRepository passRepo;
	@Autowired
	private RoleSets rol;
	@Autowired
	private AuthenticatedUserFacade userFac;
	@Value("${file.upload-dir}")
	String path;

	@Override
	public UserDataTransferObject createUser(UserDataTransferObject transferObject) {
		String publicUserId = utils.generatedKey(12);
		if (userRepo.findByEmail(transferObject.getEmail()) != null) {
			throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		for (int i = 0; i < transferObject.getAddresses().size(); i++) {
			AddressTransferObject address = transferObject.getAddresses().get(i);
			address.setUserDetails(transferObject);
			address.setAddressId(utils.generatedKey(5));
			transferObject.getAddresses().set(i, address);
			/*
			 * this will loop through the address list and generate all its public id and
			 * set its user to the registered user and then returned back to the original
			 * transfer object.
			 * 
			 */
		}
		UserEntity entity = new ModelMapper().map(transferObject, UserEntity.class);
		List<Roles> roleList = new ArrayList<>();
		Roles roles = new Roles(rol.ADMIN, rol.PR_ADMIN, entity);
		Roles roles2 = new Roles(rol.SUPER_ADMIN, rol.PR_SUPER_ADMIN, entity);
		Roles roles3 = new Roles(rol.DIRECTOR, rol.PR_DIRECTOR, entity);
//		roles.setUserDetails(entity);
		roleList.add(roles);
		roleList.add(roles2);
		roleList.add(roles3);
		entity.setEncryptedPassword(passwordEncoder.encode(transferObject.getPassword()));
		entity.setUserId(publicUserId);
		entity.setRoleList(roleList);
		entity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));// create a method to
																								// generate our
		// email verification token..
		UserEntity storedUser = userRepo.save(entity);
		service.sendText(storedUser);
		UserDataTransferObject returnValue = new ModelMapper().map(storedUser, UserDataTransferObject.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity entity = userRepo.findByEmail(username);
		if (entity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		// return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new
		// ArrayList<>());
		return new User(entity.getEmail(), entity.getEncryptedPassword(), entity.getEmailVerificationStatus(), true,
				true, true, new ArrayList<>());
		// this user constructor will help us check if the user is verified via email
		// befor he can sign in..
	}

	@Override
	public UserDataTransferObject getUser(String email) {
		UserEntity entity = userRepo.findByEmail(email);
		if (entity == null) {
			throw new UsernameNotFoundException("user " + email + " does not exixts");
		}
		UserDataTransferObject returnValue = new ModelMapper().map(entity, UserDataTransferObject.class);
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
	public Boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		UserEntity user = userRepo.findByEmailVerificationToken(token);
		if (user != null) {
			boolean isTokenExpired = utils.hasTokenExpired(token);
			if (!isTokenExpired) {
				user.setEmailVerificationToken(null);
				user.setEmailVerificationStatus(true);
				userRepo.save(user);
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override
	public Boolean requestPasswordResetToken(PasswordResetRequestModel requestModel) {
		Boolean value = false;
		UserEntity user = userRepo.findByEmail(requestModel.getEmail());
		if (user == null) {
			return value;
		}
		String token = utils.generatePasswordResetToken(user.getUserId());
//		CompletableFuture<Boolean> mailSent = CompletableFuture.supplyAsync(() -> {
//			return service.sendPasswordEmail(user.getFirstName(), user.getEmail(), token);
//		});
		PasswordReset password = new PasswordReset();
		password.setToken(token);
		password.setUserDetails(user);
		passRepo.save(password);
		value = service.sendPasswordEmail(user.getFirstName(), user.getEmail(), token);
		return value;
	}

	@Override
	public Boolean passwordReset(PasswordResetModel requestModel) {
		Boolean value = false;
		if (utils.hasTokenExpired(requestModel.getToken())) {
			return value;
		}
		PasswordReset newPassword = passRepo.findByToken(requestModel.getToken());
		if (newPassword == null) {
			return value;
		}
		String encodedPassword = passwordEncoder.encode(requestModel.getPassword());
		UserEntity user = newPassword.getUserDetails();
		user.setEncryptedPassword(encodedPassword);
		UserEntity returnUser = userRepo.save(user);
		if (returnUser != null && returnUser.getEncryptedPassword().equals(encodedPassword)) {
			value = true;
		}
		passRepo.delete(newPassword);
		return value;
	}

	@Override
	public void uploadProfilePicture(MultipartFile file) {
		final String fileDataName = FunctionUtils.PROFILE_IMAGE_PATH + FunctionUtils.getRandomName()
				+ userFac.getUser().getLastName() + ".png";
		Path convertFile = Paths.get(path + fileDataName);
		System.out.println("CONVERT: " + convertFile);
		try {
			Files.copy(file.getInputStream(), convertFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}

		UserEntity user = userRepo.findById(userFac.getUser().getId()).get();
		user.setPhotoUrl(fileDataName);
		userRepo.save(user);

	}
}
