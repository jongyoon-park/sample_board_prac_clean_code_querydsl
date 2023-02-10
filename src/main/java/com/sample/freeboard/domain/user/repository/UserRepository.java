package com.sample.freeboard.domain.user.repository;

import com.sample.freeboard.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccountId(String accountId);
    User findByNickname(String nickname);
}
