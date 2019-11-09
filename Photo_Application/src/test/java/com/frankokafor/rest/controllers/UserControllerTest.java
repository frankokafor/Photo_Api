package com.frankokafor.rest.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.frankokafor.rest.service.AddressService;
import com.frankokafor.rest.service.UserService;

class UserControllerTest {
	@InjectMocks
	UserController controller;
	@Mock
	UserService service;
	@Mock
	AddressService addService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUser() {
		fail("Not yet implemented");
	}

}
