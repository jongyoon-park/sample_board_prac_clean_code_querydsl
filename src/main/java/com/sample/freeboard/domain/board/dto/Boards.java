package com.sample.freeboard.domain.board.dto;

import com.sample.freeboard.global.collection.FirstClassCollection;
import lombok.Getter;

import java.util.List;

@Getter
public class Boards implements FirstClassCollection {
    private List<BoardResponse> boardList;

    public Boards(List<BoardResponse> boardList) {
        this.boardList = boardList;
    }

    @Override
    public int getCount() {
        return this.boardList.size();
    }
}
