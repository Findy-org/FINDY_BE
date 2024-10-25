package org.findy.findy_be.place.api.swagger;

import org.findy.findy_be.common.meta.CustomApiResponse;
import org.findy.findy_be.common.meta.CustomApiResponses;
import org.findy.findy_be.place.dto.request.PlaceRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Place API", description = "장소 관련 API")
public interface PlaceAPIPresentation {

	@Operation(summary = "장소 등록", description = "새로운 장소를 등록하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "장소 등록 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 입력 값")
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "HttpMessageNotReadableException", status = 400, message = "Validation failed for argument [0] in public void org.findy.findy_be.place.api.PlaceController.registerPlace(org.findy.findy_be.place.dto.request.PlaceRequest) with 7 errors: [Field error in object 'placeRequest' on field 'link': rejected value [null]; codes [NotNull.placeRequest.link,NotNull.link,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [placeRequest.link,link]; arguments []; default message [link]]; default message [널이어서는 안됩니다]] [Field error in object 'placeRequest' on field 'address': rejected value [null]; codes [NotNull.placeRequest.address,NotNull.address,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [placeRequest.address,address]; arguments []; default message [address]]; default message [널이어서는 안됩니다]]", description = "validation에 맞지 않은 요청을 할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : 1의 즐겨찾기가 존재하지 않습니다.", description = "장소를 저장할 즐겨찾기를 못 찾는 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "내부 서버 오류")
	})
	void registerPlace(@Valid @RequestBody PlaceRequest request);
}
