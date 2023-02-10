package com.sample.freeboard.domain.account.service;

import com.sample.freeboard.domain.user.domain.User;
import com.sample.freeboard.global.errorhandler.exception.ForbiddenException;
import com.sample.freeboard.global.errorhandler.exception.UnauthorizedException;
import com.sample.freeboard.domain.board.repository.BoardRepository;
import com.sample.freeboard.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.sample.freeboard.global.constant.Message.*;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void authenticate(String authentication) {
        if (!StringUtils.hasText(authentication)) {
            throw new UnauthorizedException(EMPTY_ACCOUNT_ID_MESSAGE);
        }

        if (Objects.isNull(userRepository.findByAccountId(authentication))) {
            throw new UnauthorizedException(NOT_FOUND_USER_MESSAGE);
        }
    }

    public void checkBoardAuthenticate(String accountId, long boardId) {
        User findUserByBoardId = boardRepository.getById(boardId).getUser();
        hasAuthenticate(findUserByBoardId.getAccountId(), accountId);
    }

    private void hasAuthenticate(String accountId, String compareId) {
        if (accountId.equals(compareId)) {
            return;
        }

        throw new ForbiddenException(NOT_AUTHENTICATION_USER);
    }
}