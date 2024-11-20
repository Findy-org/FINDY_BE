package org.findy.findy_be.place.application.find;

import java.util.Optional;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.vo.Category;

public interface FindPlace {
	Optional<Place> invoke(final String title, final String roadAddress, final Category category);
}
