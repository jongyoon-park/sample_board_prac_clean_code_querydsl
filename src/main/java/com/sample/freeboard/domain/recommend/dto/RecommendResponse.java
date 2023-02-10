package com.sample.freeboard.domain.recommend.dto;

import com.sample.freeboard.domain.board.domain.Board;
import com.sample.freeboard.domain.board.dto.BoardDTO;
import com.sample.freeboard.domain.recommend.domain.Recommend;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecommendResponse {
    private Long recommendId;
    private LocalDateTime modifiedDt;
    private BoardDTO board;
    private Long userId;

    public RecommendResponse(Recommend recommend) {
        this.recommendId = recommend.getRecommendId();
        this.modifiedDt = recommend.getModifiedDt();
        Board boardSource = recommend.getBoard();
        this.board = new BoardDTO(boardSource.getTitle(), boardSource.getContent(), boardSource.getBoardId());
        this.userId = recommend.getUser().getUserId();
    }
}
