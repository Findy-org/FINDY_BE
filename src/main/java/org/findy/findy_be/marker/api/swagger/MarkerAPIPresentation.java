package org.findy.findy_be.marker.api.swagger;

import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.common.meta.CustomApiResponse;
import org.findy.findy_be.common.meta.CustomApiResponses;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.marker.dto.request.RegisterSearchedMarkerRequest;
import org.findy.findy_be.place.dto.response.PlaceResponse;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Marker API", description = "마커 관련 API")
public interface MarkerAPIPresentation {

	@Operation(summary = "마커 등록", description = "새로운 마커"
		+ "를 등록하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "마커 등록 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 입력 값")
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "HttpMessageNotReadableException", status = 400, message = "유튜브 즐겨찾기는 장소를 추가할 수 없습니다.", description = "validation에 맞지 않은 요청을 할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : 1의 즐겨찾기가 존재하지 않습니다.", description = "장소를 저장할 즐겨찾기를 못 찾는 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "내부 서버 오류")
	})
	void registerMarker(@LoginUser User user, @PathVariable Long bookmarkId,
		@Valid @RequestBody RegisterSearchedMarkerRequest request);

	@Operation(summary = "마커 목록 조회", description = "유저의 마커 목록을 조회하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "마커 조회 성공")
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "IllegalArgumentException", status = 400, message = "잘못된 요청입니다.", description = "잘못된 쿼리 파라미터가 포함된 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "서버 내부에서 예기치 않은 오류가 발생한 경우")
	})
	SliceResponse<PlaceResponse> getMarkers(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@ModelAttribute PagedRequest request);
}
