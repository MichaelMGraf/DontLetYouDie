package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service("relationshipService")
@RequiredArgsConstructor
public class RelationshipService {

    private final AccountService accountService;
    private final RelationshipRepository relationshipRepository;

    public void configureRelationTable() {
        relationshipRepository.addUniqueConstrain();
    }

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


    public List<String> getPendingFriendRequests(String username) throws AccountNotFoundException {

        Account account = accountService.getAccount(username);

        // Find relationships initiated by the account, user will always be owner of related account
        Optional<List<Relationship>> relationships = relationshipRepository.findRelationshipsByRelAccount(account);

        List<String> friendCandidates = new ArrayList<>();

        if (relationships.isPresent()) {
            relationships.ifPresent(relationshipsToExtract -> extractPendingFriendRequests(relationshipsToExtract, friendCandidates));
        }

        return friendCandidates;
    }
  
  
    public Relationship getRelationship(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException {
        Account account1 = accountService.getAccount(srcAccount);
        Account account2 = accountService.getAccount(relAccount);

        Optional<Relationship> relationshipO =
                relationshipRepository.findRelationshipBySrcAccountAndRelAccount(account1, account2);
        if (relationshipO.isEmpty()) {
            relationshipO =
                    relationshipRepository.findRelationshipBySrcAccountAndRelAccount(account2, account1);
            if (relationshipO.isEmpty()) throw new RelationshipNotFoundException(srcAccount, relAccount);
        }
        return relationshipO.get();
    }


    public void accept(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        Relationship relationship = getRelationship(srcAccount, relAccount);
        if (relationship.getRelationshipStatus() != RelationshipStatus.PENDING)
            throw new RelationshipStatusException(srcAccount, relAccount, relationship.getRelationshipStatus(), RelationshipStatus.PENDING);

        relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);
    }

    public void delete(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        relationshipRepository.delete(getRelationship(srcAccount, relAccount));
    }


    public List<String> getFriendsString(String username) throws AccountNotFoundException {

        Account account = accountService.getAccount(username);

        return getFriendsAccount(account)
                .stream()
                .map(Account::getUsername)
                .toList();
    }

    public List<Account> getFriendsAccount(Account account) {

        // Find relationships either initiated by the account or received by the account
        Optional<List<Relationship>> relationshipsSource = relationshipRepository.findRelationshipsBySrcAccount(account);
        Optional<List<Relationship>> relationshipsRelated = relationshipRepository.findRelationshipsByRelAccount(account);

        List<Account> friends = new ArrayList<>();

        if (relationshipsSource.isPresent()) {
            List<Account> accountList = extractAccountList(relationshipsSource.get(), Relationship::getSrcAccount);
            friends.addAll(accountList);
        }
        if (relationshipsRelated.isPresent()) {
            List<Account> accountList = extractAccountList(relationshipsRelated.get(), Relationship::getRelAccount);
            friends.addAll(accountList);
        }
        return friends;
    }

    private static List<Account> extractAccountList(List<Relationship> relationshipList, Function<Relationship, Account> mapFunction) {
        return relationshipList
                .stream()
                .filter(r -> r.getRelationshipStatus() == RelationshipStatus.FRIEND)
                .map(mapFunction)
                .toList();
    }

    private static void extractPendingFriendRequests(List<Relationship> relationships, List<String> friendCandidates) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.PENDING) {
                //Since user was always owner of related account, here we return source-account-user
                friendCandidates.add(relationship.getSrcAccount().getUsername());
            }
        }
    }
}
