package com.frankokafor.rest.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankokafor.rest.configuration.SpringApplicationContext;
import com.frankokafor.rest.model.request.UserLoginRequestModel;
import com.frankokafor.rest.models.Roles;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.utils.FunctionUtils;
import com.google.gson.Gson;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final FunctionUtils utils = new FunctionUtils();
	private final Gson gson = new Gson();
	private final Map m = new HashMap();

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
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.ALL_VALUE);
			try {
				m.put("code", "error");
				m.put("message", "usuccessful, invalid username or password \n");
				String json = gson.toJson(m);
				PrintWriter pr = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pr.write(json);
			} catch (IOException ex1) {
				return null;
			}
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User) authResult.getPrincipal()).getUsername();
		String token = utils.generateAuthenticationToken(userName);

		/*
		 * because our authentication filtter is not a bean, and we want to inject a
		 * bean class into it, we create an application context and get the method that
		 * gets a bean class directly.
		 * 
		 */
//		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplimentation");
		UserRepository userRepo = (UserRepository) SpringApplicationContext.getBean("userRepository");
		/* UserDataTransferObject */UserEntity currentUser = userRepo.findByEmail(userName);// userService.getUser(userName);

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		response.addHeader("User ID", currentUser.getUserId());
		m.put("code", "success");
		m.put("message", "Login successful");
		m.put("token", SecurityConstants.TOKEN_PREFIX + token);
		List<String> roles = new ArrayList<>();// Arrays.asList("me","you");
		List<Roles> rs = currentUser.getRoleList();
		for (int i = 0; i < rs.size(); i++) {
			roles.add(rs.get(i).getName());
		}
		m.put("roles", roles);
		String str = gson.toJson(m);
		PrintWriter pr = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		pr.write(str);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.ALL_VALUE);
		m.put("code", "error");
		m.put("message", "usuccessful, invalid username or password");
		String json = gson.toJson(m);
		PrintWriter pr = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		pr.write(json);
	}
}
