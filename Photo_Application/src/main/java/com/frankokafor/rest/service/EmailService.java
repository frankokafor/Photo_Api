package com.frankokafor.rest.service;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.shared.object.UserDataTransferObject;

@Service
public interface EmailService {
	void sendText(UserEntity user) throws MessagingException;

	Boolean sendPasswordEmail(String name, String email, String token) throws MessagingException;

}
