package org.findy.findy_be.place.dto.request;

import org.findy.findy_be.bookmark.dto.request.CategoryRequest;
import org.findy.findy_be.place.domain.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "장소 DTO")
public record RegisterPlaceRequest(

	@NotNull(message = "장소명은 비어있을 수 없습니다.")
	@Schema(description = "장소명", example = "동대문<b>엽기떡볶이</b> 종각점")
	String title,

	@Schema(description = "설명", example = "설명")
	String description,

	@NotNull(message = "주소는 비어있을 수 없습니다.")
	@Schema(description = "주소", example = "서울특별시 종로구 공평동 124")
	String address,

	@NotNull(message = "도로명 주소는 비어있을 수 없습니다.")
	@Schema(description = "도로명 주소", example = "서울특별시 종로구 삼봉로 100")
	String roadAddress,

	CategoryRequest category,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "x 좌표", example = "1269827323")
	String mapX,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "y 좌표", example = "375719345")
	String mapY,

	@Schema(description = "전화번호", example = "02-000-000")
	String telephone
) {
	public Place toEntity() {
		return Place.create(this);
	}

}