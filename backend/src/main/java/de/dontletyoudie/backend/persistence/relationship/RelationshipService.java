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

import java.util.ArrayList;
import java.util.List;
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

    public String delete(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        Optional<Relationship> relationshipO = getRelationship(srcAccount, relAccount);
        if (relationshipO.isEmpty()) throw new RelationshipNotFoundException(srcAccount, relAccount);

        relationshipRepository.delete(relationshipO.get());
        return "Friendship successfully deleted";
    }


    public List<String> getFriends(String username) throws AccountNotFoundException {

        Account account = accountService.getAccount(username);

        // Find relationships either initiated by the account or received by the account
        Optional<List<Relationship>> relationshipsSource = relationshipRepository.findRelationshipsBySrcAccount(account);
        Optional<List<Relationship>> relationshipsRelated = relationshipRepository.findRelationshipsByRelAccount(account);

        List<String> friends = new ArrayList<>();

        if (relationshipsSource.isPresent() && relationshipsRelated.isPresent()) {
            relationshipsSource.get().addAll(relationshipsRelated.get());
            extractFriends(relationshipsSource.get(), friends, username);
        } else if (relationshipsSource.isPresent()) {
            relationshipsSource.ifPresent(relationships -> extractFriends(relationshipsSource.get(), friends, username));
        } else if (relationshipsRelated.isPresent()) {
            relationshipsRelated.ifPresent(relationships -> extractFriends(relationshipsRelated.get(), friends, username));
        }

        return friends;
    }


    private static void extractPendingFriendRequests(List<Relationship> relationships, List<String> friendCandidates) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.PENDING) {
                //Since user was always owner of related account, here we return source-account-user
                friendCandidates.add(relationship.getSrcAccount().getUsername());
            }
        }
    }

    private static void extractFriends(List<Relationship> relationships, List<String> friends, String username) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.FRIEND) {
                    friends.add(relationship.getSrcAccount().getUsername().equals(username)? relationship.getRelAccount().getUsername() : username);
            }
        }
    }

}
