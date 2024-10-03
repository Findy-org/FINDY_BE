package org.findy.findy_be.bookmark.application;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;

	public void initBookmarks(User user) {
		List<Bookmark> bookmarks = Bookmark.initBookmarks(user);
		bookmarkRepository.saveAll(bookmarks);
	}
}
