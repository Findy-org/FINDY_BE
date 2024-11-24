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
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
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
	private List<RegisterMarkerRequest> requests;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testBookmark = mock(Bookmark.class);

		place1 = mock(Place.class);
		when(place1.getTitle()).thenReturn("Place1");
		when(place1.getRoadAddress()).thenReturn("RoadAddress1");

		place2 = mock(Place.class);
		when(place2.getTitle()).thenReturn("Place2");
		when(place2.getRoadAddress()).thenReturn("RoadAddress2");

		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT.getLabel(),
			MiddleCategory.KOREAN.getLabel());
		RegisterMarkerRequest request1 = new RegisterMarkerRequest(
			"Place1", "Description1", "Address1", "RoadAddress1", categoryRequest,
			"12345", "67890", "02-000-0000", "0.04"
		);
		RegisterMarkerRequest request2 = new RegisterMarkerRequest(
			"Place2", "Description2", "Address2", "RoadAddress2", categoryRequest,
			"54321", "09876", "02-000-0001", "0.04"
		);
		requests = Arrays.asList(request1, request2);
	}

	@DisplayName("[성공] 새로운 장소에 대한 마커 생성되고 마킹된 장소 수가 업데이트")
	@Test
	void 새로운_장소() {
		// given
		List<Place> places = Arrays.asList(place1, place2);

		when(bookmarkRepository.findById(anyLong())).thenReturn(Optional.of(testBookmark));
		when(markerRepository.findAllByBookmarkAndPlaces(eq(testBookmark), eq(places)))
			.thenReturn(List.of());

		// when
		batchCreateMarkerService.invoke(testBookmark, places, requests);

		// then
		verify(markerRepository, times(1)).saveAll(anyList());
		verify(testBookmark, times(1)).incrementMarkersCount(2);
	}

	@DisplayName("[성공 case2] 이미 존재하는 마커가 있을 경우 새로운 마커를 생성하지 않음 마킹된 장소 수가 업데이트 없음")
	@Test
	void 이미_존재하는_마커가_있을_경우() {
		// given
		List<Place> places = Arrays.asList(place1, place2);
		Marker existingMarker = mock(Marker.class);
		when(existingMarker.getPlace()).thenReturn(place2);

		when(bookmarkRepository.findById(anyLong())).thenReturn(Optional.of(testBookmark));
		when(markerRepository.findAllByBookmarkAndPlaces(eq(testBookmark), eq(places)))
			.thenReturn(List.of(existingMarker));

		// when
		batchCreateMarkerService.invoke(testBookmark, places, requests);

		// then
		verify(markerRepository, times(1)).saveAll(anyList());
		verify(markerRepository, times(1)).findAllByBookmarkAndPlaces(testBookmark, places);
		verify(testBookmark, times(1)).incrementMarkersCount(0);
	}
}
