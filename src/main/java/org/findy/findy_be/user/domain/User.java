package org.findy.findy_be.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.info.OAuth2UserInfo;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@JsonIgnore
	@Id
	@Column(name = "user_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;

	@Column(name = "user_id", length = 64, unique = true)
	@NotNull
	@Size(max = 64)
	private String userId;

	@Column(name = "username", length = 100)
	@NotNull
	@Size(max = 100)
	private String username;

	@JsonIgnore
	@Column(name = "password", length = 128)
	@NotNull
	@Size(max = 128)
	private String password;

	@Column(name = "email", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "email_verified_yn", length = 1)
	@NotNull
	@Size(min = 1, max = 1)
	private String emailVerifiedYn;

	@Column(name = "profile_image_url", length = 512)
	@NotNull
	@Size(max = 512)
	private String profileImageUrl;

	@Column(name = "provider_type", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private SocialProviderType socialProviderType;

	@Column(name = "role_type", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private RoleType roleType;

	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP", name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP", name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Bookmark> bookmarks = new ArrayList<>();

	public void updateUser(OAuth2UserInfo userInfo) {
		if (userInfo.getName() != null && !this.username.equals(userInfo.getName())) {
			this.username = userInfo.getName();
		}

		if (userInfo.getImageUrl() != null && !this.profileImageUrl.equals(userInfo.getImageUrl())) {
			this.profileImageUrl = userInfo.getImageUrl();
		}
	}

	public static User create(
		@NotNull @Size(max = 64) String userId,
		@NotNull @Size(max = 100) String username,
		@NotNull @Size(max = 512) String email,
		@NotNull @Size(max = 1) String emailVerifiedYn,
		@NotNull @Size(max = 512) String profileImageUrl,
		@NotNull SocialProviderType socialProviderType,
		@NotNull RoleType roleType,
		@NotNull LocalDateTime createdAt,
		@NotNull LocalDateTime updatedAt
	) {
		email = email != null ? email : "NO_EMAIL";
		profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		return User.builder()
			.userId(userId)
			.username(username)
			.password("NO_PASS")
			.email(email)
			.emailVerifiedYn(emailVerifiedYn)
			.profileImageUrl(profileImageUrl)
			.socialProviderType(socialProviderType)
			.roleType(roleType)
			.createdAt(createdAt)
			.updatedAt(updatedAt)
			.build();
	}
}
