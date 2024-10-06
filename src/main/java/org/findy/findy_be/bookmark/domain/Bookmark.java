package org.findy.findy_be.bookmark.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.findy.findy_be.common.entity.BaseEntity;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.user.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {

	private String name;

	@Enumerated(EnumType.STRING)
	@NotNull
	private MajorCategory majorCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public static Bookmark of(String name, MajorCategory majorCategory, User user) {
		return Bookmark.builder()
			.name(name)
			.majorCategory(majorCategory)
			.user(user)
			.build();
	}

	public static List<Bookmark> initBookmarks(User user) {
		return Stream.of(MajorCategory.values())
			.map(category -> Bookmark.of(category.getLabel(), category, user))
			.collect(Collectors.toList());
	}
}
