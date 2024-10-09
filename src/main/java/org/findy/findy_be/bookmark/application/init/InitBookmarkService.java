package org.findy.findy_be.bookmark.application.init;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InitBookmarkService implements InitBookmark {

	private final BookmarkRepository bookmarkRepository;

	@Override
	public void invoke(User user) {
		List<Bookmark> bookmarks = Bookmark.initBookmarks(user);
		bookmarkRepository.saveAll(bookmarks);
	}
}
