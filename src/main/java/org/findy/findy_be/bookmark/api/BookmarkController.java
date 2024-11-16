package org.findy.findy_be.bookmark.api;

import org.findy.findy_be.bookmark.api.swagger.BookmarkAPIPresentation;
import org.findy.findy_be.bookmark.application.create.CreateCustomBookmark;
import org.findy.findy_be.bookmark.application.delete.DeleteBookmark;
import org.findy.findy_be.bookmark.application.find.FindAllPagedBookmarks;
import org.findy.findy_be.bookmark.application.register.RegisterYoutubeBookmark;
import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	private final CreateCustomBookmark createCustomBookmark;
	private final FindAllPagedBookmarks findAllPagedBookmarks;
	private final DeleteBookmark deleteBookmark;

	@PostMapping("/youtube")
	public void registerYoutubeBookmark(@LoginUser User user, @Valid @RequestBody YoutubeBookmarkRequest request) {
		registerYoutubeBookmark.invoke(user, request);
	}

	@PostMapping("/custom")
	public void registerCustomBookmark(@LoginUser User user, @Valid @RequestBody CreateCustomBookmarkRequest request) {
		createCustomBookmark.invoke(user, request);
	}

	@GetMapping
	public SliceResponse<BookmarkResponse> getBookmarkList(@LoginUser User user,
		@ModelAttribute PagedRequest request) {
		return findAllPagedBookmarks.invoke(user.getUserId(), request.cursor(), request.size());
	}

	@DeleteMapping("/{id}")
	public void deleteBookmark(@LoginUser User user, @PathVariable("id") Long bookmarkId) {
		deleteBookmark.invoke(user.getUserId(), bookmarkId);
	}
}
