package org.findy.findy_be.marker.dto.request;

import org.findy.findy_be.place.domain.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "마커 등록 요청 DTO")
public record RegisterNaverMarkerRequest(

	@NotNull(message = "장소명은 비어있을 수 없습니다.")
	@Schema(description = "장소명", example = "동대문<b>엽기떡볶이</b> 종각점")
	String title,

	@NotNull(message = "주소는 비어있을 수 없습니다.")
	@Schema(description = "주소", example = "서울특별시 종로구 공평동 124")
	String address,

	String category,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "x 좌표", example = "1269827323")
	String mapx,

	@NotNull(message = "좌표는 비어있을 수 없습니다.")
	@Schema(description = "y 좌표", example = "375719345")
	String mapy
) {

	public Place toPlaceEntity() {
		return Place.valueOfNaverMarker(this);
	}
}