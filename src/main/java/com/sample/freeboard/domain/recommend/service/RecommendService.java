package com.sample.freeboard.domain.recommend.service;

import com.sample.freeboard.domain.user.domain.User;
import com.sample.freeboard.global.constant.Message;
import com.sample.freeboard.global.dto.response.ListResponse;
import com.sample.freeboard.global.errorhandler.exception.NotFoundException;
import com.sample.freeboard.domain.board.domain.Board;
import com.sample.freeboard.domain.recommend.domain.Recommend;
import com.sample.freeboard.domain.recommend.dto.RecommendResponse;
import com.sample.freeboard.domain.recommend.dto.Recommends;
import com.sample.freeboard.domain.recommend.repository.RecommendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;

    public ListResponse<Recommends> getRecommendHistory(long userId) {
        return new ListResponse<>(getRecommends(findRecommendList(userId)));
    }

    public void setRecommend(Board board, User writer) {
        Recommend findRecommend = findRecommend(board.getBoardId(), writer.getUserId());

        if (Objects.isNull(findRecommend)) {
            recommendRepository.save(new Recommend(board, writer));
            return;
        }

        findRecommend.againRecommend();
        recommendRepository.save(findRecommend);
    }

    public void setDisRecommend(Board board, User writer) {
        Recommend findRecommend = findRecommend(board.getBoardId(), writer.getUserId());

        if (Objects.isNull(findRecommend)) {
            throw new NotFoundException(Message.NOT_EXIST_RECOMMEND);
        }

        findRecommend.disRecommend();
        recommendRepository.save(findRecommend);
    }

    private Recommend findRecommend(long boardId, long userId) {
        return recommendRepository.findByUserIdAndBoardId(boardId, userId);
    }

    private List<Recommend> findRecommendList(long userId) {
        return recommendRepository.findByUserId(userId);
    }

    private Recommends getRecommends(List<Recommend> recommendList) {
        ArrayList<RecommendResponse> recommendResponses = new ArrayList<>();
        recommendList.forEach(recommend -> recommendResponses.add(new RecommendResponse(recommend)));
        return new Recommends(recommendResponses);
    }
}
