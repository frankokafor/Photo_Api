package com.frankokafor.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.rest.models.AddressEntity;
import com.frankokafor.rest.models.UserEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

	List<AddressEntity> findByUserDetails(UserEntity user);
}
