package com.frankokafor.rest.service.implimentation;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.model.response.AddressResponse;
import com.frankokafor.rest.models.AddressEntity;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.repository.AddressRepository;
import com.frankokafor.rest.repository.UserRepository;
import com.frankokafor.rest.service.AddressService;
import com.frankokafor.rest.shared.object.AddressTransferObject;

@Service
public class AddressServiceImpl implements AddressService{
	@Autowired
	private UserRepository repo;
	@Autowired
	private AddressRepository adrepo;

	@Override
	public List<AddressResponse> getUserAddresses(String userId) {
		List<AddressResponse> returnValue = new ArrayList<>();
		UserEntity user = repo.findByUserId(userId);
		List<AddressEntity> addresses = adrepo.findByUserDetails(user);
//		if(user==null||addresses.isEmpty()) {
//			return null;
//		}
		for(AddressEntity ad : addresses) {
			AddressResponse me = new ModelMapper().map(ad, AddressResponse.class);
			returnValue.add(me);
		}
		return returnValue;
	}

	@Override
	public AddressResponse findAddress(Long id) {
		AddressEntity me = adrepo.findById(id).get();
		AddressResponse u = new ModelMapper().map(me, AddressResponse.class);
		return u;
	}

}
