package com.sample.freeboard.domain.board.service;

import com.sample.freeboard.domain.board.dto.BoardDTO;
import com.sample.freeboard.domain.board.dto.BoardResponse;
import com.sample.freeboard.domain.board.dto.Boards;
import com.sample.freeboard.domain.board.repository.BoardRepository;
import com.sample.freeboard.domain.user.domain.User;
import com.sample.freeboard.domain.user.service.UserService;
import com.sample.freeboard.global.constant.Message;
import com.sample.freeboard.global.dto.filter.Pagination;
import com.sample.freeboard.global.dto.response.ListResponse;
import com.sample.freeboard.global.errorhandler.exception.BadRequestException;
import com.sample.freeboard.global.errorhandler.exception.NotFoundException;
import com.sample.freeboard.domain.board.dto.BoardOwnResponse;
import com.sample.freeboard.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserService userService;

    private final int MAX_TITLE_LENGTH = 60;
    private final int MAX_CONTENT_LENGTH = 3000;

    public ListResponse<Boards> getReadableBoards(User user, Pagination pagination) {
        isValidatePage(pagination.getPage(), pagination.getPageSize(), (int) countReadableBoardList());
        if (Objects.nonNull(user)) {
            return new ListResponse<>(getBoards(findReadableBoardList(pagination), user));
        }
        return new ListResponse<>(getBoards(findReadableBoardList(pagination)));
    }

    public void writeBoard(BoardDTO boardDTO, String accountId) {
        User writer = userService.findUserByAccountId(accountId);
        validateBoard(boardDTO.getTitle(), boardDTO.getContent());
        boardRepository.save(new Board(boardDTO.getTitle(), boardDTO.getContent(), writer));
    }

    public void updateBoard(long boardId, BoardDTO boardDTO) {
        Board findBoard = getBoardById(boardId);
        validateUpdate(boardDTO.getTitle(), boardDTO.getContent());
        findBoard.updateBoard(boardDTO);
        boardRepository.save(findBoard);
    }

    public void deleteBoard(long boardId) {
        Board findBoard = getBoardById(boardId);
        findBoard.deleteBoard();
        boardRepository.save(findBoard);
    }

    public BoardResponse readBoard(long boardId, User user) {
        Board findBoard = getBoardById(boardId);
        if (Objects.nonNull(user)) {
            return convertBoardResponse(findBoard, isRecommendedBoard(user, findBoard));
        }
        return convertBoardResponse(findBoard);
    }

    public Board getBoardById(long boardId) {
        Board findBoard = boardRepository.findByBoardId(boardId);

        if (Objects.isNull(findBoard)) {
            throw new NotFoundException(Message.NOT_EXIST_BOARD);
        }

        return findBoard;
    }

    private void validateBoard(String title, String board) {
        if (isValuableBoard(title, board)) {
            return;
        }

        throw new BadRequestException(Message.BAD_REQUEST_BOARD);
    }

    private void validateUpdate(String title, String board) {
        if (isValuableUpdate(title, board)) {
            return;
        }

        throw new BadRequestException(Message.BAD_REQUEST_BOARD);
    }

    private boolean isValuableBoard(String title, String content) {
        return isValuableTitle(title) && isValuableContent(content);
    }

    private boolean isValuableUpdate(String title, String content) {
        return isValuableTitle(title) ||  isValuableContent(content);
    }

    private boolean isValuableTitle(String title) {
        return StringUtils.hasText(title) && isAdmittedTitle(title);
    }

    private boolean isValuableContent(String content) {
        return StringUtils.hasText(content) && isAdmittedContent(content);
    }

    private boolean isAdmittedTitle(String title) {
        return title.length() < MAX_TITLE_LENGTH;
    }

    private boolean isAdmittedContent(String content) {
        return content.length() < MAX_CONTENT_LENGTH;
    }

    private List<Board> findReadableBoardList(Pagination pagination) {
        return boardRepository.findReadableBoardList(pagination);
    }

    private Boards getBoards(List<Board> boardList) {
        ArrayList<BoardResponse> boardResponses = new ArrayList<>();
        boardList.forEach(board -> boardResponses.add(convertBoardResponse(board)));

        return new Boards(boardResponses);
    }

    private Boards getBoards(List<Board> boardList, User user) {
        ArrayList<BoardResponse> boardResponses = new ArrayList<>();
        boardList.forEach(board ->
                        boardResponses.add(convertBoardResponse(board, isRecommendedBoard(user, board)))
        );
        return new Boards(boardResponses);
    }

    private boolean isRecommendedBoard(User user, Board board) {
        return board.getRecommendList().stream()
                .filter(recommend ->
                        (recommend.getUser().getUserId() == user.getUserId()) && recommend.getRecommendValue())
                .count() == 1;
    }

    private BoardResponse convertBoardResponse(Board board) {
        return new BoardResponse(board);
    }

    private BoardOwnResponse convertBoardResponse(Board board, boolean isRecommend) {
        return new BoardOwnResponse(board, isRecommend);
    }

    private void isValidatePage(int page, int pageSize, int totalCount) {
        if (isValuablePage(page, pageSize, totalCount)) {
            return;
        }

        throw new BadRequestException(Message.BAD_REQUEST_PAGE);
    }

    private boolean isValuablePage(int page, int pageSize, int totalCount) {
        return isPositivePage(page) && isPositivePage(pageSize) && page <= countTotalPage(pageSize, totalCount);
    }

    private boolean isPositivePage(int page) {
        return Objects.nonNull(page) && page > 0;
    }

    private int countTotalPage(int pageSize, int totalCount) {
        return ((totalCount - 1) / pageSize) + 1;
    }

    private long countReadableBoardList() {
        return boardRepository.countReadableBoardList();
    }


}
