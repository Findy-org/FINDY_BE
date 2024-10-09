package org.findy.findy_be.marker.application.create;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateMarkerServiceTest extends MockTest {

	@Mock
	private MarkerRepository markerRepository;

	@InjectMocks
	private CreateMarkerService createMarkerService;

	private Bookmark bookmark;
	private Place place;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		bookmark = mock(Bookmark.class);
		place = mock(Place.class);
	}

	@DisplayName("북마크와 장소 정보를 이용해 마커를 생성하고 저장")
	@Test
	void 북마크와_장소_정보를_이용해_마커를_생성하고_저장() {
		// given
		Marker marker = Marker.create(bookmark, place);
		when(markerRepository.findByBookmarkAndPlace(bookmark, place)).thenReturn(Optional.empty());
		when(markerRepository.save(any(Marker.class))).thenReturn(marker);

		// when
		createMarkerService.invoke(bookmark, place);

		// then
		verify(markerRepository, times(1)).save(any(Marker.class));
	}

	@DisplayName("이미 존재하는 마커가 있을 경우 새로운 마커를 저장하지 않음")
	@Test
	void 이미_존재하는_마커가_있을_경우_새로운_마커를_저장하지_않음() {
		// given
		Marker existingMarker = Marker.create(bookmark, place);
		when(markerRepository.findByBookmarkAndPlace(bookmark, place)).thenReturn(Optional.of(existingMarker));

		// when
		createMarkerService.invoke(bookmark, place);

		// then
		verify(markerRepository, never()).save(any(Marker.class));
	}
}