package org.findy.findy_be.bookmark.application.update;

import static org.assertj.core.api.Assertions.*;
import static org.findy.findy_be.common.exception.ErrorCode.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.UpdateBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

class UpdateBookmarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private UpdateBookmarkService updateBookmarkService;

	private User user;
	private Bookmark bookmark;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now());
		bookmark = Bookmark.createCustomType("서촌", user);
	}

	@DisplayName("[성공] 즐겨찾기 이름 업데이트")
	@Test
	void 즐겨찾기_이름_업데이트() throws Exception {
		// given
		Long customBookmarkId = 1L;
		UpdateBookmarkRequest request = new UpdateBookmarkRequest("새로운 이름");
		when(bookmarkRepository.findById(customBookmarkId)).thenReturn(Optional.of(bookmark));

		// when
		updateBookmarkService.invoke(user.getUserId(), customBookmarkId, request);

		// then
		assertThat(bookmark.getName()).isEqualTo("새로운 이름");
		verify(bookmarkRepository, times(1)).findById(customBookmarkId);
	}

	@DisplayName("[실패] 존재하지 않는 즐겨찾기 ID일 경우 EntityNotFoundException 발생")
	@Test
	void 존재하지_않는_즐겨찾기_ID일_경우() {
		// given
		Long nonExistentBookmarkId = 999L;
		UpdateBookmarkRequest request = new UpdateBookmarkRequest("새로운 이름");
		when(bookmarkRepository.findById(nonExistentBookmarkId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> updateBookmarkService.invoke(user.getUserId(), nonExistentBookmarkId, request))
			.isInstanceOf(EntityNotFoundException.class)
			.hasMessageContaining(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), nonExistentBookmarkId));
	}

	@DisplayName("[실패] 다른 사용자의 즐겨찾기를 업데이트하려고 할 경우 IllegalArgumentException 발생")
	@Test
	void 다른_사용자의_즐겨찾기를_업데이트하려고_할_경우() {
		// given
		Long customBookmarkId = 1L;
		UpdateBookmarkRequest request = new UpdateBookmarkRequest("새로운 이름");
		when(bookmarkRepository.findById(customBookmarkId)).thenReturn(Optional.of(bookmark));

		// when & then
		assertThatThrownBy(() -> updateBookmarkService.invoke("다른사용자ID", customBookmarkId, request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
	}

	@DisplayName("[성공] 동일한 이름 요청 시 업데이트하지 않음")
	@Test
	void 동일한_이름_요청_시() throws Exception {
		// given
		Long customBookmarkId = 1L;
		UpdateBookmarkRequest request = new UpdateBookmarkRequest("서촌"); // 기존 이름과 동일
		when(bookmarkRepository.findById(customBookmarkId)).thenReturn(Optional.of(bookmark));

		// when
		updateBookmarkService.invoke(user.getUserId(), customBookmarkId, request);

		// then
		assertThat(bookmark.getName()).isEqualTo("서촌"); // 이름이 변경되지 않음
		verify(bookmarkRepository, times(1)).findById(customBookmarkId);
	}
}
