package org.findy.findy_be.marker.application.domain;

import org.findy.findy_be.common.entity.BaseTimeEntity;
import org.findy.findy_be.marker.application.domain.vo.Category;
import org.findy.findy_be.marker.application.domain.vo.Coordinate;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@SequenceGenerator(name = "place_sequence", sequenceName = "place_seq", allocationSize = 10)
public class Place extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marker_sequence")
	private Long id;

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

	public static Place create(final RegisterMarkerRequest request) {
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
