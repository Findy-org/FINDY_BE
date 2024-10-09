package org.findy.findy_be.bookmark.api;

import org.findy.findy_be.bookmark.application.init.InitBookmark;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookmarks")
public class BookmarkController {

	private final InitBookmark initBookmark;
}
