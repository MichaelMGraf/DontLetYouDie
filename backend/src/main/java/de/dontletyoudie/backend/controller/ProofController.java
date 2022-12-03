package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountShowDto;
import de.dontletyoudie.backend.persistence.proof.Proof;
import de.dontletyoudie.backend.persistence.proof.ProofService;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDtoWithoutPicture;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(path = "/api/proof")
public class ProofController {

    private final ProofService proofService;

    @Autowired
    public ProofController(ProofService proofService) {
        this.proofService = proofService;
    }


    /**
     *
     * @param username Username of the account that is being queried for
     * @return ArrayList<Proof> Instance of the pending Proofs if any exist, else just 204 no content
     */
    @GetMapping(path = "/getPending/{username}")
    public ResponseEntity<ArrayList<ProofReturnDto>> getPendingProofs(@PathVariable(value="username") String username) {

        Optional<ArrayList<ProofReturnDto>> proofs = proofService.getPendingProofs(username);

        if (proofs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(proofs.get(), HttpStatus.OK);
        }

    }


    @PostMapping(path = "/add")
    public ResponseEntity<String> uploadProof(@RequestParam String username,
                                             @RequestParam String zonedDateTime,
                                             @RequestParam String comment,
                                             @RequestParam String category,
                                             @RequestBody MultipartFile multipartFile)
                                                throws IOException {
        ZonedDateTime zonedDateTimeMockup = ZonedDateTime.now();

        ProofAddDto proofAddDto = new ProofAddDto(
                username,
                multipartFile.getBytes(),
                //proofAddDtoWithoutPicture.getCreationDate(),
                zonedDateTimeMockup,
                category,
                comment);

        proofService.saveProof(proofAddDto);

        return new ResponseEntity<>("sagnix", HttpStatus.OK);
    }
}
