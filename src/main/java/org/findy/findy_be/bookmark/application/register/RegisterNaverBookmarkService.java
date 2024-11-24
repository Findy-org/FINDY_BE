package org.findy.findy_be.bookmark.application.register;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.RegisterNaverBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.marker.application.register.BatchRegisterMarker;
import org.findy.findy_be.marker.dto.request.RegisterNaverMarkerRequest;
import org.findy.findy_be.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisterNaverBookmarkService implements RegisterNaverBookmark {

	private final BookmarkRepository bookmarkRepository;
	private final BatchRegisterMarker<RegisterNaverMarkerRequest> batchRegisterMarker;

	@Override
	public void invoke(final User user, final RegisterNaverBookmarkRequest request) {
		List<RegisterNaverMarkerRequest> placeRequests = request.places();

		Bookmark newBookmark = bookmarkRepository.save(request.toEntity(user));
		batchRegisterMarker.invoke(newBookmark, placeRequests);
	}
}
