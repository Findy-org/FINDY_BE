package org.findy.findy_be.place.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	@NotNull
	@Column(name = "major_category")
	private String majorCategory;

	@Column(name = "middle_category")
	private String middleCategory;

	public static Category of(String major, String middle) {
		return new Category(major, middle);
	}
}
