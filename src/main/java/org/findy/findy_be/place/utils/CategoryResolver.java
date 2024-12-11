package org.findy.findy_be.place.utils;

import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.vo.Category;

public class CategoryResolver {

	private static final String DELIMITER_ONE = ">";
	private static final String DELIMITER_TWO = ",";

	public static Category resolveCategory(String input) {
		String[] parts = input.split(DELIMITER_ONE);

		if (parts.length != 2) {
			parts = input.split(DELIMITER_TWO);
		}

		String firstCategory = parts[0].trim();
		String secondCategory = parts[1].trim();

		Category category = getCategoryBySecondCategory(secondCategory, firstCategory);
		if (category != null)
			return category;

		return getCategoryByFirstCategory(firstCategory, secondCategory);
	}

	private static Category getCategoryBySecondCategory(final String secondCategory, final String firstCategory) {
		return MiddleCategory.findByLabel(secondCategory)
			.filter(middleCategory -> middleCategory.matchesMajorCategoryLabel(firstCategory))
			.map(middleCategory -> Category.of(middleCategory.getMajorCategory().getLabel(), middleCategory.getLabel()))
			.orElse(null);
	}

	private static Category getCategoryByFirstCategory(final String firstCategory, final String secondCategory) {
		return MiddleCategory.findByLabel(firstCategory)
			.map(middleCategory -> Category.of(middleCategory.getMajorCategory().getLabel(), middleCategory.getLabel()))
			.orElse(Category.of(firstCategory, secondCategory));
	}

}
