package org.findy.findy_be.place.application.register;

import static org.assertj.core.api.Assertions.*;
import static org.findy.findy_be.common.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.common.exception.custom.ForbiddenAccessException;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.CategoryRequest;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RegisterPlaceServiceTest extends MockTest {

	@Mock
	private FindPlace findPlace;

	@Mock
	private CreateMarker createMarker;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private RegisterPlaceService registerPlaceService;

	private RegisterPlaceRequest placeRequest;
	private User user;
	private Bookmark bookmark;
	private Place place;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.create(
			"N49sfgdahdKz_fp-223424er1N3D6kd",
			"나경호",
			"hoyana@naver.com",
			"Y",
			"https://github.com/account",
			SocialProviderType.NAVER,
			RoleType.USER,
			LocalDateTime.now(),
			LocalDateTime.now()
		);
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		placeRequest = new RegisterPlaceRequest(
			"동대문엽기떡볶이 종각점",
			"설명",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			categoryRequest,
			"1269827323",
			"375719345",
			"02-000-000"
		);

		bookmark = mock(Bookmark.class);
		when(bookmark.getUser()).thenReturn(user);
		when(bookmark.getBookmarkType()).thenReturn(BookmarkType.CUSTOM);
		place = mock(Place.class);
	}

	@DisplayName("장소가 없을 경우 새로운 장소를 저장하고 마커 생성")
	@Test
	void 장소가_없을_경우_새로운_장소를_저장하고_마커_생성() {
		// given
		Long bookmarkId = 1L;
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
		when(findPlace.invoke(placeRequest.title(), placeRequest.roadAddress())).thenReturn(Optional.empty());
		when(placeRepository.save(any(Place.class))).thenReturn(place);

		// when
		registerPlaceService.invoke(user.getUserId(), placeRequest, bookmarkId);

		// then
		verify(placeRepository, times(1)).save(any(Place.class));
		verify(createMarker, times(1)).invoke(bookmark, place);
	}

	@DisplayName("이미 존재하는 장소가 있을 경우 마커만 생성")
	@Test
	void 이미_존재하는_장소가_있을_경우_마커만_생성() {
		// given
		Long bookmarkId = 1L;
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
		when(findPlace.invoke(placeRequest.title(), placeRequest.roadAddress())).thenReturn(Optional.of(place));

		// when
		registerPlaceService.invoke(user.getUserId(), placeRequest, bookmarkId);

		// then
		verify(placeRepository, never()).save(any(Place.class));
		verify(createMarker, times(1)).invoke(bookmark, place);
	}

	@DisplayName("유튜브 즐겨찾기일 경우 IllegalArgumentException 발생")
	@Test
	void 유튜브_즐겨찾기일_경우_예외_발생() {
		// given
		Long bookmarkId = 1L;

		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
		when(bookmark.getBookmarkType()).thenReturn(BookmarkType.YOUTUBE);

		// when & then
		assertThatThrownBy(() -> registerPlaceService.invoke(user.getUserId(), placeRequest, bookmarkId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(BAD_REQUEST_YOUTUBE_BOOKMARK_REGISTER_ERROR.getMessage());
	}

	@DisplayName("다른 사용자의 북마크일 경우 ForbiddenAccessException 발생")
	@Test
	void 다른_사용자의_북마크일_경우_예외_발생() {
		// given
		Long bookmarkId = 1L;

		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
		User otherUser = mock(User.class);
		when(otherUser.getUserId()).thenReturn("다른사용자ID");
		when(bookmark.getUser()).thenReturn(otherUser);

		// when & then
		assertThatThrownBy(() -> registerPlaceService.invoke(user.getUserId(), placeRequest, bookmarkId))
			.isInstanceOf(ForbiddenAccessException.class)
			.hasMessage(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
	}

}
