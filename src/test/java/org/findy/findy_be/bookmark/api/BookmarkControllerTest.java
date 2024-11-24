package org.findy.findy_be.bookmark.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.bookmark.dto.request.RegisterYoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.dto.request.UpdateBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.IntegrationTest;
import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
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

	@Autowired
	private BookmarkRepository bookmarkRepository;

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

	@DisplayName("[성공] 유효한 유튜브 북마크 요청")
	@Test
	void 유튜브_북마크_등록_성공() throws Exception {
		// given
		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT.getLabel(),
			MiddleCategory.KOREAN.getLabel());
		List<RegisterMarkerRequest> selectedPlaces = List.of(
			new RegisterMarkerRequest("Place1", "Description1", "Address1", "RoadAddress1", categoryRequest,
				"12345",
				"67890", "02-000-0000", "0.04"));
		RegisterYoutubeBookmarkRequest request = new RegisterYoutubeBookmarkRequest(
			"@iammingki", "걍밍경", "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", selectedPlaces);

		// when
		ResultActions resultActions = PostYoutubeBookmark(request);

		// then
		resultActions
			.andExpect(status().isOk());
	}

	// 비지니스로 로직으로 인한 폐기
	// @DisplayName("[실패] 유튜브 북마크 요청 시 유효성 검증 실패 - 유튜버 ID 형식 오류")
	// @Test
	// void 유튜브_북마크_등록_유효성_검증_실패_유튜버_ID_형식() throws Exception {
	// 	// given
	// 	RegisterYoutubeBookmarkRequest invalidRequest = new RegisterYoutubeBookmarkRequest(
	// 		"iammingki", "걍밍경", "https://yt3.googleusercontent.com/ytc/...",
	// 		"https://www.youtube.com/watch?v=hE2wMo5Coco", null);
	//
	// 	// when
	// 	ResultActions resultActions = PostYoutubeBookmark(invalidRequest);
	//
	// 	// then
	// 	resultActions
	// 		.andExpect(status().isBadRequest())
	// 		.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("유튜버 ID는 @으로 시작해야합니다.")));
	// }

	@DisplayName("[실패] 유튜브 북마크 요청 시 유효성 검증 실패 - 유튜버 이름 누락")
	@Test
	void 유튜브_북마크_등록_유효성_검증_실패_유튜버_이름_누락() throws Exception {
		// given
		RegisterYoutubeBookmarkRequest invalidRequest = new RegisterYoutubeBookmarkRequest(
			"@iammingki", null, "https://yt3.googleusercontent.com/ytc/...",
			"https://www.youtube.com/watch?v=hE2wMo5Coco", null);

		// when
		ResultActions resultActions = PostYoutubeBookmark(invalidRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("유튜버 이름은 비어있을 수 없습니다.")));
	}

	@DisplayName("[성공] 유저의 북마크 리스트를 페이징으로 조회 성공")
	@Test
	void 유저_북마크_리스트_페이징_조회_성공() throws Exception {
		// given
		int size = 5;
		Long cursor = null;
		PagedRequest pagedRequest = new PagedRequest(cursor, size);
		initBookmark();

		// when
		ResultActions resultActions = GetPagedBookmarks(pagedRequest);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(size))
			.andExpect(jsonPath("$.hasNext").value(true))
			.andExpect(jsonPath("$.nextCursor").isNotEmpty());
	}

	@DisplayName("[성공 case2] 유저의 북마크 리스트 페이징 조회 - 마지막 페이지일 경우")
	@Test
	void 유저_북마크_리스트_페이징_조회_마지막_페이지() throws Exception {
		// given
		int size = 11;
		Long cursor = 0L;
		PagedRequest pagedRequest = new PagedRequest(cursor, size);
		initBookmark();

		// when
		ResultActions resultActions = GetPagedBookmarks(pagedRequest);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(10))
			.andExpect(jsonPath("$.hasNext").value(false))
			.andExpect(jsonPath("$.nextCursor").isEmpty());
	}

	@DisplayName("[성공] 유효한 커스텀 북마크 요청")
	@Test
	void 커스텀_북마크_등록_성공() throws Exception {
		// given
		CreateCustomBookmarkRequest request = new CreateCustomBookmarkRequest("서촌");

		// when
		ResultActions resultActions = PostCustomBookmark(request);

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("[실패] 이미 존재하는 이름으로 커스텀 북마크 요청")
	@Test
	void 커스텀_북마크_등록_실패_중복_이름() throws Exception {
		// given
		bookmarkRepository.save(Bookmark.of("서촌", BookmarkType.CUSTOM, null, null, null, testUser));
		CreateCustomBookmarkRequest duplicateRequest = new CreateCustomBookmarkRequest("서촌");

		// when
		ResultActions resultActions = PostCustomBookmark(duplicateRequest);

		// then
		resultActions.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("이미 존재하는 북마크 이름입니다")));
	}

	@DisplayName("[성공] 북마크 삭제 요청")
	@Test
	void 북마크_삭제_요청() throws Exception {
		// given
		Bookmark bookmark = bookmarkRepository.save(Bookmark.of("서촌", BookmarkType.CUSTOM, null, null, null, testUser));

		// when
		DeleteBookmark(bookmark.getId());

		// then
		Optional<Bookmark> resultBookmark = bookmarkRepository.findById(bookmark.getId());
		Assertions.assertThat(resultBookmark.isEmpty()).isTrue();
	}

	@DisplayName("[성공] 북마크 수정 요청")
	@Test
	void 북마크_이름_수정() throws Exception {
		// given
		Bookmark bookmark = bookmarkRepository.save(Bookmark.of("서촌", BookmarkType.CUSTOM, null, null, null, testUser));
		UpdateBookmarkRequest request = new UpdateBookmarkRequest("안국");

		// when
		PutBookmark(bookmark.getId(), request);

		// then
		Optional<Bookmark> resultBookmark = bookmarkRepository.findById(bookmark.getId());
		Assertions.assertThat(resultBookmark.get().getName()).isEqualTo("안국");
	}

	private @NotNull ResultActions PostYoutubeBookmark(Object content) throws Exception {
		return mvc.perform(post("/api/bookmarks/youtube")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(content)))
			.andDo(print());
	}

	private @NotNull ResultActions GetPagedBookmarks(final PagedRequest pagedRequest) throws Exception {
		return mvc.perform(get("/api/bookmarks")
				.param("cursor", pagedRequest.cursor() == null ? "" : pagedRequest.cursor().toString())
				.param("size", String.valueOf(pagedRequest.size()))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());
	}

	private @NotNull ResultActions PostCustomBookmark(Object content) throws Exception {
		return mvc.perform(post("/api/bookmarks/custom")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(content)))
			.andDo(print());
	}

	private @NotNull ResultActions DeleteBookmark(Long id) throws Exception {
		return mvc.perform(delete("/api/bookmarks/{id}", id))
			.andDo(print());
	}

	private @NotNull ResultActions PutBookmark(Long id, UpdateBookmarkRequest request) throws Exception {
		return mvc.perform(put("/api/bookmarks/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print());
	}

	private void initBookmark() {
		List<Bookmark> bookmarks = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			bookmarks.add(Bookmark.of("Bookmark" + i, BookmarkType.CUSTOM, null, null, null, testUser));
		}
		bookmarkRepository.saveAll(bookmarks);
	}
}
