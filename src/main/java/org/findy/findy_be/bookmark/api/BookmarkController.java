package org.findy.findy_be.bookmark.api;

import org.findy.findy_be.bookmark.api.swagger.BookmarkAPIPresentation;
import org.findy.findy_be.bookmark.application.find.FindAllPagedBookmarks;
import org.findy.findy_be.bookmark.application.register.RegisterYoutubeBookmark;
import org.findy.findy_be.bookmark.dto.request.PagedBookmarkRequest;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.common.dto.pagination.SliceResponse;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController implements BookmarkAPIPresentation {

	private final RegisterYoutubeBookmark registerYoutubeBookmark;
	private final FindAllPagedBookmarks findAllPagedBookmarks;

	@PostMapping("/youtube")
	public void registerPlace(@LoginUser User user, @Valid @RequestBody YoutubeBookmarkRequest request) {
		registerYoutubeBookmark.invoke(user.getUserId(), request);
	}

	@GetMapping
	public SliceResponse<BookmarkResponse> getBookmarkList(@LoginUser User user,
		@ModelAttribute PagedBookmarkRequest request) {
		return findAllPagedBookmarks.invoke(user.getUserId(), request.cursor(), request.size());
	}
}
