package com.smartwatch.leaderboard.repository;

import com.smartwatch.leaderboard.model.ChallengeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, Long> {
    Page<ChallengeUser> findByChallengeId(Long challengeId, Pageable pageable);
    List<ChallengeUser> findByChallengeId(Long challengeId);
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
}
