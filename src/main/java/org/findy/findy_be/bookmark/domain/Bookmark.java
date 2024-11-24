package org.findy.findy_be.bookmark.domain;

import java.util.ArrayList;
import java.util.List;

import org.findy.findy_be.common.entity.BaseTimeEntity;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookmarks")
@SequenceGenerator(name = "bookmark_sequence", sequenceName = "bookmark_seq", allocationSize = 10)
public class Bookmark extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_sequence")
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	@NotNull
	private BookmarkType bookmarkType;

	private String youtuberId;
	private String youtuberProfile;
	private String youtubeLink;

	@NotNull
	private Long markersCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Marker> markers = new ArrayList<>();

	public void incrementMarkersCount(int incrementValue) {
		this.markersCount += incrementValue;
	}

	public void decreaseMarkersCount() {
		this.markersCount--;
	}

	public void updateName(String name) {
		this.name = this.name.equals(name) || name == null ? this.name : name;
	}

	public static Bookmark of(String name, BookmarkType type, String youtuberId, String youtuberProfile,
		String youtubeLink, User user) {
		return Bookmark.builder()
			.name(name)
			.bookmarkType(type)
			.youtuberId(youtuberId)
			.youtuberProfile(youtuberProfile)
			.youtubeLink(youtubeLink)
			.markersCount(0L)
			.user(user)
			.build();
	}

	public static Bookmark createYoutubeType(String name, String youtuberId, String youtuberProfile, String youtubeLink,
		User user) {
		return Bookmark.of(name, BookmarkType.YOUTUBE, youtuberId, youtuberProfile, youtubeLink, user);
	}

	public static Bookmark createCustomType(String name, User user) {
		return Bookmark.of(name, BookmarkType.CUSTOM, null, null, null, user);
	}
}
