package com.sample.freeboard.domain.board.dto;

import com.sample.freeboard.domain.user.dto.UserDTO;
import com.sample.freeboard.domain.board.domain.Board;
import com.sample.freeboard.domain.recommend.domain.Recommend;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse extends BoardDTO{
    protected LocalDateTime createdDt;
    protected LocalDateTime modifiedDt;
    protected Long recommendCount;
    protected UserDTO writer;

    public BoardResponse(String title, String content, Long boardId) {
        super(title, content, boardId);
    }

    public BoardResponse(Board board) {
        super(board.getTitle(), board.getContent(), board.getBoardId());
        this.createdDt = board.getCreatedDt();
        this.modifiedDt = board.getModifiedDt();
        this.recommendCount = sortRecommendCount(board.getRecommendList());
        this.writer = new UserDTO(board.getUser().getUserId(), board.getUser().convertKorean());
    }

    private long sortRecommendCount(List<Recommend> recommendList) {
        return recommendList.stream()
                .filter(Recommend::getRecommendValue)
                .count();
    }
}
