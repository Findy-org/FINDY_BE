package org.findy.findy_be.marker.api;

import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.marker.api.swagger.MarkerAPIPresentation;
import org.findy.findy_be.marker.application.find.FindAllPagedMarkers;
import org.findy.findy_be.marker.application.register.RegisterMarker;
import org.findy.findy_be.marker.dto.request.RegisterSearchedMarkerRequest;
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
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkerController implements MarkerAPIPresentation {

	private final RegisterMarker registerMarker;
	private final FindAllPagedMarkers findAllPagedMarkers;

	@PostMapping("/{bookmarkId}")
	public void registerMarker(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@Valid @RequestBody RegisterSearchedMarkerRequest request) {
		registerMarker.invoke(bookmarkId, request, user.getUserId());
	}

	@GetMapping("/{bookmarkId}")
	public SliceResponse<PlaceResponse> getMarkers(@LoginUser User user, @PathVariable("bookmarkId") Long bookmarkId,
		@ModelAttribute PagedRequest request) {
		return findAllPagedMarkers.invoke(user.getUserId(), bookmarkId, request.cursor(), request.size());
	}
}
