package org.findy.findy_be.place.domain;

import org.findy.findy_be.common.entity.BaseEntity;
import org.findy.findy_be.place.domain.vo.Category;
import org.findy.findy_be.place.domain.vo.Coordinate;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

	@Embedded
	private Category category;

	public static Place create(final RegisterPlaceRequest request) {
		Coordinate coordinate = Coordinate.of(request.mapX(), request.mapY());
		Category category = Category.of(request.category().majorCategory(), request.category().middleCategory());
		return Place.builder()
			.address(request.address())
			.description(request.description())
			.category(category)
			.coordinate(coordinate)
			.roadAddress(request.roadAddress())
			.telephone(request.telephone())
			.title(request.title())
			.build();
	}
}
