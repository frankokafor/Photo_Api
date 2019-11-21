package com.frankokafor.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.models.UserEntity;

@Service
public interface AuthenticatedUserFacade {

	Authentication getAuthentication();

	String getName();

	UserEntity getUser();
}
