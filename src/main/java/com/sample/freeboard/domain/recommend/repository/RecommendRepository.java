package com.sample.freeboard.domain.recommend.repository;

import com.sample.freeboard.domain.recommend.repository.custom.RecommendRepositoryCustom;
import com.sample.freeboard.domain.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, RecommendRepositoryCustom {
}
