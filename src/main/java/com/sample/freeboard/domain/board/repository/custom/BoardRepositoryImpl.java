package com.sample.freeboard.domain.board.repository.custom;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sample.freeboard.domain.account.domain.QAccountType;
import com.sample.freeboard.domain.board.domain.QBoard;
import com.sample.freeboard.domain.user.domain.QUser;
import com.sample.freeboard.global.dto.filter.Pagination;
import com.sample.freeboard.domain.board.domain.Board;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Board> findReadableBoardList(Pagination pagination) {
        return queryFactory
                .selectFrom(QBoard.board)
                .join(QBoard.board.user, QUser.user)
                .fetchJoin()
                .join(QUser.user.accountType, QAccountType.accountType)
                .fetchJoin()
                .where(notDeleted())
                .orderBy(descByCreatedDt())
                .offset(calculateOffset(pagination.getPage(), pagination.getPageSize()))
                .limit(pagination.getPageSize())
                .fetch();
    }

    @Override
    public Board findByBoardId(long boardId) {
        return queryFactory
                .selectFrom(QBoard.board)
                .join(QBoard.board.user, QUser.user)
                .fetchJoin()
                .where(equalBoardId(boardId))
                .fetchOne();
    }

    @Override
    public long countReadableBoardList() {
        return queryFactory
                .select(QBoard.board.count())
                .from(QBoard.board)
                .join(QBoard.board.user, QUser.user)
                .join(QUser.user.accountType, QAccountType.accountType)
                .where(notDeleted())
                .orderBy(descByCreatedDt())
                .fetchOne();
    }

    private int calculateOffset(int page, int pageSize) {
        return (page - 1) * pageSize;
    }

    private BooleanExpression containsContent(String content) {
        return StringUtils.hasText(content) ? QBoard.board.content.contains(content) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return StringUtils.hasText(title) ? QBoard.board.title.contains(title) : null;
    }

    private OrderSpecifier<LocalDateTime> descByCreatedDt() {
        return QBoard.board.createdDt.desc();
    }

    private BooleanExpression equalBoardId(long boardId) {
        return QBoard.board.boardId.eq(boardId);
    }

    private BooleanExpression notDeleted() {
        return QBoard.board.isDeleted.isFalse();
    }
}
