package com.sample.freeboard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    protected String title;
    protected String content;
    protected Long boardId;
}
