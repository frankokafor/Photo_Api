package com.frankokafor.rest.service;

import org.springframework.stereotype.Service;

import com.frankokafor.rest.shared.object.UserDataTransferObject;

@Service
public interface EmailService {
	void sendText(UserDataTransferObject user);

}
