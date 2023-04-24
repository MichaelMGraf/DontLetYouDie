package de.dontletyoudie.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ResponseEntity<JudgementDto> addJudgement(@RequestBody JudgementDto judgementDto) {

        Judgement judgement = judgementService.saveJudgement(judgementDto);

        return new ResponseEntity<>(new JudgementDto(judgement.getJudgeName(),
                                                    judgement.getProofId(),
                                                    judgement.getApproved()),
                                                    HttpStatus.CREATED);
    }

    @PathFilter(path = "/api/judgement/add", tokenRequired = true)
    public static PathFilterResult filterAdd(FilterData data) throws IOException {
        JudgementDto dto = new ObjectMapper().readValue(data.getRequest().getInputStream(), JudgementDto.class);
        return data.getToken().getSubject().equals(dto.getJudgeName())
                ? PathFilterResult.getNotDenied() : PathFilterResult.getAccessDenied("JudgeName does not match token");
    }
}
