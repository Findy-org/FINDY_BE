package org.findy.findy_be.place.domain;

import org.findy.findy_be.common.entity.BaseEntity;
import org.findy.findy_be.place.domain.vo.Coordinate;
import org.findy.findy_be.place.dto.request.PlaceRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
	@Column(name = "address")
	private String address;

	@NotNull
	@Column(name = "road_address")
	private String roadAddress;

	@Embedded
	private Coordinate coordinate;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "major_category")
	private MajorCategory majorCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "middle_category")
	private MiddleCategory middleCategory;

	public static Place create(final PlaceRequest request) {
		Coordinate coordinate = Coordinate.of(request.mapX(), request.mapY());
		return Place.builder()
			.address(request.address())
			.description(request.description())
			.link(request.link())
			.majorCategory(request.majorCategory())
			.coordinate(coordinate)
			.middleCategory(request.middleCategory())
			.roadAddress(request.roadAddress())
			.telephone(request.telephone())
			.title(request.title())
			.build();
	}
}

