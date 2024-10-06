package org.findy.findy_be.place.repository;

import org.findy.findy_be.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
