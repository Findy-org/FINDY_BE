package org.findy.findy_be.place.application.compare;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;

public interface FindPlace {

	Place invoke(PlaceRequest request);
}
