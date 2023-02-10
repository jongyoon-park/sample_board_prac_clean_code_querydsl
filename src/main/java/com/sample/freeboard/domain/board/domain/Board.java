package com.sample.freeboard.domain.board.domain;

import com.sample.freeboard.domain.board.dto.BoardDTO;
import com.sample.freeboard.domain.user.domain.User;
import com.sample.freeboard.domain.recommend.domain.Recommend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "board")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_id")
    private Long boardId;
    private String title;
    private String content;
    @Column(name = "created_dt")
    private LocalDateTime createdDt;
    @Column(name = "modified_dt")
    private LocalDateTime modifiedDt;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<Recommend> recommendList = new ArrayList<>();

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.createdDt = LocalDateTime.now();
        this.isDeleted = false;
        updateModifiedDt();

        if (Objects.nonNull(user)) {
            setUser(user);
        }
    }

    public void setUser(User user) {
        this.user = user;
        user.getBoardList().add(this);
    }

    public void updateBoard(BoardDTO boardDTO) {
        if (Objects.nonNull(boardDTO.getTitle())) {
            this.title = boardDTO.getTitle();
        }
        if (Objects.nonNull(boardDTO.getContent())) {
            this.content = getContent();
        }
        updateModifiedDt();
    }

    public void deleteBoard() {
        this.isDeleted = true;
        updateModifiedDt();
    }

    private void updateModifiedDt() {
        this.modifiedDt = LocalDateTime.now();
    }
}
