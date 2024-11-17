package org.findy.findy_be.place.application.find;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
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

class FindPlaceServiceTest extends MockTest {

	@Mock
	private PlaceRepository placeRepository;

	@InjectMocks
	private FindPlaceService findPlaceService;

	private RegisterYouTubeMarkerRequest placeRequest;
	private Place place;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		placeRequest = new RegisterYouTubeMarkerRequest(
			"동대문엽기떡볶이 종각점",
			"설명",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			categoryRequest,
			"1269827323",
			"375719345",
			"02-000-000",
			"0.04"
		);
		place = mock(Place.class);
	}

	@DisplayName("[성공] 주어진 PlaceRequest에 해당하는 장소를 성공적으로 조회")
	@Test
	void PlaceRequest_장소_조회() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress()
		)).thenReturn(Optional.of(place));

		// when
		Place foundPlace = findPlaceService.invoke(placeRequest.title(), placeRequest.roadAddress()).get();

		// then
		assertThat(foundPlace).isNotNull();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress()
		);
	}

	@DisplayName("[성공 case2] 주어진 PlaceRequest에 해당하는 장소가 없을 때 null을 반환")
	@Test
	void PlaceRequest에_해당하는_장소가_없을_때() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress()
		)).thenReturn(Optional.empty());

		// when
		Optional<Place> foundPlace = findPlaceService.invoke(placeRequest.title(), placeRequest.roadAddress());

		// then
		assertThat(foundPlace).isEmpty();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress()
		);
	}
}