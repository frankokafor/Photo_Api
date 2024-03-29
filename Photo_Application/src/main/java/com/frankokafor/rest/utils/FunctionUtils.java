package com.frankokafor.rest.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.frankokafor.rest.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class FunctionUtils {
	// this will generate our public user id
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public static String PROFILE_IMAGE_PATH = "profile_image/";

	public String generateUserId() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomName() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return number + "";
	}

	public String generatedKey(int key) {
		return generatedRandomString(key);
	}

	private String generatedRandomString(int key) {
		StringBuilder main = new StringBuilder(key);

		for (int i = 0; i < key; i++) {
			main.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(main);
	}

	public boolean hasTokenExpired(String token) {
		Boolean value = true;
		try {
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody();
			Date tokenExpirationDate = claims.getExpiration();
			Date todayDate = new Date();
			value = tokenExpirationDate.before(todayDate);
			return value;
		} catch (Exception e) {
			return value;
		}
	}

	public String generateEmailVerificationToken(String userId) {
		String token = Jwts.builder().setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
		return token;
	}

	public String generatePasswordResetToken(String userId) {
		String token = Jwts.builder().setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
		return token;
	}

	public String generateAuthenticationToken(String username) {
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
		return token;
	}
}
