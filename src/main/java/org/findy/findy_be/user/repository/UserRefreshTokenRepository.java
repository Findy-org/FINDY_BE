package org.findy.findy_be.user.repository;

import org.findy.findy_be.user.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
	UserRefreshToken findByUserId(String userId);

	UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
