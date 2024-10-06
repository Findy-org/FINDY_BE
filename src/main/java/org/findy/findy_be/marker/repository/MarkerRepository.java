package org.findy.findy_be.marker.repository;

import org.findy.findy_be.marker.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepository extends JpaRepository<Marker, Long> {
}
