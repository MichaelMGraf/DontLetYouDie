package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    List<Judgement> findByJudge(Account judge);
    List<Judgement> getAllByProofId(Long id);
}
