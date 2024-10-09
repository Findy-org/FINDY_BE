package org.findy.findy_be.place.repository;

import static org.assertj.core.api.Assertions.*;

import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaceRepositoryTest extends RepositoryTest {

	@Autowired
	private PlaceRepository placeRepository;

	@DisplayName("주어진 상세 정보로 장소를 조회할 수 있다")
	@Test
	void 주어진_상세_정보로_장소를_조회할_수_있다() {
		// given
		Place savedPlace = placeRepository.save(Place.create(
			"서울특별시 종로구 공평동 124", "테스트 설명", "https://test-link.com",
			MajorCategory.RESTAURANT, "1269827323", "375719345",
			null, "서울특별시 종로구 삼봉로 100", "02-000-000", "테스트 장소"
		));

		// when
		Place foundPlace = placeRepository.findPlaceByDetails(
			savedPlace.getTitle(), savedPlace.getAddress(), savedPlace.getRoadAddress(),
			savedPlace.getMapx(), savedPlace.getMapy(), savedPlace.getMajorCategory()
		);

		// then
		assertThat(foundPlace).isNotNull();
		assertThat(foundPlace.getId()).isEqualTo(savedPlace.getId());
	}

	@DisplayName("주어진 상세 정보와 일치하는 장소가 없으면 null을 반환한다")
	@Test
	void 주어진_상세_정보와_일치하는_장소가_없으면_null을_반환한다() {
		// when
		Place foundPlace = placeRepository.findPlaceByDetails(
			"존재하지 않는 장소", "주소", "도로명 주소", "123456", "654321", MajorCategory.CAFE_AND_DESSERT
		);

		// then
		assertThat(foundPlace).isNull();
	}
}