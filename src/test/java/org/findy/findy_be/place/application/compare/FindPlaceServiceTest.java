package org.findy.findy_be.place.application.compare;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;
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
	void whenPlaceExists_returnPlace() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.address(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy(),
			placeRequest.majorCategory()
		)).thenReturn(place);

		// when
		Place foundPlace = findPlaceService.invoke(placeRequest);

		// then
		assertThat(foundPlace).isNotNull();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.address(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy(),
			placeRequest.majorCategory()
		);
	}

	@DisplayName("주어진 PlaceRequest에 해당하는 장소가 없을 때 null을 반환")
	@Test
	void whenPlaceDoesNotExist_returnNull() {
		// given
		when(placeRepository.findPlaceByDetails(
			placeRequest.title(),
			placeRequest.address(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy(),
			placeRequest.majorCategory()
		)).thenReturn(null);

		// when
		Place foundPlace = findPlaceService.invoke(placeRequest);

		// then
		assertThat(foundPlace).isNull();
		verify(placeRepository, times(1)).findPlaceByDetails(
			placeRequest.title(),
			placeRequest.address(),
			placeRequest.roadAddress(),
			placeRequest.mapx(),
			placeRequest.mapy(),
			placeRequest.majorCategory()
		);
	}
}