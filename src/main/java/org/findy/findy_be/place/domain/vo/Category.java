package org.findy.findy_be.place.domain.vo;

import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "major_category")
	private MajorCategory majorCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "middle_category")
	private MiddleCategory middleCategory;

	public static Category of(MajorCategory major, MiddleCategory middle) {
		return new Category(major, middle);
	}
}
