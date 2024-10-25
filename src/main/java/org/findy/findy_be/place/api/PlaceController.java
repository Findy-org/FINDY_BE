package org.findy.findy_be.place.api;

import org.findy.findy_be.place.api.swagger.PlaceAPIPresentation;
import org.findy.findy_be.place.application.register.RegisterPlace;
import org.findy.findy_be.place.dto.request.PlaceRequest;
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

	@PostMapping()
	public void registerPlace(@Valid @RequestBody PlaceRequest request) {
		registerPlace.invoke(request);
	}
}
