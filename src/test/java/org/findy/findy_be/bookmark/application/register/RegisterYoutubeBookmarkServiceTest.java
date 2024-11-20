package org.findy.findy_be.bookmark.application.register;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.RegisterYoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.application.register.BatchRegisterMarkerService;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RegisterYoutubeBookmarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@Mock
	private BatchRegisterMarkerService batchRegisterPlace;

	@InjectMocks
	private RegisterYoutubeBookmarkService registerYoutubeBookmarkService;

	private User testUser;
	private RegisterYoutubeBookmarkRequest request;
	private List<RegisterYouTubeMarkerRequest> selectedPlaces;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = mock(User.class);
		when(testUser.getUserId()).thenReturn("N49sfgdahdKz_fp-223424er1N3D6kd");

		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT.getLabel(),
			MiddleCategory.KOREAN.getLabel());
		selectedPlaces = List.of(
			new RegisterYouTubeMarkerRequest("Place1", "Description1", "Address1", "RoadAddress1", categoryRequest,
				"12345", "67890", "02-000-0000", "0.04")
		);
		request = new RegisterYoutubeBookmarkRequest("@iammingki", "걍밍경",
			"https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", selectedPlaces);

		Bookmark newBookmark = mock(Bookmark.class);
		when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(newBookmark);
	}

	@DisplayName("[성공] 새로운 유튜브 북마크 생성")
	@Test
	void 새로운_유튜브_북마크_생성() {
		// given
		when(bookmarkRepository.findByUserAndYoutuberId(testUser, request.youtuberId())).thenReturn(Optional.empty());

		// when
		registerYoutubeBookmarkService.invoke(testUser, request);

		// then
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verify(batchRegisterPlace, times(1)).invoke(any(Bookmark.class), eq(selectedPlaces));
	}
}
