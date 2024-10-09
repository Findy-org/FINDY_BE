package org.findy.findy_be.place.api;

import org.findy.findy_be.common.meta.CustomApiResponse;
import org.findy.findy_be.common.meta.CustomApiResponses;
import org.findy.findy_be.place.api.swagger.PlaceAPIPresentation;
import org.findy.findy_be.place.application.register.RegisterPlace;
import org.findy.findy_be.place.dto.PlaceRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController implements PlaceAPIPresentation {

	private final RegisterPlace registerPlace;

	@CustomApiResponses({
		@CustomApiResponse(error = "HttpMessageNotReadableException", status = 400, message = "Validation failed for argument [0] in public void org.findy.findy_be.place.api.PlaceController.registerPlace(org.findy.findy_be.place.dto.PlaceRequest) with 7 errors: [Field error in object 'placeRequest' on field 'link': rejected value [null]; codes [NotNull.placeRequest.link,NotNull.link,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [placeRequest.link,link]; arguments []; default message [link]]; default message [널이어서는 안됩니다]] [Field error in object 'placeRequest' on field 'address': rejected value [null]; codes [NotNull.placeRequest.address,NotNull.address,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [placeRequest.address,address]; arguments []; default message [address]]; default message [널이어서는 안됩니다]]", description = "validation에 맞지 않은 요청을 할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : 1의 즐겨찾기가 존재하지 않습니다.", description = "장소를 저장할 즐겨찾기를 못 찾는 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "내부 서버 오류")
	})
	@PostMapping("/register")
	public void registerPlace(@Valid @RequestBody PlaceRequest request) {
		registerPlace.invoke(request);
	}
}
