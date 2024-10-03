package org.findy.findy_be.user.repository;

import org.findy.findy_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserId(String userId);
}
