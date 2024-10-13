package org.findy.findy_be.place.api.swagger;

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
	void registerPlace(@Valid @RequestBody PlaceRequest request);
}
