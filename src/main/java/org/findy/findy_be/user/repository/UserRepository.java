package org.findy.findy_be.user.repository;

import java.util.Optional;

import org.findy.findy_be.user.entity.SocialType;
import org.findy.findy_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByOauthId(final String oauthId);

	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String refreshToken);

	Optional<User> findByRefreshToken(String refreshToken);

	Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String id);
}
