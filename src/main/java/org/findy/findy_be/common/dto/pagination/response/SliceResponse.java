package org.findy.findy_be.common.dto.pagination.response;

import java.util.List;

public record SliceResponse<T>(
	List<T> data,
	int currentCursor,
	int pageSize,
	boolean hasNext,
	Long nextCursor
) {
	public SliceResponse(final List<T> data, final int currentCursor, final int pageSize, final boolean hasNext,
		final Long nextCursor) {
		this.data = data;
		this.currentCursor = currentCursor;
		this.pageSize = pageSize;
		this.hasNext = hasNext;
		this.nextCursor = nextCursor;
	}
}
