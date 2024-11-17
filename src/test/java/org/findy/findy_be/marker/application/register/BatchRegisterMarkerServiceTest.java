package org.findy.findy_be.marker.application.register;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.application.create.BatchCreateMarkerService;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.application.find.FindPlaceService;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BatchRegisterMarkerServiceTest extends MockTest {

	@Mock
	private FindPlaceService findPlace;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private BatchCreateMarkerService batchCreateMarker;

	@InjectMocks
	private BatchRegisterMarkerService batchRegisterMarkerService;

	private Bookmark testBookmark;
	private RegisterYouTubeMarkerRequest request1;
	private RegisterYouTubeMarkerRequest request2;
	private Place place1;
	private Place place2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testBookmark = mock(Bookmark.class);
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		request1 = new RegisterYouTubeMarkerRequest("Place1", "Description1", "Address1", "RoadAddress1",
			categoryRequest,
			"12345", "67890", "02-000-0000", "0.04");
		request2 = new RegisterYouTubeMarkerRequest("Place2", "Description2", "Address2", "RoadAddress2",
			categoryRequest,
			"54321", "09876", "02-000-0001", "0.04");
		place1 = mock(Place.class);
		place2 = mock(Place.class);
	}

	@DisplayName("[성공] 새로운 장소가 모두 등록되고 마커가 생성됨")
	@Test
	void 새로운_장소_모두_등록_및_마커_생성() {
		// given
		List<RegisterYouTubeMarkerRequest> requests = Arrays.asList(request1, request2);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(findPlace.invoke(request2.title(), request2.roadAddress())).thenReturn(Optional.empty());
		when(placeRepository.saveAll(anyList())).thenReturn(Arrays.asList(place1, place2));

		// when
		batchRegisterMarkerService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).saveAll(anyList());
		verify(batchCreateMarker, times(1)).invoke(testBookmark, Arrays.asList(place1, place2), requests);
	}

	@DisplayName("[성공 case2] 일부 장소는 이미 등록되어 새로 등록되지 않음")
	@Test
	void 이미_등록된_장소_새로_등록되지_않음() {
		// given
		List<RegisterYouTubeMarkerRequest> requests = Arrays.asList(request1, request2);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(findPlace.invoke(request2.title(), request2.roadAddress())).thenReturn(Optional.of(place2));
		when(placeRepository.saveAll(anyList())).thenReturn(Arrays.asList(place1));

		// when
		batchRegisterMarkerService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).saveAll(anyList());
		verify(batchCreateMarker, times(1)).invoke(
			eq(testBookmark),
			argThat(list -> list.contains(place1) && list.contains(place2)),
			eq(requests)
		);
	}

	@DisplayName("[성공 case3] 단일 장소가 등록되고 마커가 생성됨")
	@Test
	void 단일_장소_등록_및_마커_생성() {
		// given
		List<RegisterYouTubeMarkerRequest> requests = Arrays.asList(request1);

		when(findPlace.invoke(request1.title(), request1.roadAddress())).thenReturn(Optional.empty());
		when(placeRepository.saveAll(anyList())).thenReturn(Arrays.asList(place1));

		// when
		batchRegisterMarkerService.invoke(testBookmark, requests);

		// then
		verify(placeRepository, times(1)).saveAll(anyList());
		verify(batchCreateMarker, times(1)).invoke(testBookmark, Arrays.asList(place1), requests);
	}
}
