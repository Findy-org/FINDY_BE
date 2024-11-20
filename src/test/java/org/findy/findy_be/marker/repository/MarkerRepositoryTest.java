package org.findy.findy_be.marker.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.RepositoryTest;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.dto.request.CategoryRequest;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MarkerRepositoryTest extends RepositoryTest {

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private MarkerRepository markerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	private User testUser;
	private Place testPlace;
	private Bookmark testBookmark;
	private Marker testMarker;

	@BeforeEach
	void setUp() {
		testUser = userRepository.save(User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now()));

		CategoryRequest categoryRequest = new CategoryRequest(MajorCategory.RESTAURANT.getLabel(),
			MiddleCategory.KOREAN.getLabel());
		RegisterYouTubeMarkerRequest request = new RegisterYouTubeMarkerRequest(
			"동대문엽기떡볶이 종각점",
			"설명",
			"서울특별시 종로구 공평동 124",
			"서울특별시 종로구 삼봉로 100",
			categoryRequest,
			"1269827323",
			"375719345",
			"02-000-000",
			"0.04"
		);
		testPlace = placeRepository.save(Place.create(request));
		testBookmark = bookmarkRepository.save(Bookmark.of("서촌", BookmarkType.CUSTOM, null, null, null, testUser));
		testMarker = markerRepository.save(Marker.createForCustomBookmark(testBookmark, testPlace));
		testMarker.changeBookmark(testBookmark);
	}

	@DisplayName("[성공] Bookmark 삭제 시 연관된 Marker도 삭제된다")
	@Test
	void 북마크_삭제시_마커도_삭제() {
		// given
		Long markerId = testMarker.getId();
		Optional<Marker> currentMarker = markerRepository.findById(markerId);
		assertThat(currentMarker.isPresent()).isTrue();

		// when
		bookmarkRepository.delete(testBookmark);

		// then
		Optional<Marker> resultMarker = markerRepository.findById(markerId);
		assertThat(resultMarker.isEmpty()).isTrue();
	}
}