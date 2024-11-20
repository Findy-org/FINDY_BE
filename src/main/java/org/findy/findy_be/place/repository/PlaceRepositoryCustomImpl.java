package org.findy.findy_be.place.repository;

import java.util.List;
import java.util.Objects;

import org.findy.findy_be.bookmark.domain.QBookmark;
import org.findy.findy_be.marker.domain.QMarker;
import org.findy.findy_be.place.domain.QPlace;
import org.findy.findy_be.place.dto.response.MarkerPlaceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<MarkerPlaceResponse> findPlacesByUserIdAndBookmarkId(String userId, Long bookmarkId, Pageable pageable,
		Long cursor) {
		QBookmark bookmark = QBookmark.bookmark;
		QMarker marker = QMarker.marker;
		QPlace place = QPlace.place;

		List<Tuple> results = queryFactory
			.select(marker.id, place)
			.from(bookmark)
			.join(bookmark.markers, marker)
			.join(marker.place, place)
			.where(bookmark.user.userId.eq(userId)
				.and(bookmark.id.eq(bookmarkId))
				.and(cursor != null ? marker.id.gt(cursor) : null))
			.orderBy(bookmark.id.asc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<MarkerPlaceResponse> responses = results.stream()
			.map(tuple -> MarkerPlaceResponse.of(
				tuple.get(marker.id),
				Objects.requireNonNull(tuple.get(place))
			))
			.toList();

		boolean hasNext = responses.size() > pageable.getPageSize();
		if (hasNext) {
			responses = responses.subList(0, pageable.getPageSize());
		}

		return new SliceImpl<>(responses, pageable, hasNext);
	}
}
