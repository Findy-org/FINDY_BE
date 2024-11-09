package org.findy.findy_be.bookmark.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.common.RepositoryTest;
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

class BookmarkRepositoryCustomImplTest extends RepositoryTest {

	@Autowired
	private BookmarkRepositoryCustomImpl bookmarkRepository;

	@Autowired
	private BookmarkRepository bookmarkJpaRepository;

	@Autowired
	private UserRepository userRepository;

	private User testUser;

	@BeforeEach
	void setUp() {
		testUser = User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now());
		userRepository.save(testUser);
	}

	@DisplayName("[성공] 주어진 사용자 ID와 커서 및 페이지 크기로 북마크를 페이지네이션 조회")
	@Test
	void 사용자_ID_cursor_및_page_size_bookmark_pagination_조회() {
		// given
		initBookmarks(20);

		Long cursor = 10L;
		int size = 5;
		Pageable pageable = PageRequest.of(0, size);

		// when
		Slice<Bookmark> resultSlice = bookmarkRepository.findBookmarksByUserId(testUser.getUserId(), pageable, cursor);

		// then
		assertThat(resultSlice.getContent()).hasSize(size);
		assertThat(resultSlice.hasNext()).isTrue();
		assertThat(resultSlice.getContent().get(0).getId()).isGreaterThan(cursor);
		assertThat(resultSlice.getContent().get(0).getUser().getUserId()).isEqualTo(testUser.getUserId());
	}

	@DisplayName("[성공 case2] 마지막 페이지에서 다음 페이지가 없는 상태를 확인할 수 있다")
	@Test
	void 마지막_페이지에서_다음_페이지가_없는_상태_확인() {
		// given
		initBookmarks(5);

		Long cursor = 3L;
		int size = 2;
		Pageable pageable = PageRequest.of(0, size);

		// when
		Slice<Bookmark> resultSlice = bookmarkRepository.findBookmarksByUserId(testUser.getUserId(), pageable, cursor);

		// then
		assertThat(resultSlice.getContent()).hasSize(2);
		assertThat(resultSlice.hasNext()).isFalse();
	}

	private void initBookmarks(final int endInclusive) {
		List<Bookmark> bookmarks = IntStream.rangeClosed(1, endInclusive)
			.mapToObj(i -> Bookmark.builder()
				.name("Bookmark " + i)
				.bookmarkType(BookmarkType.YOUTUBE)
				.youtuberId("YoutuberId" + i)
				.youtuberProfile("https://example.com/profile" + i)
				.markersCount((long)(i * 10))
				.user(testUser)
				.build())
			.collect(Collectors.toList());
		bookmarkJpaRepository.saveAll(bookmarks);
	}
}
