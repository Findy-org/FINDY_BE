package org.findy.findy_be.place.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MiddleCategoryTest {

	@DisplayName("[성공] 중분류 라벨로 Enum을 찾을 수 있음")
	@Test
	void fromLabel_성공() {
		// given
		String label = "한식";

		// when
		MiddleCategory category = MiddleCategory.fromLabel(label);

		// then
		assertThat(category).isEqualTo(MiddleCategory.KOREAN);
	}

	@DisplayName("[성공] Enum 값을 라벨로 변환")
	@Test
	void getLabel_성공() {
		// given
		MiddleCategory category = MiddleCategory.KOREAN;

		// when
		String label = category.getLabel();

		// then
		assertThat(label).isEqualTo("한식");
	}

	@DisplayName("[성공] 중분류의 MajorCategory 라벨과 일치하는지 확인")
	@Test
	void matchesMajorCategoryLabel_성공() {
		// given
		MiddleCategory category = MiddleCategory.KOREAN;
		String majorLabel = "음식점";

		// when
		boolean matches = category.matchesMajorCategoryLabel(majorLabel);

		// then
		assertThat(matches).isTrue();
	}

	@DisplayName("[실패] 중분류의 MajorCategory 라벨이 일치하지 않음")
	@Test
	void matchesMajorCategoryLabel_실패() {
		// given
		MiddleCategory category = MiddleCategory.KOREAN;
		String incorrectMajorLabel = "카페,디저트";

		// when
		boolean matches = category.matchesMajorCategoryLabel(incorrectMajorLabel);

		// then
		assertThat(matches).isFalse();
	}
}
