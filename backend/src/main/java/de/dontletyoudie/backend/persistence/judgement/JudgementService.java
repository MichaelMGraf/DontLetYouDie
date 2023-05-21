package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.exceptions.ProofNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("judgementService")
@RequiredArgsConstructor
public class JudgementService {
    private final JudgementRepository judgementRepository;
    private final AccountService accountService;
    private final ProofService proofService;


    public Judgement saveJudgement(JudgementDto judgementDto) throws AccountNotFoundException, ProofNotFoundException {
        return judgementRepository.save(new Judgement(
                accountService.getAccount(judgementDto.getJudge()),
                proofService.getProof(judgementDto.getProofId()),
                judgementDto.getApproved(),
                judgementDto.getDate()));
    }
}
