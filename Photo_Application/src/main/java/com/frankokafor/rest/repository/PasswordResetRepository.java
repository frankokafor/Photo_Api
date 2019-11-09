package com.frankokafor.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.rest.models.PasswordReset;
import com.frankokafor.rest.models.UserEntity;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
	PasswordReset findByToken(String token);

	void deleteByUserDetails(UserEntity user);

}
