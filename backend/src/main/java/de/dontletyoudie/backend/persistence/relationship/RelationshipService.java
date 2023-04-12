package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("relationshipService")
@RequiredArgsConstructor
public class RelationshipService {

    private final AccountService accountService;
    private final RelationshipRepository relationshipRepository;

    public Relationship save(RelationshipAddDto relationshipAddDto) throws AccountNotFoundException {
        Account srcAccount = accountService.getAccount(relationshipAddDto.getSrcUsername());
        Account relAccount = accountService.getAccount(relationshipAddDto.getRelUsername());

        Relationship returnRelationship = new Relationship();

        if (relationshipRepository.findRelationshipBySrcAccountAndRelAccount(srcAccount, relAccount).isPresent()) {
            //TODO Behavior is a bit weird atm, but afaik we want to remodel adding friends anyways to have a proper search function, then this case will never happen
            return returnRelationship;
        } else {
            return relationshipRepository.save(new Relationship(
                    srcAccount,
                    relAccount,
                    RelationshipStatus.PENDING
            ));
        }
    }

    public Optional<Relationship> getRelationship(String srcAccount, String relAccount) throws AccountNotFoundException {
        return relationshipRepository.findRelationshipBySrcAccountAndRelAccount(accountService.getAccount(srcAccount),
                accountService.getAccount(relAccount));
    }

    public RelationshipDto accept(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        Optional<Relationship> relationshipO = getRelationship(srcAccount, relAccount);
        if (relationshipO.isEmpty()) throw new RelationshipNotFoundException(srcAccount, relAccount);

        Relationship relationship = relationshipO.get();
        if (relationship.getRelationshipStatus() != RelationshipStatus.PENDING)
            throw new RelationshipStatusException(srcAccount, relAccount, relationship.getRelationshipStatus(), RelationshipStatus.PENDING);

        relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);

        return new RelationshipDto(relationship);
    }
}
