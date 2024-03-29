package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.Judgement;
import de.dontletyoudie.backend.persistence.judgement.JudgementService;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.persistence.proof.exceptions.ProofNotFoundException;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Filter
@RestController
@RequestMapping("/api/judgement")
@RequiredArgsConstructor
public class JudgementController {

    private final JudgementService judgementService;

    @PostMapping("/add")
    public ResponseEntity<JudgementDto> addJudgement(@RequestParam(name = "judgeName") String judgeName,
                                                     @RequestParam Long proofId,
                                                     @RequestParam Boolean approved) {

        JudgementDto judgementDto = new JudgementDto(
                judgeName,
                proofId,
                approved,
                LocalDateTime.now());

        Judgement judgement;
        try {
            judgement = judgementService.saveJudgement(judgementDto);
        } catch (AccountNotFoundException | ProofNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(new JudgementDto(judgement.getJudge().getUsername(),
                                                    judgement.getProof().getId(),
                                                    judgement.getApproved(),
                                                    judgement.getDate()),
                                                    HttpStatus.CREATED);
    }

    @PathFilter(path = "/api/judgement/add", tokenRequired = true)
    public static PathFilterResult filterAdd(FilterData data) {
        return data.getToken().getSubject().equals(data.getRequest().getParameter("judgeName"))
                ? PathFilterResult.getNotDenied() : PathFilterResult.getAccessDenied("JudgeName does not match token");
    }
}
