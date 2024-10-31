package org.findy.findy_be.bookmark.application.register;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.place.application.register.BatchRegisterPlaceService;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.application.UserService;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RegisterYoutubeBookmarkServiceTest extends MockTest {

	@Mock
	private UserService userService;

	@Mock
	private BookmarkRepository bookmarkRepository;

	@Mock
	private BatchRegisterPlaceService batchRegisterPlace;

	@InjectMocks
	private RegisterYoutubeBookmarkService registerYoutubeBookmarkService;

	private User testUser;
	private YoutubeBookmarkRequest request;
	private Bookmark youtubeBookmark;
	private List<RegisterPlaceRequest> selectedPlaces;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = mock(User.class);
		when(testUser.getUserId()).thenReturn("N49sfgdahdKz_fp-223424er1N3D6kd");

		selectedPlaces = List.of(
			new RegisterPlaceRequest("Place1", "https://place1.com", "Description1", "02-000-0000",
				"Address1", "RoadAddress1", "12345", "67890", MajorCategory.RESTAURANT, MiddleCategory.KOREAN)
		);

		request = new YoutubeBookmarkRequest("@iammingki", "걍밍경", "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", selectedPlaces);

		youtubeBookmark = Bookmark.createYoutubeType(request.youtuberName(), request.youtuberId(),
			request.youtuberProfile(), testUser);
	}

	@DisplayName("새로운 유튜브 북마크 생성")
	@Test
	void 새로운_유튜브_북마크_생성() {
		// given
		when(userService.findUser(testUser.getUserId())).thenReturn(testUser);
		when(bookmarkRepository.findByUserAndYoutuberId(testUser, request.youtuberId())).thenReturn(Optional.empty());

		// when
		registerYoutubeBookmarkService.invoke(testUser.getUserId(), request);

		// then
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verify(batchRegisterPlace, times(1)).invoke(any(Bookmark.class), eq(selectedPlaces));
	}
}
