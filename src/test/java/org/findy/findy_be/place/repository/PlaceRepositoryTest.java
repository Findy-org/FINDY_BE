package org.findy.findy_be.place.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;
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
		PlaceRequest request = new PlaceRequest(
			"동대문엽기떡볶이 종각점",
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
		Place savedPlace = placeRepository.save(Place.create(request));

		// when
		Place foundPlace = placeRepository.findPlaceByDetails(
			savedPlace.getTitle(), savedPlace.getRoadAddress(),
			savedPlace.getMapx(), savedPlace.getMapy()
		).get();

		// then
		assertThat(foundPlace).isNotNull();
		assertThat(foundPlace.getId()).isEqualTo(savedPlace.getId());
	}

	@DisplayName("주어진 상세 정보와 일치하는 장소가 없으면 null을 반환한다")
	@Test
	void 주어진_상세_정보와_일치하는_장소가_없으면_null을_반환한다() {
		// given // when
		Optional<Place> foundPlace = placeRepository.findPlaceByDetails(
			"존재하지 않는 장소", "도로명 주소", "123456", "654321"
		);

		// then
		assertThat(foundPlace).isEmpty();
	}
}