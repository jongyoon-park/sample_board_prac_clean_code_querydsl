package com.sample.freeboard.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class ListResponse<CollectionType> {
    private CollectionType CollectionType;

    public ListResponse(CollectionType CollectionType) {
        this.CollectionType = CollectionType;
    }
}
