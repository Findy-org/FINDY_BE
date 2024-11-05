package org.findy.findy_be.marker.domain;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.entity.BaseTimeEntity;
import org.findy.findy_be.place.domain.Place;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "markers")
@SequenceGenerator(name = "bookmark_sequence", sequenceName = "bookmark_seq")
public class Marker extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marker_sequence")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookmark_id")
	private Bookmark bookmark;

	public static Marker create(final Bookmark bookmark, final Place place) {
		return Marker.builder()
			.place(place)
			.bookmark(bookmark)
			.build();
	}
}
