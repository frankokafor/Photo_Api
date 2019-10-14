package com.frankokafor.rest.services;

import com.frankokafor.rest.shared.object.UserDataTransferObject;

public interface UserService {
	UserDataTransferObject createUser(UserDataTransferObject transferObject);
}
