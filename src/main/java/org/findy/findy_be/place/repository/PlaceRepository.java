package org.findy.findy_be.place.repository;

import java.util.Optional;

import org.findy.findy_be.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

	@Query("SELECT p FROM Place p WHERE p.title = :title AND p.roadAddress = :roadAddress")
	Optional<Place> findPlaceByDetails(@Param("title") String title, @Param("roadAddress") String roadAddress);
}
