package org.findy.findy_be.place.domain.vo;

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
	private String mapx;

	@NotNull
	private String mapy;

	public static Coordinate of(String mapx, String mapy) {
		return new Coordinate(mapx, mapy);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Coordinate))
			return false;
		Coordinate that = (Coordinate)o;
		return mapx.equals(that.mapx) && mapy.equals(that.mapy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mapx, mapy);
	}
}
