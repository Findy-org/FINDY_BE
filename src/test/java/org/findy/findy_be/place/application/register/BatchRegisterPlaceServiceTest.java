package org.findy.findy_be.place.application.register;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.application.create.BatchCreateMarkerService;
import org.findy.findy_be.place.application.find.FindPlaceService;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BatchRegisterPlaceServiceTest extends MockTest {

	@Mock
	private FindPlaceService findPlace;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private BatchCreateMarkerService batchCreateMarker;

	@InjectMocks
	private BatchRegisterPlaceService batchRegisterPlaceService;

	private Bookmark testBookmark;
	private RegisterPlaceRequest request1;
	private RegisterPlaceRequest request2;
	private Place place1;
	private Place place2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testBookmark = mock(Bookmark.class);
		request1 = new RegisterPlaceRequest("Place1", "https://example.com/1", "Description1", "02-000-0000",
			"Address1", "RoadAddress1", "12345", "67890", MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		request2 = new RegisterPlaceRequest("Place2", "https://example.com/2", "Description2", "02-000-0001",
			"Address2", "RoadAddress2", "54321", "09876", MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		place1 = mock(Place.class);
		place2 = mock(Place.class);
	}

	@DisplayName("새로운 장소가 모두 등록되고 마커가 생성됨")
	@Test
	void 새로운_장소_모두_등록_및_마커_생성() {
		// given
		List<RegisterPlaceRequest> requests = Arrays.asList(request1, request2);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(findPlace.invoke(request2.title(), request2.roadAddress())).thenReturn(Optional.empty());
		when(placeRepository.bulkInsert(anyList())).thenReturn(Arrays.asList(place1, place2));

		// when
		batchRegisterPlaceService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).bulkInsert(anyList());
		verify(batchCreateMarker, times(1)).invoke(testBookmark, Arrays.asList(place1, place2));
	}

	@DisplayName("일부 장소는 이미 등록되어 새로 등록되지 않음")
	@Test
	void 일부_장소_이미_등록되어_새로_등록되지_않음() {
		// given
		List<RegisterPlaceRequest> requests = Arrays.asList(request1, request2);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(findPlace.invoke(request2.title(), request2.roadAddress())).thenReturn(Optional.of(place2));
		when(placeRepository.bulkInsert(anyList())).thenReturn(Arrays.asList(place1));

		// when
		batchRegisterPlaceService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).bulkInsert(anyList());
		verify(batchCreateMarker, times(1)).invoke(eq(testBookmark),
			argThat(list -> list.contains(place1) && list.contains(place2)));
	}

	@DisplayName("단일 장소가 등록되고 마커가 생성됨")
	@Test
	void 단일_장소_등록_및_마커_생성() {
		// given
		List<RegisterPlaceRequest> requests = Arrays.asList(request1);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(placeRepository.bulkInsert(anyList())).thenReturn(Arrays.asList(place1));

		// when
		batchRegisterPlaceService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).bulkInsert(anyList());
		verify(batchCreateMarker, times(1)).invoke(testBookmark, Arrays.asList(place1));
	}
}
