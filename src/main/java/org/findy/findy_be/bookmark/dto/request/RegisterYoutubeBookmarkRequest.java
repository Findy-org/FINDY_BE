package org.findy.findy_be.bookmark.dto.request;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.common.validation.ValidYoutuberId;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "유튜브 즐겨찾기 DTO")
public record RegisterYoutubeBookmarkRequest(

	@ValidYoutuberId(message = "유튜버 ID는 @으로 시작해야합니다.")
	@Schema(description = "유튜버 ID", example = "@iammingki")
	String youtuberId,

	@NotNull(message = "유튜버 이름은 비어있을 수 없습니다.")
	@Schema(description = "유튜버 이름", example = "걍밍경")
	String youtuberName,

	@Schema(description = "유튜버 프로필 link", example = "https://yt3.googleusercontent.com/ytc/AIdro_mieTH2WSE4oBMmczfLHB3HhikzOg1nz9tFD-MLad93Xnw=s160-c-k-c0x00ffffff-no-rj")
	String youtuberProfile,

	@Schema(description = "유튜브 링크", example = "https://www.youtube.com/watch?v=hE2wMo5Coco")
	String youtubeLink,

	List<RegisterYouTubeMarkerRequest> places
) {
	public Bookmark toEntity(User user) {
		return Bookmark.createYoutubeType(youtuberName, youtuberId, youtuberProfile, youtubeLink, user);
	}
}
