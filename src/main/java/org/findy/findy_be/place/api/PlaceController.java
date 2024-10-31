package org.findy.findy_be.place.api;

import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.place.api.swagger.PlaceAPIPresentation;
import org.findy.findy_be.place.application.register.RegisterPlace;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.PathVariable;
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

	@PostMapping("/{bookmarkId}")
	public void registerPlace(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@Valid @RequestBody RegisterPlaceRequest request) {
		registerPlace.invoke(user.getUserId(), request, bookmarkId);
	}
}
