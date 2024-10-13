package org.findy.findy_be.place.application.find;

import java.util.Optional;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.PlaceRequest;

public interface FindPlace {

	Optional<Place> invoke(PlaceRequest request);
}
