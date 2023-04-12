package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service("relationshipService")
@RequiredArgsConstructor
public class RelationshipService {

    private final AccountService accountService;
    private final RelationshipRepository relationshipRepository;

    public Relationship save(RelationshipAddDto relationshipAddDto) {
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

    public HashMap<HashMap<String, String>, RelationshipStatus> getPending(String username) {
        Account account = accountService.getAccount(username);
        System.out.println(account);
        // Find relationships either initiated by the account of received by the account
        Optional<List<Relationship>> relationshipsSource = relationshipRepository.findRelationshipsBySrcAccount(account);
        Optional<List<Relationship>> relationshipsRelated = relationshipRepository.findRelationshipsByRelAccount(account);


        // Save affected accounts of relationship and status in a Hashmap to be returned later
        HashMap<HashMap<String, String>, RelationshipStatus> returnedRelationships = new HashMap<>();

        if (relationshipsSource.isPresent() && relationshipsRelated.isPresent()) {
            relationshipsSource.get().addAll(relationshipsRelated.get());
            extractRelationshipInfo(relationshipsSource.get(), returnedRelationships, accountService);
        } else if (relationshipsSource.isPresent()) {
            relationshipsSource.ifPresent(relationships -> extractRelationshipInfo(relationships, returnedRelationships, accountService));
        } else if (relationshipsRelated.isPresent()) {
            relationshipsRelated.ifPresent(relationships -> extractRelationshipInfo(relationships, returnedRelationships, accountService));
        }

        return returnedRelationships;
    }

    private static void extractRelationshipInfo(List<Relationship> relationships,
                                     HashMap<HashMap<String, String>, RelationshipStatus> returnedRelationships, AccountService accountService) {
        for (Relationship relationship : relationships) {
            HashMap<String, String> accounts = new HashMap<>();
            System.out.println("Src: " + accountService.getAccountById(relationship.getSrcAccount().getId()).getUsername());
            System.out.println("Rel: " + accountService.getAccountById(relationship.getRelAccount().getId()).getUsername());
            System.out.println("status: " + relationship.getRelationshipStatus());
            accounts.put(
                    "Src: " + accountService.getAccountById(relationship.getSrcAccount().getId()).getUsername(),
                    "Rel: " + accountService.getAccountById(relationship.getRelAccount().getId()).getUsername());

            returnedRelationships.put(
                    accounts,
                    relationship.getRelationshipStatus());
        }
    }
}
