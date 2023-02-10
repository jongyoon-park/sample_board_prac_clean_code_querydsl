package com.sample.freeboard.domain.board.controller;

import com.sample.freeboard.domain.account.service.AuthService;
import com.sample.freeboard.global.annotation.AnyoneCallable;
import com.sample.freeboard.domain.board.dto.Boards;
import com.sample.freeboard.global.dto.response.ListResponse;
import com.sample.freeboard.domain.board.dto.BoardDTO;
import com.sample.freeboard.domain.board.dto.BoardResponse;
import com.sample.freeboard.global.dto.filter.Pagination;
import com.sample.freeboard.domain.board.service.BoardService;
import com.sample.freeboard.domain.recommend.service.RecommendService;
import com.sample.freeboard.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("sample/board")
public class BoardController {

    private final BoardService boardService;
    private final AuthService authService;
    private final RecommendService recommendService;
    private final UserService userService;

    @GetMapping("boards")
    @AnyoneCallable
    public ListResponse<Boards> getReadableBoards(@RequestBody Pagination pagination, @RequestHeader ("Authentication") String accountId) {
        return boardService.getReadableBoards(userService.findUserByAccountId(accountId), pagination);
    }

    @PostMapping("")
    public void writeBoard(@RequestBody BoardDTO boardDTO, @RequestHeader("Authentication") String accountId) {
        boardService.writeBoard(boardDTO, accountId);
    }

    @GetMapping("{boardId}")
    @AnyoneCallable
    public BoardResponse getBoard(@PathVariable("boardId") long boardId, @RequestHeader ("Authentication") String accountId) {
        return boardService.readBoard(boardId, userService.findUserByAccountId(accountId));
    }

    @PatchMapping("{boardId}")
    public void updateBoard(@PathVariable("boardId") long boardId, @RequestBody BoardDTO boardDTO, @RequestHeader("Authentication") String accountId) {
        authService.checkBoardAuthenticate(accountId, boardId);
        boardService.updateBoard(boardId, boardDTO);
    }

    @DeleteMapping("{boardId}")
    public void deleteBoard(@PathVariable("boardId") long boardId, @RequestHeader("Authentication") String accountId) {
        authService.checkBoardAuthenticate(accountId, boardId);
        boardService.deleteBoard(boardId);
    }

    @PostMapping("{boardId}/recommend")
    public void setRecommend(@PathVariable("boardId") long boardId, @RequestHeader("Authentication") String accountId) {
        recommendService.setRecommend(boardService.getBoardById(boardId), userService.findUserByAccountId(accountId));
    }

    @DeleteMapping("{boardId}/recommend")
    public void setDisRecommend(@PathVariable("boardId") long boardId, @RequestHeader("Authentication") String accountId) {
        recommendService.setDisRecommend(boardService.getBoardById(boardId), userService.findUserByAccountId(accountId));
    }
}