package com.frankokafor.rest.service.implimentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.shared.object.UserDataTransferObject;
/*this is a test class using mockito to mock our selected method and test its accuracy and efficiency
 * whhen performing a test, it doesnt matter what our return is, we are just testing if our method works
 * and so we can create dummy data just for the test purpose
 * This tests helps us determine that we can rely on our code that everything it is expected to do will work accordingly
 * and if for any reason any of the tests fails, maven will not be able to build it into a war file..
 * 
 */
class UserServiceImplimentationTest {
	@InjectMocks //this will help mockito to inject all the fake objects this class will need as many classes where autowired
	UserServiceImplimentation service;//to the main original class
	
	@Mock//this is mocked because it is needed for our test class as an externally injected object..
	UserRepository userRepo;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		//this will help mockito to be able to instantiate the object of our preferred test class..
	}

	@Test
	void testGetUser() {
		UserEntity user = new UserEntity();
		user.setId(3L);
		user.setFirstName("emeka");
		user.setLastName("okoro");
		user.setUserId("ddtsfersf");
		user.setEncryptedPassword("i am encrypted");
		
		when(userRepo.findByEmail(anyString())).thenReturn(user);
		/*so this means dat wen userrepo.findbyemail is called it should accept any string as argument and still return our
		 * expected value
		 * 
		 */
		UserDataTransferObject dto = service.getUser("test@yahoo.com");
		assertNotNull(dto);//this is to check if anything is returned if it is null then our test will fail
		assertEquals("emeka", dto.getFirstName());
	}
	
	@Test
	void testGetUser_UserNotFoundException() {
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, ()-> {service.getUser("test@yahoo.com");});
		/*so we assert that anytime this method "service.getUser("test@yahoo.com")" returns null, this exception is
		 * expected to be thrown..
		 */
	}

}
