package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.persistence.proof.Proof;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.exceptions.ProofNotFoundException;
import de.dontletyoudie.backend.persistence.stat.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("judgementService")
@RequiredArgsConstructor
public class JudgementService {
    private final JudgementRepository judgementRepository;
    private final AccountService accountService;
    private final ProofService proofService;
    private final StatService statService;

    public Judgement saveJudgement(JudgementDto judgementDto) throws AccountNotFoundException, ProofNotFoundException {
        Proof relatedProof = proofService.getProof(judgementDto.getProofId());

        Judgement returnJudgement =  judgementRepository.save(new Judgement(
                accountService.getAccount(judgementDto.getJudge()),
                relatedProof,
                judgementDto.getApproved(),
                judgementDto.getDate()));

        List<Judgement> judgements = judgementRepository.getAllByProofId(returnJudgement.getProof().getId());

        // Check if 3 proofs have been uploaded yet
        switch (getFinalProofStatus(judgements)) {
            case APPROVED:
                statService.increaseStats(returnJudgement.getProof());
                //TODO Fix dummy data import then reintroduce cleanup
                //cleanupProofsJudgements(judgements, returnJudgement.getProof());
            case DENIED:
                //TODO Fix dummy data import then reintroduce cleanup
                //cleanupProofsJudgements(judgements, returnJudgement.getProof());
        }

        return returnJudgement;

    }
  
    private void cleanupProofsJudgements(List<Judgement> judgements, Proof proof) {
        judgementRepository.deleteAll(judgements);
        proofService.deleteProof(proof);
    }

    private FinalProofStatus getFinalProofStatus(List<Judgement> judgements) {
        int score = 0;

        if (judgements.size() == 3) {
            for (Judgement judgement : judgements) {
                score += judgement.getApproved() ? 1 : 0;
            }

            if (score >= 2) {
                return FinalProofStatus.APPROVED;
            } else {
                return FinalProofStatus.DENIED;
            }
        }

        return FinalProofStatus.TBD;
    }

    private enum FinalProofStatus {
        APPROVED, DENIED, TBD
    }

}
