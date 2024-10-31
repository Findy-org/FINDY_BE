package org.findy.findy_be.marker.application.create;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
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

class BatchCreateMarkerServiceTest extends MockTest {

	@Mock
	private MarkerRepository markerRepository;

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private BatchCreateMarkerService batchCreateMarkerService;

	private Bookmark testBookmark;
	private Place place1;
	private Place place2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testBookmark = mock(Bookmark.class);
		place1 = mock(Place.class);
		place2 = mock(Place.class);
	}

	@DisplayName("새로운 장소에 대한 마커 생성")
	@Test
	void 새로운_장소에_대한_마커_생성() {
		// given
		List<Place> places = Arrays.asList(place1, place2);

		when(bookmarkRepository.findById(anyLong())).thenReturn(Optional.of(testBookmark));
		when(markerRepository.findAllByBookmarkAndPlaces(eq(testBookmark), eq(places)))
			.thenReturn(List.of());

		// when
		batchCreateMarkerService.invoke(testBookmark, places);

		// then
		verify(markerRepository, times(1)).bulkInsert(anyList());
	}

	@DisplayName("이미 존재하는 마커가 있을 경우 새로운 마커를 생성하지 않음")
	@Test
	void 이미_존재하는_마커가_있을_경우_새로운_마커_생성_하지_않음() {
		// given
		List<Place> places = Arrays.asList(place1, place2);
		Marker existingMarker = mock(Marker.class);
		when(existingMarker.getPlace()).thenReturn(place2);

		when(bookmarkRepository.findById(anyLong())).thenReturn(Optional.of(testBookmark));
		when(markerRepository.findAllByBookmarkAndPlaces(eq(testBookmark), eq(places)))
			.thenReturn(List.of(existingMarker));

		// when
		batchCreateMarkerService.invoke(testBookmark, places);

		// then
		verify(markerRepository, times(1)).bulkInsert(anyList());
		verify(markerRepository, times(1)).findAllByBookmarkAndPlaces(testBookmark, places);
	}
}
