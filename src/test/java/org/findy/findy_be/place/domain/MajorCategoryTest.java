package org.findy.findy_be.place.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MajorCategoryTest {

	@DisplayName("[성공] 문자열로 대분류를 Enum으로 변환")
	@Test
	void fromLabel_성공() {
		// given
		String label = "음식점";

		// when
		MajorCategory category = MajorCategory.fromLabel(label);

		// then
		assertThat(category).isEqualTo(MajorCategory.RESTAURANT);
	}

	@DisplayName("[실패] 존재하지 않는 문자열로 Enum 변환 시 예외 발생")
	@Test
	void fromLabel_실패_예외발생() {
		// given
		String invalidLabel = "잘못된값";

		// when & then
		assertThatThrownBy(() -> MajorCategory.fromLabel(invalidLabel))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("잘못된 대분류입니다. 대분류: " + invalidLabel);
	}

	@DisplayName("[성공] Enum을 문자열로 변환")
	@Test
	void getLabel_성공() {
		// given
		MajorCategory category = MajorCategory.RESTAURANT;

		// when
		String label = category.getLabel();

		// then
		assertThat(label).isEqualTo("음식점");
	}
}
