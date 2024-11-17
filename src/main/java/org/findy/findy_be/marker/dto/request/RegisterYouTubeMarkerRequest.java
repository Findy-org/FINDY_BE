package org.findy.findy_be.marker.dto.request;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.place.domain.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "마커 등록 요청 DTO")
public record RegisterYouTubeMarkerRequest(

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
	String telephone,

	@Schema(description = "타임 스탬프", example = "0.04")
	String timestamp
) {

	public Place toPlaceEntity() {
		return Place.create(this);
	}

	public Marker toMarkerEntity(Bookmark bookmark) {
		Place place = toPlaceEntity();
		return Marker.createForYoutubeBookmark(timestamp, bookmark, place);
	}
}