package org.findy.findy_be.marker.dto.request;

import static org.findy.findy_be.place.utils.CategoryResolver.*;

import org.findy.findy_be.marker.application.domain.Place;
import org.findy.findy_be.marker.application.domain.vo.Coordinate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "장소 DTO")
public record RegisterSearchedMarkerRequest(

	@NotNull(message = "장소명은 비어있을 수 없습니다.")
	@Schema(description = "장소명", example = "동대문<b>엽기떡볶이</b> 종각점")
	String title,

	@Schema(description = "카테고리(네이버형식)", example = "음식점>분식")
	String category,

	@Schema(description = "설명", example = "설명")
	String description,

	@Schema(description = "전화번호", example = "02-000-000")
	String telephone,

	@NotNull(message = "주소는 비어있을 수 없습니다.")
	@Schema(description = "주소", example = "서울특별시 종로구 공평동 124")
	String address,

	@NotNull(message = "도로명 주소는 비어있을 수 없습니다.")
	@Schema(description = "도로명 주소", example = "서울특별시 종로구 삼봉로 100")
	String roadAddress,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "x 좌표", example = "1269827323")
	String mapX,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "y 좌표", example = "375719345")
	String mapY
) {
	public Place toEntity() {
		Coordinate coordinate = Coordinate.of(this.mapX, this.mapY);
		return Place.builder()
			.title(this.title())
			.category(resolveCategory(this.category))
			.description(this.description())
			.telephone(this.telephone())
			.address(this.address())
			.roadAddress(this.roadAddress())
			.coordinate(coordinate)
			.build();
	}

}