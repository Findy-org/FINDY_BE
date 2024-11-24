package org.findy.findy_be.place.domain;

import static org.findy.findy_be.place.utils.CategoryResolver.*;

import java.util.Objects;

import org.findy.findy_be.common.entity.BaseTimeEntity;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.marker.dto.request.RegisterNaverMarkerRequest;
import org.findy.findy_be.place.domain.vo.Category;
import org.findy.findy_be.place.domain.vo.Coordinate;

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

	public static Place valueOfYoutubeMarker(final RegisterMarkerRequest request) {
		Coordinate coordinate = Coordinate.of(request.mapx(), request.mapy());
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

	public static Place valueOfNaverMarker(final RegisterNaverMarkerRequest request) {
		Coordinate coordinate = Coordinate.of(request.mapx(), request.mapy());
		Category category = resolveCategory(request.category() + ">" + request.category());
		return Place.builder()
			.address(request.address())
			.description(null)
			.category(category)
			.coordinate(coordinate)
			.roadAddress(request.address())
			.telephone(null)
			.title(request.title())
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Place place = (Place)o;

		return Objects.equals(title, place.title) &&
			Objects.equals(address, place.address) &&
			Objects.equals(category.getMajorCategory(), place.category.getMajorCategory());
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, address, category.getMajorCategory());
	}

}
