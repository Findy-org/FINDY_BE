package org.findy.findy_be.bookmark.dto.request;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "custom 즐겨찾기 DTO")
public record CreateCustomBookmarkRequest(

	@NotNull(message = "북마크 이름은 비어있을 수 없습니다.")
	@Schema(description = "북마크 이름", example = "서촌")
	String name
) {
	public Bookmark toEntity(User user) {
		return Bookmark.createCustomType(name, user);
	}
}
