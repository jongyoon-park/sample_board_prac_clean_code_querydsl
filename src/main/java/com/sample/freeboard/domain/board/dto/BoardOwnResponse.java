package com.sample.freeboard.domain.board.dto;

import com.sample.freeboard.domain.board.domain.Board;
import lombok.Getter;

@Getter
public class BoardOwnResponse extends BoardResponse {
    private Boolean isRecommend;
    public BoardOwnResponse(String title, String content, Long boardId) {
        super(title, content, boardId);
    }

    public BoardOwnResponse(Board board) {
        super(board);
    }
    public BoardOwnResponse(Board board, Boolean isRecommend) {
        super(board);
        this.isRecommend = isRecommend;
    }
}
