package de.dontletyoudie.backend.persistence.judgement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    List<Judgement> findJudgementsByJudgeName(String judgeName);
}
