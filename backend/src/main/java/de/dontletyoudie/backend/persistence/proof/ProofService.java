package de.dontletyoudie.backend.persistence.proof;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.category.Category;
import de.dontletyoudie.backend.persistence.category.CategoryRepository;
import de.dontletyoudie.backend.persistence.category.exceptions.CategoryNotFoundException;
import de.dontletyoudie.backend.persistence.judgement.JudgementRepository;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import de.dontletyoudie.backend.persistence.proof.exceptions.ProofNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("proofService")
@RequiredArgsConstructor
public class ProofService {

    private final ProofRepository proofRepository;
    private final RelationshipService relationshipService;
    private final AccountService accountService;

    @PostConstruct
    public void init() {
        accountService.setProofService(this);
    }
    private final JudgementRepository judgementRepository;
    private final CategoryRepository categoryRepository;

    public Optional<ProofReturnDto> getPendingProof(String username) throws AccountNotFoundException {

        List<Proof> allPendingProofs = getAllPendingProofs(accountService.getAccount(username));
        if (allPendingProofs.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new ProofReturnDto(allPendingProofs.get(0)));
    }

    public void deleteProof(Proof proof) {
        proofRepository.delete(proof);
    }

    public List<Proof> getAllPendingProofs(Account account) {
        List<Account> friends = relationshipService.getFriendsAccount(account);

        List<Long> judgedProofIds = getJudgedProofIds(account);

        return friends.stream()
                .flatMap(a -> proofRepository.findProofsByAccount(a).stream())
                .filter(p -> !judgedProofIds.contains(p.getId()))
                .toList();
    }

    private List<Long> getJudgedProofIds(Account account) {
        return judgementRepository.findByJudge(account)
                .stream()
                .map(j -> j.getProof().getId())
                .toList();
    }

    public List<Proof> findProofsCreatedByUser(Account account) {
        return proofRepository.findProofsByAccount(account);
    }

    public void saveProof(ProofAddDto proofAddDto) throws AccountNotFoundException, CategoryNotFoundException {
        Account account = accountService.getAccount(proofAddDto.getUsername());
        Category category = categoryRepository.findCategoryByName(proofAddDto.getCategory()).orElseThrow(
                () -> new CategoryNotFoundException(proofAddDto.getCategory()));

        proofRepository.save(new Proof(
                account,
                category,
                proofAddDto.getImage(),
                proofAddDto.getComment(),
                proofAddDto.getCreationDate(),
                false
        ));
    }

    public Proof getProof(long id) throws ProofNotFoundException {
        Optional<Proof> proof = proofRepository.findById(id);
        if (proof.isEmpty()) throw new ProofNotFoundException(id);
        return proof.get();
    }
}
