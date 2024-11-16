package org.findy.findy_be.marker.application.domain.vo;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinate {

	@NotNull
	private String mapX;

	@NotNull
	private String mapY;

	public static Coordinate of(String mapX, String mapY) {
		return new Coordinate(mapX, mapY);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Coordinate))
			return false;
		Coordinate that = (Coordinate)o;
		return mapX.equals(that.mapX) && mapY.equals(that.mapY);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mapX, mapY);
	}
}
