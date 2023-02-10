package com.sample.freeboard.domain.board.repository.custom;

import com.sample.freeboard.global.dto.filter.Pagination;
import com.sample.freeboard.domain.board.domain.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findReadableBoardList(Pagination pagination);
    Board findByBoardId(long boardId);

    long countReadableBoardList();
}
