package com.frankokafor.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frankokafor.rest.model.response.AddressResponse;
import com.frankokafor.rest.shared.object.AddressTransferObject;

@Service
public interface AddressService {

	List<AddressResponse> getUserAddresses(String userId);
	AddressResponse findAddress(Long id);
}
