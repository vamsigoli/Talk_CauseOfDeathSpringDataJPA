package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository;

import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
