package org.findy.findy_be.place.repository;

import java.util.Optional;

import org.findy.findy_be.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

	@Query("SELECT p FROM Place p WHERE p.title = :title AND p.roadAddress = :roadAddress AND p.mapX = :mapX AND p.mapY = :mapY")
	Optional<Place> findPlaceByDetails(@Param("title") String title, @Param("roadAddress") String roadAddress,
		@Param("mapX") String mapX, @Param("mapY") String mapY);
}
