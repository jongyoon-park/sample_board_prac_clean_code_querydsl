package com.sample.freeboard.domain.user.domain;

import com.sample.freeboard.domain.account.domain.AccountType;
import com.sample.freeboard.domain.board.domain.Board;
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

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String nickname;
    @Column(name = "account_id")
    private String accountId;
    private Boolean quit;
    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_id")
    private AccountType accountType;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany
    private List<Recommend> recommendList = new ArrayList<>();

    public User(String nickname, String accountId, AccountType accountType) {
        this.nickname = nickname;
        this.accountId = accountId;
        this.quit = false;
        this.createdDt = LocalDateTime.now();

        if (Objects.nonNull(accountType)) {
            changeAccountType(accountType);
        }
    }

    public String convertKorean() {
        return nickname + "(" + accountType.convertTypeByKorean() + ")";
    }

    public void changeAccountType(AccountType accountType) {
        this.accountType = accountType;
        accountType.getUserList().add(this);
    }
}