package org.findy.findy_be.marker.application.delete;

import static org.findy.findy_be.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DeleteMarkerServiceTest extends MockTest {

	@Mock
	private MarkerRepository markerRepository;

	@InjectMocks
	private DeleteMarkerService deleteMarkerService;

	private User testUser;
	private Bookmark bookmark;
	private Place place;
	private Marker marker;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		testUser = mock(User.class);
		when(testUser.getUserId()).thenReturn("N49sfgdahdKz_fp-223424er1N3D6kd");

		bookmark = mock(Bookmark.class);
		when(bookmark.getUser()).thenReturn(testUser);

		place = mock(Place.class);

		marker = Marker.createForCustomBookmark(bookmark, place);
	}

	@DisplayName("[성공] 마커 삭제")
	@Test
	public void 마커_삭제() throws Exception {
		// given
		Long markerId = 1L;
		String userId = testUser.getUserId();
		when(markerRepository.findById(markerId)).thenReturn(Optional.of(marker));

		// when
		deleteMarkerService.invoke(userId, markerId);

		// then
		verify(markerRepository, times(1)).delete(marker);
	}

	@DisplayName("[실패] 다른 유저의 마커 삭제 시도")
	@Test
	void 마커_삭제_실패_다른유저() {
		// given
		Long markerId = 1L;
		String userId = "differentUserId";
		when(markerRepository.findById(markerId)).thenReturn(Optional.of(marker));

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> deleteMarkerService.invoke(userId, markerId));

		assertEquals(FORBIDDEN_MARKER_ACCESS.getMessage(), exception.getMessage());
	}
}
