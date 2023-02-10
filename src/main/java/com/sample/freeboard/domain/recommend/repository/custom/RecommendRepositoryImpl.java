package com.sample.freeboard.domain.recommend.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sample.freeboard.domain.board.domain.QBoard;
import com.sample.freeboard.domain.recommend.domain.QRecommend;
import com.sample.freeboard.domain.user.domain.QUser;
import com.sample.freeboard.domain.recommend.domain.Recommend;

import javax.persistence.EntityManager;
import java.util.List;

public class RecommendRepositoryImpl implements RecommendRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RecommendRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Recommend> findByUserId(long userId) {
        return queryFactory
                .selectFrom(QRecommend.recommend)
                .join(QRecommend.recommend.user, QUser.user)
                .fetchJoin()
                .join(QRecommend.recommend.board, QBoard.board)
                .fetchJoin()
                .where(equalUserId(userId), isRecommend())
                .fetch();
    }

    @Override
    public Recommend findByUserIdAndBoardId(long boardId, long userId) {
        return queryFactory
                .selectFrom(QRecommend.recommend)
                .join(QRecommend.recommend.user, QUser.user)
                .fetchJoin()
                .join(QRecommend.recommend.board, QBoard.board)
                .fetchJoin()
                .where(equalUserId(userId), equalBoardId(boardId))
                .fetchOne();
    }

    private BooleanExpression equalBoardId(long boardId) {
        return QRecommend.recommend.board.boardId.eq(boardId);
    }

    private BooleanExpression isRecommend() {
        return QRecommend.recommend.recommendValue.isTrue();
    }

    private BooleanExpression equalUserId(long userId) {
        return QRecommend.recommend.user.userId.eq(userId);
    }
}
