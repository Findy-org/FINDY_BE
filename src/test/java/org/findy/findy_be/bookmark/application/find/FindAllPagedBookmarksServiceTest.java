package org.findy.findy_be.bookmark.application.find;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

class FindAllPagedBookmarksServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private FindAllPagedBookmarksService findAllPagedBookmarksService;

	private User testUser;
	private List<Bookmark> bookmarks;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		testUser = mock(User.class);
		bookmarks = List.of(
			Bookmark.createCustomType("내장소1", testUser),
			Bookmark.createCustomType("내장소2", testUser),
			Bookmark.createCustomType("내장소3", testUser),
			Bookmark.createCustomType("내장소4", testUser),
			Bookmark.createCustomType("내장소5", testUser),
			Bookmark.createCustomType("내장소6", testUser),
			Bookmark.createCustomType("내장소7", testUser),
			Bookmark.createCustomType("내장소8", testUser)
		);
	}

	@DisplayName("유저의 북마크 목록을 페이징으로 조회 - 다음 페이지가 있을 경우")
	@Test
	void 유저_북마크_조회_성공_다음페이지_있음() {
		// given
		Pageable pageable = PageRequest.of(0, 3);
		Slice<Bookmark> bookmarkSlice = new SliceImpl<>(bookmarks.subList(0, 3), pageable, true);

		when(bookmarkRepository.findBookmarksByUserId(eq(testUser.getUserId()), any(Pageable.class), anyLong()))
			.thenReturn(bookmarkSlice);

		// when
		SliceResponse<BookmarkResponse> response = findAllPagedBookmarksService.invoke(testUser.getUserId(), 0L, 3);

		// then
		assertThat(response.data().size()).isEqualTo(3);
		assertThat(response.hasNext()).isTrue();
		assertThat(response.nextCursor()).isEqualTo(bookmarks.get(2).getId());
	}

	@DisplayName("유저의 북마크 목록을 페이징으로 조회 - 다음 페이지가 없을 경우")
	@Test
	void 유저_북마크_조회_성공_다음페이지_없음() {
		// given
		Pageable pageable = PageRequest.of(0, 3);
		Slice<Bookmark> bookmarkSlice = new SliceImpl<>(bookmarks.subList(0, 2), pageable, false);

		when(bookmarkRepository.findBookmarksByUserId(eq(testUser.getUserId()), any(Pageable.class), anyLong()))
			.thenReturn(bookmarkSlice);

		// when
		SliceResponse<BookmarkResponse> response = findAllPagedBookmarksService.invoke(testUser.getUserId(), 0L, 3);

		// then
		assertThat(response.data().size()).isEqualTo(2);
		assertThat(response.hasNext()).isFalse();
		assertThat(response.nextCursor()).isNull();
	}
}
