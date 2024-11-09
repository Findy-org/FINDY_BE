package org.findy.findy_be.place.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.CategoryRequest;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

class PlaceRepositoryCustomImplTest extends RepositoryTest {

	@Autowired
	private PlaceRepositoryCustomImpl placeRepository;

	@Autowired
	private PlaceRepository placeJpaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private MarkerRepository markerRepository;

	private User testUser;
	private Bookmark testBookmark;
	private List<Place> persistPlaces;

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
		testUser = userRepository.save(testUser);

		testBookmark = Bookmark.createCustomType("Test Bookmark", testUser);
		testBookmark = bookmarkRepository.save(testBookmark);

		initPlacesForBookmark(testBookmark, 10);
	}

	@DisplayName("[성공 (다음 페이지가 있는 경우)]유저의 특정 북마크에 저장된 장소 목록을 페이징 조회")
	@Test
	void 유저_북마크_장소_조회_성공_case1() {
		Pageable pageable = PageRequest.of(0, 5);
		Long cursor = 0L;

		Slice<Place> placeSlice = placeRepository.findPlacesByUserIdAndBookmarkId(testUser.getUserId(),
			testBookmark.getId(), pageable, cursor);

		assertThat(placeSlice.getContent()).hasSize(5);
		assertThat(placeSlice.hasNext()).isTrue();
		assertThat(placeSlice.getContent().get(0).getTitle()).isEqualTo("Test Place 1");
	}

	@DisplayName("[성공 (마지막 페이지)] 유저의 특정 북마크에 저장된 장소 목록을 페이징 조회")
	@Test
	void 유저_북마크_장소_조회_성공_case2() {
		Pageable pageable = PageRequest.of(0, 11);
		Long cursor = 0L;

		Slice<Place> placeSlice = placeRepository.findPlacesByUserIdAndBookmarkId(testUser.getUserId(),
			testBookmark.getId(), pageable, cursor);
		assertThat(placeSlice.getContent()).hasSize(10);
		assertThat(placeSlice.hasNext()).isFalse();
		assertThat(placeSlice.getContent().get(0).getTitle()).isEqualTo("Test Place 1");
	}

	private void initPlacesForBookmark(Bookmark bookmark, int count) {
		List<Place> places = IntStream.rangeClosed(1, count)
			.mapToObj(i -> Place.create(new RegisterPlaceRequest(
				"Test Place " + i,
				"Description " + i,
				"Address " + i,
				"RoadAddress " + i,
				new CategoryRequest(MajorCategory.RESTAURANT, MiddleCategory.KOREAN),
				"1269827323",
				"375719345",
				"02-000-000" + i
			)))
			.collect(Collectors.toList());

		persistPlaces = placeJpaRepository.saveAll(places);

		List<Marker> markers = places.stream()
			.map(place -> Marker.create(bookmark, place))
			.collect(Collectors.toList());

		markerRepository.saveAll(markers);
	}
}
