package org.findy.findy_be.marker.dto.response;

import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.place.dto.response.MarkerPlaceResponse;

public record PagedMarkerResponse(
	String bookmarkName,
	SliceResponse<MarkerPlaceResponse> markers
) {
	public static PagedMarkerResponse of(String bookmarkName, SliceResponse<MarkerPlaceResponse> markers) {
		return new PagedMarkerResponse(bookmarkName, markers);
	}
}
