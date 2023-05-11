package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("judgementService")
@RequiredArgsConstructor
public class JudgementService {
    private JudgementRepository judgementRepository;
    private AccountService accountService;

    @Autowired
    public JudgementService(JudgementRepository judgementRepository, AccountService accountService) {
        this.accountService = accountService;
        this.judgementRepository = judgementRepository;
    }


    public Judgement saveJudgement(JudgementDto judgementDto) throws AccountNotFoundException {
        return judgementRepository.save(new Judgement(
                accountService.getAccount(judgementDto.getJudgeName()),
                judgementDto.getProofId(),
                judgementDto.getApproved()));
    }
}
