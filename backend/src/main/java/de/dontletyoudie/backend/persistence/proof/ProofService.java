package de.dontletyoudie.backend.persistence.proof;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountRepository;
import de.dontletyoudie.backend.persistence.judgement.Judgement;
import de.dontletyoudie.backend.persistence.judgement.JudgementRepository;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import de.dontletyoudie.backend.persistence.relationship.Relationship;
import de.dontletyoudie.backend.persistence.relationship.RelationshipRepository;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("proofService")
@RequiredArgsConstructor
public class ProofService {

    private final ProofRepository proofRepository;
    private final RelationshipRepository relationshipRepository;
    private final AccountRepository accountRepository;
    private final JudgementRepository judgementRepository;


    public Optional<ProofReturnDto> getPendingProofs(String username) {


        //Get Account
        Optional<Account> userAccount = accountRepository.findAccountByUsername(username);

        // Find Relationships
        Optional<List<Relationship>> relationships = relationshipRepository.findRelationshipsByRelAccountOrSrcAccount(userAccount.get(), userAccount.get());
        List<Account> friends = new ArrayList<>();

        if (relationships.isEmpty()) {
            return Optional.empty();
        } else {
            for (Relationship relationship : relationships.get()) {
                if (relationship.getRelationshipStatus() == RelationshipStatus.FRIEND) {

                    String srcUserName = relationship.getSrcAccount().getUsername();
                    Account friend = srcUserName.equals(username) ? relationship.getRelAccount() : relationship.getSrcAccount();

                    friends.add(friend);
                }
            }
        }

        // Gather Proofs the user has judged
        List<Judgement> judgements = judgementRepository.findJudgementsByJudgeName(username);
        List<Long> judgedProofIds = new ArrayList<>();

        for (Judgement judgement : judgements) {
            judgedProofIds.add(judgement.getProofId());
        }


        for (Account friend : friends) {
            List<Proof> proofs = proofRepository.findProofsByUsername(friend.getUsername());
            for (Proof proof : proofs) {
                if (!judgedProofIds.contains(proof.getId())) {
                    return Optional.of(new ProofReturnDto(
                            proof.getUsername(),
                            proof.getImage(),
                            proof.getDateTime(),
                            proof.getCategory(),
                            proof.getComment(),
                            proof.getAvgScore(),
                            proof.getJudgements()));
                }
            }
        }


        return Optional.empty();
    }

    public void saveProof(ProofAddDto proofAddDto) {
        proofRepository.save(new Proof(
                proofAddDto.getUsername(),
                proofAddDto.getCategory(),
                proofAddDto.getImage(),
                proofAddDto.getComment(),
                proofAddDto.getCreationDate(),
                0,
                0
        ));
    }
}
