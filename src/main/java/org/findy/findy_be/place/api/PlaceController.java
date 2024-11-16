package org.findy.findy_be.place.api;

import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.place.api.swagger.PlaceAPIPresentation;
import org.findy.findy_be.place.application.find.FindAllPagedPlaces;
import org.findy.findy_be.place.application.register.RegisterPlace;
import org.findy.findy_be.place.dto.request.RegisterSearchedPlaceRequest;
import org.findy.findy_be.place.dto.response.PlaceResponse;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	private final FindAllPagedPlaces findAllPagedPlaces;

	@PostMapping("/{bookmarkId}")
	public void registerPlace(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@Valid @RequestBody RegisterSearchedPlaceRequest request) {
		registerPlace.invoke(bookmarkId, request, user.getUserId());
	}

	@GetMapping("/{bookmarkId}")
	public SliceResponse<PlaceResponse> getPlaces(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@ModelAttribute PagedRequest request) {
		return findAllPagedPlaces.invoke(user.getUserId(), bookmarkId, request.cursor(), request.size());
	}
}
