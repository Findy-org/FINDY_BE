package org.findy.findy_be.marker.repository;

import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.repository.BulkInsertRepository;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepository extends JpaRepository<Marker, Long>, BulkInsertRepository<Marker> {
	Optional<Marker> findByBookmarkAndPlace(Bookmark bookmark, Place place);
}
