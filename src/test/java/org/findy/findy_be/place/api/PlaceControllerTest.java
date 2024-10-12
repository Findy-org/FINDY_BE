package org.findy.findy_be.place.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.IntegrationTest;
import org.findy.findy_be.place.application.register.RegisterPlace;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.dto.PlaceRequest;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WithMockUser(roles = "USER")
class PlaceControllerTest extends IntegrationTest {

	@Autowired
	private RegisterPlace registerPlace;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private PlaceController placeController;

	@BeforeEach
	void setUp() {
		User user = User.create(
			"N49sfgdahdKz_fp-223424er1N3D6kd",
			"나경호",
			"hoyana@naver.com",
			"Y",
			"https://github.com/account",
			SocialProviderType.NAVER,
			RoleType.USER,
			LocalDateTime.now(),
			LocalDateTime.now()
		);
		User savedUser = userRepository.save(user);
		Bookmark bookmark = Bookmark.of("Test Bookmark", BookmarkType.CUSTOM, null, savedUser);
		bookmarkRepository.save(bookmark);
	}

	@Test
	void 장소_등록_API_성공() throws Exception {
		// given
		Long bookmarkId = bookmarkRepository.findAll().get(0).getId();
		PlaceRequest request = new PlaceRequest(
			"동대문엽기떡볶이 종각점",
			"https://blog.naver.com/ddm_yupdduk",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345",
			MajorCategory.RESTAURANT,
			MiddleCategory.KOREAN,
			bookmarkId
		);

		// when
		ResultActions resultActions = perfromPostRegisterPlace(request);

		// then
		resultActions
			.andExpect(status().isOk());
	}

	@Test
	void 장소_등록_API_검증_실패() throws Exception {
		// given
		PlaceRequest invalidRequest = new PlaceRequest(
			null,
			null,
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345",
			MajorCategory.RESTAURANT,
			MiddleCategory.KOREAN,
			1L
		);

		// when
		ResultActions resultActions = perfromPostRegisterPlace(invalidRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("장소명은 비어있을 수 없습니다.")))
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("링크는 비어있을 수 없습니다.")));
	}

	@Test
	void 장소_등록_API_존재하지_않는_즐겨찾기_ID() throws Exception {
		// given
		PlaceRequest requestWithInvalidBookmarkId = new PlaceRequest(
			"동대문엽기떡볶이 종각점",
			"https://blog.naver.com/ddm_yupdduk",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345",
			MajorCategory.RESTAURANT,
			MiddleCategory.KOREAN,
			999L
		);

		// when
		ResultActions resultActions = perfromPostRegisterPlace(requestWithInvalidBookmarkId);

		// then: 404 오류 메시지 확인
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error").value("EntityNotFoundException"))
			.andExpect(jsonPath("$.message").value("해당 id : 999의 즐겨찾기가 존재하지 않습니다."));
	}

	private @NotNull ResultActions perfromPostRegisterPlace(final PlaceRequest request) throws Exception {
		return mvc.perform(post("/api/places")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print());
	}
}