package org.findy.findy_be.place.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.findy.findy_be.common.exception.ErrorCode;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.vo.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryResolverTest {

	@DisplayName("[성공 case1] 정상적인 '음식점>한식' 입력을 Category로 변환")
	@Test
	void 음식점과_한식_입력시_Category로_정상변환() {
		String input = "음식점>한식";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.RESTAURANT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.KOREAN.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@DisplayName("[성공 case2] 정상적인 '카페,디저트>아이스크림' 입력을 Category로 변환")
	@Test
	void 카페디저트와_아이스크림_입력시_Category로_정상변환() {
		String input = "카페,디저트>아이스크림";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.CAFE_AND_DESSERT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.ICE_CREAM.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@DisplayName("[성공 case3] '아이스크림>아돈노' 입력 시 첫번째 입력을 기준으로 Category로 변환")
	@Test
	void 첫번째_카테고리만으로_추정하여_Category_반환() {
		String input = "아이스크림>아돈노";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.CAFE_AND_DESSERT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.ICE_CREAM.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@DisplayName("[성공 case4] 정상적인 '음식점 > 한식' 입력을 Category로 변환")
	@Test
	void 음식점과_한식_입력시_Category로_정상변환_공백포함() {
		String input = "음식점 > 한식";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.RESTAURANT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.KOREAN.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@Test
	@DisplayName("[성공 case5] 중분류만 주어질 때 대분류를 추정하여 변환")
	void 중분류만_주어질때_대분류_추정하여_Category_변환() {
		String input = "한식 > 정의되지 않은 값";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.RESTAURANT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.KOREAN.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@Test
	@DisplayName("[성공 case6] 정상적인 '카페,디저트 > 아이스크림' 입력을 Category로 변환")
	void 카페디저트와_아이스크림_입력시_Category로_정상변환_공백포함() {
		String input = "카페,디저트 > 아이스크림";
		Category result = CategoryResolver.resolveCategory(input);

		assertThat(MajorCategory.CAFE_AND_DESSERT.getLabel()).isEqualTo(result.getMajorCategory());
		assertThat(MiddleCategory.ICE_CREAM.getLabel()).isEqualTo(result.getMiddleCategory());
	}

	@Test
	@DisplayName("[실패] 잘못된 형식의 입력이 있을 때 예외 발생")
	void 잘못된_형식의_입력시_예외발생() {
		String input = "음식점";

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> CategoryResolver.resolveCategory(input));

		assertThat(String.format(ErrorCode.BAD_REQUEST_CATEGORY_FORM_ERROR.getMessage(), input)).isEqualTo(
			exception.getMessage());
	}

	@Test
	@DisplayName("[실패 case2] 존재하지 않는 카테고리 입력의 경우 그대로 저장합니다.")
	void 존재하지_않는_카테고리_입력시_그대로_저장() {
		String input = "음식점 > 존재하지 않는 값";

		Category result = CategoryResolver.resolveCategory(input);

		assertThat(result.getMajorCategory()).isEqualTo("음식점");
		assertThat(result.getMiddleCategory()).isEqualTo("존재하지 않는 값");
	}
}
