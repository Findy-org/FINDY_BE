package org.findy.findy_be.place.application.register;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.bookmark.application.find.FindBookMark;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
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
	private FindBookMark findBookMark;

	@Mock
	private CreateMarker createMarker;

	@Mock
	private PlaceRepository placeRepository;

	@InjectMocks
	private RegisterPlaceService registerPlaceService;

	private PlaceRequest placeRequest;
	private Bookmark bookmark;
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
			MiddleCategory.KOREAN,
			1L
		);
		bookmark = mock(Bookmark.class);
		place = mock(Place.class);
	}

	@DisplayName("장소가 없을 경우 새로운 장소를 저장하고 마커 생성")
	@Test
	void 장소가_없을_경우_새로운_장소를_저장하고_마커_생성() {
		// given
		when(findBookMark.invokeById(placeRequest.bookmarkId())).thenReturn(bookmark);
		when(findPlace.invoke(placeRequest)).thenReturn(Optional.empty());
		when(placeRepository.save(any(Place.class))).thenReturn(place);

		// when
		registerPlaceService.invoke(placeRequest);

		// then
		verify(placeRepository, times(1)).save(any(Place.class));
		verify(createMarker, times(1)).invoke(bookmark, place);
	}

	@DisplayName("이미 존재하는 장소가 있을 경우 마커만 생성")
	@Test
	void 이미_존재하는_장소가_있을_경우_마커만_생성() {
		// given
		when(findBookMark.invokeById(placeRequest.bookmarkId())).thenReturn(bookmark);
		when(findPlace.invoke(placeRequest)).thenReturn(Optional.of(place));

		// when
		registerPlaceService.invoke(placeRequest);

		// then
		verify(placeRepository, never()).save(any(Place.class));
		verify(createMarker, times(1)).invoke(bookmark, place);
	}
}