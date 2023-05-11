package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.Judgement;
import de.dontletyoudie.backend.persistence.judgement.JudgementService;
import de.dontletyoudie.backend.persistence.judgement.dtos.JudgementDto;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Filter
@RestController
@RequestMapping("/api/judgement")
public class JudgementController {

    private final JudgementService judgementService;
    @Autowired
    public JudgementController(JudgementService judgementService) {
        this.judgementService = judgementService;
    }

    @PostMapping("/add")
    public ResponseEntity<JudgementDto> addJudgement(@RequestParam(name = "judgeName") String judgeName, @RequestParam Long proofId, @RequestParam Boolean approved) {

        System.out.println("ANFANG");
        JudgementDto judgementDto = new JudgementDto(
                judgeName,
                proofId,
                approved);

        System.out.println(proofId);

        Judgement judgement = null;
        try {
            judgement = judgementService.saveJudgement(judgementDto);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(new JudgementDto(judgement.getJudge().getUsername(),
                                                    judgement.getProofId(),
                                                    judgement.getApproved()),
                                                    HttpStatus.CREATED);
    }

    @PathFilter(path = "/api/judgement/add", tokenRequired = true)
    public static PathFilterResult filterAdd(FilterData data) throws IOException {
        return data.getToken().getSubject().equals(data.getRequest().getParameter("judgeName"))
                ? PathFilterResult.getNotDenied() : PathFilterResult.getAccessDenied("JudgeName does not match token");
    }
}
