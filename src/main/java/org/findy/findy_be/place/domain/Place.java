package org.findy.findy_be.place.domain;

import org.findy.findy_be.common.entity.BaseEntity;
import org.findy.findy_be.place.dto.request.PlaceRequest;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

	public static Place create(final PlaceRequest placeRequest) {
		return Place.builder()
			.address(placeRequest.address())
			.description(placeRequest.description())
			.link(placeRequest.link())
			.majorCategory(placeRequest.majorCategory())
			.mapx(placeRequest.mapx())
			.mapy(placeRequest.mapy())
			.middleCategory(placeRequest.middleCategory())
			.roadAddress(placeRequest.roadAddress())
			.telephone(placeRequest.telephone())
			.title(placeRequest.title())
			.build();
	}
}
