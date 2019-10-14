package com.frankokafor.rest.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class FunctionUtils {
	//this will generate our public user id
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
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
}
