package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("judgementService")
@RequiredArgsConstructor
public class JudgementService {
    private JudgementRepository judgementRepository;

    @Autowired
    public JudgementService(JudgementRepository judgementRepository) {
        this.judgementRepository = judgementRepository;
    }

    public Judgement saveJudgement(JudgementDto judgementDto) {
        return judgementRepository.save(new Judgement(
                judgementDto.getJudgeName(),
                judgementDto.getProofId(),
                judgementDto.getApproved()));
    }
}
