package org.findy.findy_be.place.utils;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.vo.Category;

public class CategoryResolver {

	private static final String DELIMITER = ">";

	public static Category resolveCategory(String input) {
		String[] parts = input.split(DELIMITER);

		if (parts.length != 2) {
			throw new IllegalArgumentException(String.format(BAD_REQUEST_CATEGORY_FORM_ERROR.getMessage(), input));
		}

		String firstCategory = parts[0].trim();
		String secondCategory = parts[1].trim();

		Category middleCategory = getCategoryBySecondCategory(secondCategory, firstCategory);
		if (middleCategory != null)
			return middleCategory;

		return getCategoryByFirstCategory(input, firstCategory);
	}

	private static Category getCategoryBySecondCategory(final String secondCategory, final String firstCategory) {
		return MiddleCategory.findByLabel(secondCategory)
			.filter(middleCategory -> middleCategory.matchesMajorCategoryLabel(firstCategory))
			.map(middleCategory -> Category.of(middleCategory.getMajorCategory(), middleCategory))
			.orElse(null);
	}

	private static Category getCategoryByFirstCategory(final String input, final String firstCategory) {
		return MiddleCategory.findByLabel(firstCategory)
			.map(middleCategory -> Category.of(middleCategory.getMajorCategory(), middleCategory))
			.orElseThrow(() -> new IllegalArgumentException(
				String.format(BAD_REQUEST_CATEGORY_NOT_FOUND_ERROR.getMessage(), input)));
	}

}
