package com.frankokafor.rest.security;

import com.frankokafor.rest.configuration.SpringApplicationContext;
import com.frankokafor.rest.utils.AppProperties;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000;// 10 DAYS
	public static final String TOKEN_PREFIX = "Bearer: ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/create";
	public static final String EMAIL_VERIFICATION_URL = "/users/email-verification";
	public static final String PASSWORD_RESET_TOKEN_URL = "/users/password-reset-token";
	public static final String PASSWORD_RESET_URL = "/users/reset-password";
	public static final long PASSWORD_TOKEN_EXPIRATION_TIME = 10800000;// 3 hours
	public static final String H2_CONSOLE = "/h2-console/**";

	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
