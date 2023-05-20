package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.category.exceptions.CategoryNotFoundException;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(path = "/api/proof")
@RequiredArgsConstructor
public class ProofController {
    private final ProofService proofService;


    /**
     *
     * @param username Username of the account that is being queried for
     * @return ProofReturnDto Instance of the pending Proofs if any exist, else just 204 no content
     */

    @GetMapping(path = "/getPending")
    public ResponseEntity<ProofReturnDto> getPendingProofs(@RequestParam(value="username") String username) {


        Optional<ProofReturnDto> proof;
        try {
            proof = proofService.getPendingProofs(username);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (proof.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(proof.get(), HttpStatus.OK);
        }

    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> uploadProof(@RequestParam String username,
                                             @RequestParam String comment,
                                             @RequestParam String category,
                                             @RequestBody MultipartFile uploaded_file)
                                                throws IOException {

        ProofAddDto proofAddDto = new ProofAddDto(
                username,
                uploaded_file.getBytes(),
                LocalDateTime.now(),
                category,
                comment);

        try {
            proofService.saveProof(proofAddDto);
        } catch (AccountNotFoundException | CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>("Successfully added proof", HttpStatus.CREATED);
    }

    @PathFilter(path = "/api/proof/add", tokenRequired = true)
    public static PathFilterResult filterAdd(FilterData data) {
        return data.getToken().getSubject().equals(data.getRequest().getParameter("username"))
                ? PathFilterResult.getNotDenied() : PathFilterResult.getAccessDenied("JudgeName does not match token");
    }
}
