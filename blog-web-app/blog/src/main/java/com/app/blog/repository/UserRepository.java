package com.app.blog.repository;

import com.app.blog.models.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	Optional<Users> findByUserName(String username);
	
	Optional<Users> findByEmailAndPassword(String email, String password);

	Optional<Users> findByEmail(String email);


}
