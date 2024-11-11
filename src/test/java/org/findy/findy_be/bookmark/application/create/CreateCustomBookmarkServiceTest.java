package org.findy.findy_be.bookmark.application.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateCustomBookmarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private CreateCustomBookmarkService createCustomBookmarkService;

	private User testUser;
	private CreateCustomBookmarkRequest request;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock user setup
		testUser = mock(User.class);
		when(testUser.getUserId()).thenReturn("N49sfgdahdKz_fp-223424er1N3D6kd");

		request = new CreateCustomBookmarkRequest("서촌");
	}

	@DisplayName("[성공] 새로운 커스텀 북마크 생성")
	@Test
	void 새로운_커스텀_북마크_생성() {
		// given
		when(bookmarkRepository.findByName(request.name())).thenReturn(Optional.empty());

		// when
		createCustomBookmarkService.invoke(testUser, request);

		// then
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
	}

	@DisplayName("[실패] 이미 존재하는 북마크 이름으로 생성 시 예외 발생")
	@Test
	void 이미_존재하는_북마크_이름_예외() {
		// given
		Bookmark existingBookmark = mock(Bookmark.class);
		when(bookmarkRepository.findByName(request.name())).thenReturn(Optional.of(existingBookmark));

		// when & then
		IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
			createCustomBookmarkService.invoke(testUser, request)
		);

		assertTrue(exception.getMessage().contains("이미 존재하는 북마크 이름입니다"));
		verify(bookmarkRepository, never()).save(any(Bookmark.class));
	}
}
