package org.findy.findy_be.user.entity;

import java.time.LocalDateTime;

import org.findy.findy_be.auth.oauth.entity.SocialProviderType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@JsonIgnore
	@Id
	@Column(name = "USER_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;

	@Column(name = "USER_ID", length = 64, unique = true)
	@NotNull
	@Size(max = 64)
	private String userId;

	@Column(name = "USERNAME", length = 100)
	@NotNull
	@Size(max = 100)
	private String username;

	@JsonIgnore
	@Column(name = "PASSWORD", length = 128)
	@NotNull
	@Size(max = 128)
	private String password;

	@Column(name = "EMAIL", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "EMAIL_VERIFIED_YN", length = 1)
	@NotNull
	@Size(min = 1, max = 1)
	private String emailVerifiedYn;

	@Column(name = "PROFILE_IMAGE_URL", length = 512)
	@NotNull
	@Size(max = 512)
	private String profileImageUrl;

	@Column(name = "PROVIDER_TYPE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private SocialProviderType socialProviderType;

	@Column(name = "ROLE_TYPE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private RoleType roleType;

	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP", name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP", name = "updated_at")
	private LocalDateTime updatedAt;

	public User(
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
		this.userId = userId;
		this.username = username;
		this.password = "NO_PASS";
		this.email = email != null ? email : "NO_EMAIL";
		this.emailVerifiedYn = emailVerifiedYn;
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.socialProviderType = socialProviderType;
		this.roleType = roleType;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
