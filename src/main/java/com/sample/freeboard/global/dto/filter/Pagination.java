package com.sample.freeboard.global.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    private int pageSize;
}
