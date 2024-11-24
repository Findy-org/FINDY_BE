package org.findy.findy_be.bookmark.dto.request;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.dto.request.RegisterNaverMarkerRequest;
import org.findy.findy_be.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "네이버 즐겨찾기 DTO")
public record RegisterNaverBookmarkRequest(

	@NotNull(message = " 이름은 비어있을 수 없습니다.")
	@Schema(description = "유튜버 이름", example = "걍밍경")
	String name,

	List<RegisterNaverMarkerRequest> places
) {
	public Bookmark toEntity(User user) {
		return Bookmark.createNaverType(name, user);
	}
}
