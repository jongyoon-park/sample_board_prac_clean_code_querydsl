package com.sample.freeboard.domain.recommend.repository.custom;

import com.sample.freeboard.domain.recommend.domain.Recommend;

import java.util.List;

public interface RecommendRepositoryCustom {
    List<Recommend> findByUserId(long userId);
    Recommend findByUserIdAndBoardId(long boardId, long userId);
}
