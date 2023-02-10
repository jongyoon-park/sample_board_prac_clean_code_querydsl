package com.sample.freeboard.domain.board.repository;

import com.sample.freeboard.domain.board.domain.Board;
import com.sample.freeboard.domain.board.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> , BoardRepositoryCustom {
}
