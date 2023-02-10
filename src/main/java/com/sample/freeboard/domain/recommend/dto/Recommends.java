package com.sample.freeboard.domain.recommend.dto;

import com.sample.freeboard.global.collection.FirstClassCollection;
import lombok.Getter;

import java.util.List;

@Getter
public class Recommends implements FirstClassCollection {

    private List<RecommendResponse> recommendList;

    public Recommends(List<RecommendResponse> recommendList) {
        this.recommendList = recommendList;
    }

    @Override
    public int getCount() {
        return this.recommendList.size();
    }
}
