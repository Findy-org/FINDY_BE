package org.findy.findy_be.marker.application.find;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.response.PlaceResponse;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

class FindAllPagedMarkersServiceTest extends MockTest {

	@Mock
	private PlaceRepository placeRepository;

	@InjectMocks
	private FindAllPagedMarkersService findAllPagedMarkersService;

	private User testUser;
	private List<Place> places;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		testUser = mock(User.class);
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		places = IntStream.range(0, 5)
			.mapToObj(i -> new RegisterYouTubeMarkerRequest(
				"Test Place " + i,
				"Description " + i,
				"02-1234-5678",
				"Seoul Road " + i,
				categoryRequest,
				"1269827323",
				"375719345",
				"02-000-000",
				"0.04"
			))
			.map(Place::create)
			.collect(Collectors.toList());
	}

	@DisplayName("[성공 case1(다음페이지 있음)] 유저 마커 조회 성공")
	@Test
	void 유저_마커_조회_case1() {
		// given
		Long bookmarkId = 1L;
		Pageable pageable = PageRequest.of(0, 3);
		Slice<Place> placeSlice = new SliceImpl<>(places.subList(0, 3), pageable, true);

		when(placeRepository.findPlacesByUserIdAndBookmarkId(eq(testUser.getUserId()), eq(bookmarkId),
			any(Pageable.class),
			anyLong()))
			.thenReturn(placeSlice);

		// when
		SliceResponse<PlaceResponse> response = findAllPagedMarkersService.invoke(testUser.getUserId(), 1L, 0L, 3);

		// then
		assertThat(response.data().size()).isEqualTo(3);
		assertThat(response.hasNext()).isTrue();
		assertThat(response.nextCursor()).isEqualTo(places.get(2).getId());
	}

	@DisplayName("[성공 case2(다음페이지 없음)] 유저 마커 조회 성공")
	@Test
	void 유저_마커_조회_성공_case2() {
		// given
		Long bookmarkId = 1L;
		Pageable pageable = PageRequest.of(0, 3);
		Slice<Place> placeSlice = new SliceImpl<>(places.subList(0, 3), pageable, false);

		when(placeRepository.findPlacesByUserIdAndBookmarkId(eq(testUser.getUserId()), eq(bookmarkId),
			any(Pageable.class),
			anyLong()))
			.thenReturn(placeSlice);

		// when
		SliceResponse<PlaceResponse> response = findAllPagedMarkersService.invoke(testUser.getUserId(), 1L, 0L, 3);

		// then
		assertThat(response.data().size()).isEqualTo(3);
		assertThat(response.hasNext()).isFalse();
		assertThat(response.nextCursor()).isNull();
	}
}