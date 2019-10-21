package com.frankokafor.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.rest.models.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	/*by extending the paging and sorting repository we now have an option of adding pageable in our return method where we can 
	 * handle the amount of return values especially when calling a list.
	 * 
	 */
	
	@Query(value = "SELECT * FROM users WHERE email=?",nativeQuery = true)
	UserEntity findByEmail(String email);
	
	@Query(value = "SELECT * FROM users WHERE user_id=?",nativeQuery = true)
	UserEntity findByUserId(String userId);

}
