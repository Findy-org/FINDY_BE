package org.findy.findy_be.place.domain;

import org.findy.findy_be.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "places")
public class Place extends BaseEntity {

	@NotNull
	private String title;

	@NotNull
	private String link;

	private String description;

	private String telephone;

	@NotNull
	private String address;

	@NotNull
	private String roadAddress;

	@NotNull
	private String mapx;

	@NotNull
	private String mapy;

	@Enumerated(EnumType.STRING)
	@NotNull
	private MajorCategory majorCategory;

	@Enumerated(EnumType.STRING)
	private MiddleCategory middleCategory;

	public static Place create(final String address, final String description, final String link,
		final MajorCategory majorCategory, final String mapx, final String mapy, final MiddleCategory middleCategory,
		final String roadAddress, final String telephone, final String title) {
		return Place.builder()
			.address(address)
			.description(description)
			.link(link)
			.majorCategory(majorCategory)
			.mapx(mapx)
			.mapy(mapy)
			.middleCategory(middleCategory)
			.roadAddress(roadAddress)
			.telephone(telephone)
			.title(title)
			.build();
	}
}
