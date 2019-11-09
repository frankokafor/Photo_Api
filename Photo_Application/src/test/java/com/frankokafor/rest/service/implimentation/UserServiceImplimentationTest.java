package com.frankokafor.rest.service.implimentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.frankokafor.rest.exceptions.UserServiceException;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.PasswordResetRepository;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.shared.object.AddressTransferObject;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
import com.frankokafor.rest.utils.FunctionUtils;

/*this is a test class using mockito to mock our selected method and test its accuracy and efficiency
 * whhen performing a test, it doesnt matter what our return is, we are just testing if our method works
 * and so we can create dummy data just for the test purpose
 * This tests helps us determine that we can rely on our code that everything it is expected to do will work accordingly
 * and if for any reason any of the tests fails, maven will not be able to build it into a war file..
 * 
 */
class UserServiceImplimentationTest {
	@InjectMocks // this will help mockito to inject all the fake objects this class will need as
					// many classes where autowired
	UserServiceImplimentation service;// to the main original class
	@Mock // this is mocked because it is needed for our test class as an externally
			// injected object..
	UserRepository userRepo;
	@Mock
	FunctionUtils utils;
	@Mock
	BCryptPasswordEncoder passwordEncoder;
	@Mock
	EmailServiceImpl eService;
	@Mock
	PasswordResetRepository passRepo;
	UserEntity user;
	UserDataTransferObject dto;
	AddressTransferObject address;
	List<AddressTransferObject> addresses;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		// this will help mockito to be able to instantiate the object of our preferred
		// test class..
		user = new UserEntity();
		user.setId(3L);
		user.setFirstName("emeka");
		user.setLastName("okoro");
		user.setUserId("ddtsfersf");
		user.setEncryptedPassword("i am encrypted");
		dto = new UserDataTransferObject();
		dto.setEmail("frank@love.com");
	}

	@Test
	void testGetUser() {
		

		when(userRepo.findByEmail(anyString())).thenReturn(user);
		/*
		 * so this means dat wen userrepo.findbyemail is called it should accept any
		 * string as argument and still return our expected value
		 * 
		 */
		dto = service.getUser("test@yahoo.com");
		assertNotNull(dto);// this is to check if anything is returned if it is null then our test will
							// fail
		assertEquals("emeka", dto.getFirstName());
	}

	@Test
	void testGetUser_UserNotFoundException() {
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> {
			service.getUser("test@yahoo.com");
		});
		/*
		 * so we assert that anytime this method "service.getUser("test@yahoo.com")"
		 * returns null, this exception is expected to be thrown..
		 */
	}
	
	@Test
	void testGetUser_UserServiceException() {
		when(userRepo.findByEmail(anyString())).thenReturn(user);
		assertThrows(UserServiceException.class, () -> {
			service.createUser(dto);
		});
	}
	
	@Test
	void testCreateUser() {
		address = new AddressTransferObject();
		address.setType("billing");
		address.setCountry("nigeria");
		address.setStreetName("central");
		AddressTransferObject address2 = new AddressTransferObject();
		address2.setType("shipping");
		address2.setCountry("nigeria");
		address2.setStreetName("central");
		addresses = new ArrayList<>();
		addresses.add(address);
		addresses.add(address2);
		dto.setAddresses(addresses);
		dto.setPassword("oshorire");
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		when(utils.generatedKey(anyInt())).thenReturn("22ebukas");
		when(passwordEncoder.encode(anyString())).thenReturn("frakasme");
		when(userRepo.save(any(UserEntity.class))).thenReturn(user);
		Mockito.doNothing().when(eService).sendText(any(UserEntity.class));
		UserDataTransferObject stored = service.createUser(dto);
		assertNotNull(stored);
		assertEquals(user.getFirstName(), stored.getFirstName());
		assertEquals(user.getEmail(), stored.getEmail());
		assertNotNull(stored.getUserId());
		verify(utils,times(2)).generatedKey(5);//determines how many times the method was called
		verify(passwordEncoder,times(1)).encode("oshorire");
		verify(userRepo,times(1)).save(any(UserEntity.class));
		
	}
	

}
