package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.judgement.Judgement;
import de.dontletyoudie.backend.persistence.judgement.JudgementService;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/judgement")
public class JudgementController {

    private final JudgementService judgementService;
    @Autowired
    public JudgementController(JudgementService judgementService) {
        this.judgementService = judgementService;
    }

    @PostMapping("/add")
    public ResponseEntity<JudgementDto> addJudgement(@RequestBody JudgementDto judgementDto) {

        Judgement judgement = judgementService.saveJudgement(judgementDto);

        return new ResponseEntity<>(new JudgementDto(judgement.getJudgeName(),
                                                    judgement.getProofId(),
                                                    judgement.getApproved()),
                                                    HttpStatus.CREATED);
    }
}
