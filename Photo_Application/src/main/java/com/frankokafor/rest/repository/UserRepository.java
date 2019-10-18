package com.frankokafor.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.frankokafor.rest.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	@Query(value = "SELECT * FROM users WHERE email=?",nativeQuery = true)
	UserEntity findByEmail(String email);
	
	@Query(value = "SELECT * FROM users WHERE user_id=?",nativeQuery = true)
	UserEntity findByUserId(String userId);

}
