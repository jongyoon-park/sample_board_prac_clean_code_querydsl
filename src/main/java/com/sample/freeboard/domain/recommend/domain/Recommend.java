package com.sample.freeboard.domain.recommend.domain;

import com.sample.freeboard.domain.board.domain.Board;
import com.sample.freeboard.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "recommend")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Recommend {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recommend_id")
    private Long recommendId;
    @Column(name = "recommend_value")
    private Boolean recommendValue;
    @Column(name = "createdDt")
    private LocalDateTime createdDt;
    @Column(name = "modified_dt")
    private LocalDateTime modifiedDt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recommend(Board board, User user) {
        this.recommendValue = true;
        this.board = board;
        this.user = user;
        this.createdDt = LocalDateTime.now();
        this.modifiedDt = LocalDateTime.now();

        if (Objects.nonNull(board)) {
            setBoard(board);
        }

        if (Objects.nonNull(user)) {
            setUser(user);
        }
    }

    public void disRecommend() {
        this.recommendValue = false;
        updateModifiedDt();
    }

    public void againRecommend() {
        this.recommendValue = true;
        updateModifiedDt();
    }

    private void setBoard(Board board) {
        this.board = board;
        board.getRecommendList().add(this);
    }

    private void setUser(User user) {
        this.user = user;
        user.getRecommendList().add(this);
    }

    private void updateModifiedDt() {
        this.modifiedDt = LocalDateTime.now();
    }
}
