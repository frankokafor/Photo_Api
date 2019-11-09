package com.frankokafor.rest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankokafor.rest.configuration.SpringApplicationContext;
import com.frankokafor.rest.model.request.UserLoginRequestModel;
import com.frankokafor.rest.service.UserService;
import com.frankokafor.rest.shared.object.UserDataTransferObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserLoginRequestModel loginUser = new ObjectMapper().readValue(request.getInputStream(),
					UserLoginRequestModel.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(),
					loginUser.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User) authResult.getPrincipal()).getUsername();

		String token = Jwts.builder().setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
		/*
		 * because our authentication filtter is not a bean, and we want to inject a
		 * bean class into it, we create an application context and get the method that
		 * gets a bean class directly.
		 * 
		 */
		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplimentation");
		UserDataTransferObject currentUser = userService.getUser(userName);

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		response.addHeader("User ID", currentUser.getUserId());
	}
}
