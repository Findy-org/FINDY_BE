package org.findy.findy_be.bookmark.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.common.IntegrationTest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.dto.request.CategoryRequest;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WithMockUser(roles = "USER")
class BookmarkControllerTest extends IntegrationTest {

	@Autowired
	private UserRepository userRepository;

	private User testUser;

	@BeforeEach
	void setUp() {
		testUser = userRepository.saveAndFlush(User.create(
			"N49sfgdahdKz_fp-223424er1N3D6kd",
			"나경호",
			"hoyana@naver.com",
			"Y",
			"https://github.com/account",
			SocialProviderType.NAVER,
			RoleType.USER,
			LocalDateTime.now(),
			LocalDateTime.now()
		));

		UserPrincipal userPrincipal = UserPrincipal.create(testUser);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			userPrincipal, null, userPrincipal.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@DisplayName("유효한 유튜브 북마크 요청으로 성공")
	@Test
	void 유튜브_북마크_등록_성공() throws Exception {
		// given
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN);
		List<RegisterPlaceRequest> selectedPlaces = List.of(
			new RegisterPlaceRequest("Place1", "Description1", "Address1", "RoadAddress1", categoryRequest, "12345",
				"67890", "02-000-0000"));
		YoutubeBookmarkRequest request = new YoutubeBookmarkRequest(
			"@iammingki", "걍밍경", "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", selectedPlaces);

		// when
		ResultActions resultActions = performPostRequest("/api/bookmarks/youtube", request);

		// then
		resultActions
			.andExpect(status().isOk());
	}

	@DisplayName("유튜브 북마크 요청 시 유효성 검증 실패 - 유튜버 ID 형식 오류")
	@Test
	void 유튜브_북마크_등록_유효성_검증_실패_유튜버_ID_형식() throws Exception {
		// given
		YoutubeBookmarkRequest invalidRequest = new YoutubeBookmarkRequest(
			"iammingki", "걍밍경", "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", null);

		// when
		ResultActions resultActions = performPostRequest("/api/bookmarks/youtube", invalidRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("유튜버 ID는 @으로 시작해야합니다.")));
	}

	@DisplayName("유튜브 북마크 요청 시 유효성 검증 실패 - 유튜버 이름 누락")
	@Test
	void 유튜브_북마크_등록_유효성_검증_실패_유튜버_이름_누락() throws Exception {
		// given
		YoutubeBookmarkRequest invalidRequest = new YoutubeBookmarkRequest(
			"@iammingki", null, "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", null);

		// when
		ResultActions resultActions = performPostRequest("/api/bookmarks/youtube", invalidRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("유튜버 이름은 비어있을 수 없습니다.")));
	}

	private ResultActions performPostRequest(String url, Object content) throws Exception {
		return mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(content)))
			.andDo(print());
	}
}
