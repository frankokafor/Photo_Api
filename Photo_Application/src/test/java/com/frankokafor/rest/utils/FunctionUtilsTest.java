package com.frankokafor.rest.utils;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest // this is used for integration test to load up spring context e.g tokensecret
class FunctionUtilsTest {
	@Autowired
	FunctionUtils utils;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	final String USER_ID = "1234werty";

	@Test
	void testGeneratedKey() {
		String user = utils.generatedKey(23);
		String user2 = utils.generatedKey(23);
		assertNotNull(user);
		assertTrue(user.length() == 23);
		assertTrue(!user.equals(user2));
	}

	@Test
	void testHasTokenExpired() {
		String token = utils.generateEmailVerificationToken(USER_ID);
		boolean me = utils.hasTokenExpired(token);
		assertFalse(me);
	}

	@Test
	void testHasTokenNotExpired() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvV2tnUDZFRHdLRVYiLCJleHAiOjE1NzMzMDI1NjF9.33CQ9Y2e2xVKfDgLjIz1QNSZesL_zRboIAnE_AshIisk8vhXR_0AkV5jkOdmuw2zkvqRR5PyThCSs8AKIz5J6A";
		boolean me = utils.hasTokenExpired(token);
		assertFalse(me);
	}

	@Test
	@Disabled // used to ignore a test method
	void testGeneratePasswordResetToken() {
		fail("Not yet implemented");
	}

}
