package org.findy.findy_be.place.repository;

import java.util.List;

import org.findy.findy_be.bookmark.domain.QBookmark;
import org.findy.findy_be.marker.domain.QMarker;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.QPlace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<Place> findPlacesByUserIdAndBookmarkId(String userId, Long bookmarkId, Pageable pageable,
		Long cursor) {
		QBookmark bookmark = QBookmark.bookmark;
		QMarker marker = QMarker.marker;
		QPlace place = QPlace.place;

		List<Place> places = queryFactory
			.select(place)
			.from(bookmark)
			.join(bookmark.markers, marker)
			.join(marker.place, place)
			.where(bookmark.user.userId.eq(userId)
				.and(bookmark.id.eq(bookmarkId))
				.and(cursor != null ? bookmark.id.gt(cursor) : null))
			.orderBy(bookmark.id.asc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = places.size() > pageable.getPageSize();
		if (hasNext) {
			places = places.subList(0, pageable.getPageSize());
		}

		return new SliceImpl<>(places, pageable, hasNext);
	}
}
