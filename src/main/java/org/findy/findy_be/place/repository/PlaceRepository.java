package org.findy.findy_be.place.repository;

import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

	@Query("SELECT p FROM Place p WHERE p.title = :title AND p.address = :address " +
		"AND p.roadAddress = :roadAddress AND p.mapx = :mapx AND p.mapy = :mapy " +
		"AND p.majorCategory = :majorCategory")
	Place findPlaceByDetails(@Param("title") String title,
		@Param("address") String address, @Param("roadAddress") String roadAddress,
		@Param("mapx") String mapx, @Param("mapy") String mapy,
		@Param("majorCategory") MajorCategory majorCategory);
}
