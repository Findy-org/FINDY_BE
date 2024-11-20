package org.findy.findy_be.marker.application.find;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.marker.dto.response.PagedMarkerResponse;
import org.findy.findy_be.place.dto.response.MarkerPlaceResponse;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllPagedMarkersService implements FindAllPagedMarkers {

	private final PlaceRepository placeRepository;
	private final BookmarkRepository bookmarkRepository;

	public PagedMarkerResponse invoke(final String userId, final Long bookmarkId, final Long cursor,
		final int size) {

		Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));
		;

		Pageable pageable = PageRequest.of(0, size);
		Slice<MarkerPlaceResponse> placeSlice = placeRepository.findPlacesByUserIdAndBookmarkId(userId, bookmarkId,
			pageable, cursor);

		List<MarkerPlaceResponse> data = placeSlice.getContent();
		Long nextCursor = placeSlice.hasNext() ? data.get(data.size() - 1).markerId() : null;

		SliceResponse<MarkerPlaceResponse> sliceResponse = new SliceResponse<>(data,
			cursor != null ? cursor.intValue() : 0, size, placeSlice.hasNext(),
			nextCursor);
		return PagedMarkerResponse.of(bookmark.getName(), sliceResponse);
	}
}
