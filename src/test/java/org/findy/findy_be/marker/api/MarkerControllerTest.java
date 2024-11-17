package org.findy.findy_be.marker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.IntegrationTest;
import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.marker.application.register.RegisterMarkerService;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.dto.request.RegisterSearchedMarkerRequest;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.repository.PlaceRepository;
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
class MarkerControllerTest extends IntegrationTest {

	@Autowired
	private RegisterMarkerService registerMarker;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private MarkerRepository markerRepository;

	@Autowired
	private PlaceRepository placeRepository;

	private User testUser;
	private Bookmark testBookmark;

	@BeforeEach
	void setUp() {
		testUser = User.create(
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
		testUser = userRepository.saveAndFlush(testUser);

		Bookmark bookmark = Bookmark.of("Test Bookmark", BookmarkType.CUSTOM, null, null, null, testUser);
		testBookmark = bookmarkRepository.save(bookmark);
		UserPrincipal userPrincipal = UserPrincipal.create(testUser);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			userPrincipal, null, userPrincipal.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@DisplayName("[성공] 장소 등록 API")
	@Test
	void 장소_등록_API_성공() throws Exception {
		// given
		Long bookmarkId = testBookmark.getId();
		RegisterSearchedMarkerRequest request = new RegisterSearchedMarkerRequest(
			"동대문엽기떡볶이 종각점",
			"음식점>분식",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345"
		);

		// when
		ResultActions resultActions = PostSearchedPlace(bookmarkId, request);

		// then
		resultActions
			.andExpect(status().isOk());
	}

	@DisplayName("[실패] 장소 등록 API 검증")
	@Test
	void 장소_등록_API_검증_실패() throws Exception {
		// given
		Long bookmarkId = testBookmark.getId();
		RegisterSearchedMarkerRequest invalidRequest = new RegisterSearchedMarkerRequest(
			null,
			"음식점>분식",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345"
		);

		// when
		ResultActions resultActions = PostSearchedPlace(bookmarkId, invalidRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("장소명은 비어있을 수 없습니다.")));
	}

	@DisplayName("[실패 case2] 장소 등록 API 존재하지 않는 즐겨찾기 ID")
	@Test
	void 장소_등록_API_예외_존재하지_않는_즐겨찾기_ID() throws Exception {
		// given
		Long invalidBookmarkId = 999L;
		RegisterSearchedMarkerRequest request = new RegisterSearchedMarkerRequest(
			"동대문엽기떡볶이 종각점",
			"음식점>분식",
			"설명",
			"02-000-000",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			"1269827323",
			"375719345"
		);

		// when
		ResultActions resultActions = PostSearchedPlace(invalidBookmarkId, request);

		// then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error").value("EntityNotFoundException"))
			.andExpect(jsonPath("$.message").value("해당 id : " + invalidBookmarkId + "의 즐겨찾기가 존재하지 않습니다."));
	}

	@DisplayName("[성공] 장소 조회 API")
	@Test
	public void 장소_조회_API_성공() throws Exception {
		// given

		Long bookmarkId = testBookmark.getId();
		int size = 5;
		Long cursor = 0L;
		PagedRequest pagedRequest = new PagedRequest(cursor, size);
		initPlacesForBookmark(testBookmark, 7);

		// when
		ResultActions resultActions = GetBookmarks(bookmarkId, pagedRequest);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(size))
			.andExpect(jsonPath("$.hasNext").value(true))
			.andExpect(jsonPath("$.nextCursor").isNotEmpty());
	}

	@DisplayName("[성공 case 2] 장소 조회 API-마지막_페이지")
	@Test
	void 장소_조회_API_마지막_페이지() throws Exception {
		// given
		Long bookmarkId = testBookmark.getId();
		int size = 15;
		Long cursor = 0L;
		PagedRequest pagedRequest = new PagedRequest(cursor, size);
		initPlacesForBookmark(testBookmark, 10);

		// when
		ResultActions resultActions = GetBookmarks(bookmarkId, pagedRequest);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(10))
			.andExpect(jsonPath("$.hasNext").value(false))
			.andExpect(jsonPath("$.nextCursor").doesNotExist());
	}

	private ResultActions GetBookmarks(final Long bookmarkId, final PagedRequest pagedRequest) throws Exception {
		return mvc.perform((get("/api/markers/{bookmarkId}", bookmarkId)
				.param("cursor", pagedRequest.cursor() == null ? "" : pagedRequest.cursor().toString())
				.param("size", String.valueOf(pagedRequest.size()))
				.contentType(MediaType.APPLICATION_JSON)))
			.andDo(print());
	}

	private ResultActions PostSearchedPlace(final Long bookmarkId,
		final RegisterSearchedMarkerRequest request) throws
		Exception {
		return mvc.perform(post("/api/markers/{bookmarkId}", bookmarkId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print());
	}

	private void initPlacesForBookmark(Bookmark bookmark, int count) {
		List<Place> places = IntStream.range(1, count + 1)
			.mapToObj(i -> new RegisterSearchedMarkerRequest(
				"Test Place " + i,
				"음식점>분식",
				"Description " + i,
				"02-000-000" + i,
				"Address " + i,
				"RoadAddress " + i,
				"1269827323",
				"375719345"
			).toEntity())
			.collect(Collectors.toList());

		List<Place> placeList = placeRepository.saveAll(places);

		List<Marker> markers = placeList.stream()
			.map(place -> Marker.createForCustomBookmark(bookmark, place))
			.collect(Collectors.toList());

		markerRepository.saveAll(markers);
	}

}
