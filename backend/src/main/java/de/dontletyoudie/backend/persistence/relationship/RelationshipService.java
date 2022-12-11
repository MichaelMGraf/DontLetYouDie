package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountRepository;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("relationshipService")
@RequiredArgsConstructor
public class RelationshipService {

    private final AccountRepository accountRepository;
    private final RelationshipRepository relationshipRepository;

    public Relationship save(RelationshipAddDto relationshipAddDto) {
        Optional<Account> srcAccount = accountRepository.findAccountByUsername(relationshipAddDto.getSrcUsername());
        Optional<Account> relAccount = accountRepository.findAccountByUsername(relationshipAddDto.getRelUsername());

        if (srcAccount.isEmpty() || relAccount.isEmpty()) {
            throw new UsernameNotFoundException("Couldn't find srcAccount or relAccount");
        }

        Relationship returnRelationship = new Relationship();

        if (relationshipRepository.findRelationshipBySrcAccountAndRelAccount(srcAccount.get(), relAccount.get()).isPresent()) {
            //TODO Behavior is a bit weird atm, but afaik we want to remodel adding friends anyways to have a proper search function, then this case will never happen
            return returnRelationship;
        } else {
            return relationshipRepository.save(new Relationship(
                    srcAccount.get(),
                    relAccount.get(),
                    RelationshipStatus.PENDING
            ));
        }
    }
}
