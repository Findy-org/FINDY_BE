package org.findy.findy_be.place.application.find;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.PlaceRequest;
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

	private PlaceRequest placeRequest;
	private Place place;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		placeRequest = new PlaceRequest(
			"동대문 엽기떡볶이 종각점",
			"https://blog.naver.com/ddm_yupdduk",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345",
			MajorCategory.RESTAURANT,
			null,
			1L
		);
		place = mock(Place.class);
	}

	@DisplayName("주어진 PlaceRequest에 해당하는 장소를 성공적으로 조회")
	@Test
	void 주어진_PlaceRequest에_해당하는_장소를_성공적으로_조회() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy()
		)).thenReturn(Optional.of(place));

		// when
		Place foundPlace = findPlaceService.invoke(placeRequest).get();

		// then
		assertThat(foundPlace).isNotNull();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy()
		);
	}

	@DisplayName("주어진 PlaceRequest에 해당하는 장소가 없을 때 null을 반환")
	@Test
	void 주어진_PlaceRequest에_해당하는_장소가_없을_때_null을_반환() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy()
		)).thenReturn(Optional.empty());

		// when
		Optional<Place> foundPlace = findPlaceService.invoke(placeRequest);

		// then
		assertThat(foundPlace).isEmpty();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy()
		);
	}
}