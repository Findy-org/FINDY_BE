package org.findy.findy_be.bookmark.api.swagger;

import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.common.dto.pagination.request.PagedRequest;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.common.meta.CustomApiResponse;
import org.findy.findy_be.common.meta.CustomApiResponses;
import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.user.domain.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Bookmark API", description = "북마크 관련 API")
public interface BookmarkAPIPresentation {

	@Operation(summary = "유튜브 북마크 등록", description = "유저가 유튜브 북마크를 등록하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "유튜브 북마크 등록 성공"),
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "IllegalArgumentException", status = 400, message = "유튜브 즐겨찾기는 장소를 추가할 수 없습니다.", description = "유튜브 북마크에 잘못된 요청이 있는 경우"),
		@CustomApiResponse(error = "ForbiddenAccessException", status = 403, message = "해당 즐겨찾기에 접근할 권한이 없습니다.", description = "권한이 없는 유저가 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : {id}의 즐겨찾기가 존재하지 않습니다.", description = "존재하지 않는 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "서버 내부에서 예기치 않은 오류가 발생한 경우")
	})
	void registerYoutubeBookmark(@LoginUser User user, @Valid @RequestBody YoutubeBookmarkRequest request);

	@Operation(summary = "커스텀 북마크 등록", description = "유저가 커스텀 북마크를 등록하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "커스텀 북마크 등록 성공"),
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "BadRequestException", status = 400, message = "이미 존재하는 북마크 이름입니다: 서촌", description = "이미 존재하는 이름의 커스텀 북마크를 등록하려는 경우"),
		@CustomApiResponse(error = "ForbiddenAccessException", status = 403, message = "해당 즐겨찾기에 접근할 권한이 없습니다.", description = "권한이 없는 유저가 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "서버 내부에서 예기치 않은 오류가 발생한 경우")
	})
	void registerCustomBookmark(@LoginUser User user, @Valid @RequestBody CreateCustomBookmarkRequest request);

	@Operation(summary = "북마크 목록 조회", description = "유저가 등록한 북마크 목록을 조회하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "북마크 목록 조회 성공")
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "IllegalArgumentException", status = 400, message = "잘못된 요청입니다.", description = "잘못된 쿼리 파라미터가 포함된 경우"),
		@CustomApiResponse(error = "ForbiddenAccessException", status = 403, message = "해당 즐겨찾기에 접근할 권한이 없습니다.", description = "권한이 없는 유저가 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : {id}의 즐겨찾기가 존재하지 않습니다.", description = "존재하지 않는 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "서버 내부에서 예기치 않은 오류가 발생한 경우")
	})
	SliceResponse<BookmarkResponse> getBookmarkList(@LoginUser User user, @ModelAttribute PagedRequest request);

	@Operation(summary = "북마크 삭제", description = "북마크를 삭제하는 API", responses = {
		@ApiResponse(responseCode = "200", description = "북마크 삭제 성공")
	})
	@CustomApiResponses({
		@CustomApiResponse(error = "IllegalArgumentException", status = 400, message = "잘못된 요청입니다.", description = "잘못된 쿼리 파라미터가 포함된 경우"),
		@CustomApiResponse(error = "ForbiddenAccessException", status = 403, message = "해당 즐겨찾기에 접근할 권한이 없습니다.", description = "권한이 없는 유저가 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "EntityNotFoundException", status = 404, message = "해당 id : {id}의 즐겨찾기가 존재하지 않습니다.", description = "존재하지 않는 즐겨찾기에 접근할 경우"),
		@CustomApiResponse(error = "InternalServerError", status = 500, message = "내부 서버 오류가 발생했습니다.", description = "서버 내부에서 예기치 않은 오류가 발생한 경우")
	})
	void deleteBookmark(@LoginUser User user, @PathVariable("id") Long bookmarkId);
}
