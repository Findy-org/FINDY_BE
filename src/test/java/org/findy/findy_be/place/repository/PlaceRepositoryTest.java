package org.findy.findy_be.place.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class PlaceRepositoryTest extends RepositoryTest {

	@Autowired
	private PlaceRepository placeRepository;

	@DisplayName("[성공] 주어진 상세 정보로 장소를 조회할 수 있다")
	@Test
	void 주어진_상세_정보로_장소_조회() {
		// given
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		RegisterYouTubeMarkerRequest request = new RegisterYouTubeMarkerRequest(
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
		Place savedPlace = placeRepository.save(Place.create(request));

		// when
		Place foundPlace = placeRepository.findPlaceByDetails(
			savedPlace.getTitle(),
			savedPlace.getRoadAddress()
		).orElse(null);

		// then
		assertThat(foundPlace).isNotNull();
		assertThat(foundPlace.getId()).isEqualTo(savedPlace.getId());
	}

	@DisplayName("[성공 case2] 주어진 상세 정보와 일치하는 장소가 없으면 null을 반환한다")
	@Test
	void 주어진_상세_정보와_일치하는_장소가_없으면_null() {
		// given // when
		Optional<Place> foundPlace = placeRepository.findPlaceByDetails(
			"존재하지 않는 장소", "도로명 주소"
		);

		// then
		assertThat(foundPlace).isEmpty();
	}

	@DisplayName("[성능] Bulk Insert를 통해 여러 장소를 일괄 삽입할 수 있다")
	@Test
	@Transactional
	void bulkInsertPlaces_여러_장소_일괄_삽입() {
		// given
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		List<Place> places = IntStream.range(0, 100)
			.mapToObj(i -> Place.create(new RegisterYouTubeMarkerRequest(
					"Test Place " + i,
					"Description " + i,
					"02-1234-5678",
					"Seoul Road " + i,
					categoryRequest,
					"1269827323",
					"375719345",
					"02-000-000",
					"0.04"
				)
			)).collect(Collectors.toList());

		// when
		placeRepository.saveAll(places);

		// then
		List<Place> savedPlaces = placeRepository.findAll();
		assertThat(savedPlaces).hasSize(100);
		for (int i = 0; i < 100; i++) {
			assertThat(savedPlaces.get(i).getTitle()).isEqualTo("Test Place " + i);
		}
	}
}
